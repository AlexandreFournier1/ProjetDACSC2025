package hepl.dacsc.lib;


import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.lib.reponse.*;
import hepl.dacsc.lib.requete.*;
import hepl.dacsc.model.dao.ConsultationDAO;
import hepl.dacsc.model.dao.DoctorDAO;
import hepl.dacsc.model.dao.RapportDAO;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.entity.Rapport;
import hepl.dacsc.model.viewmodel.ConsultationSearchVM;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;
import hepl.dacsc.model.viewmodel.RapportSearchVM;
import hepl.dacsc.utils.KeyUtils;
import hepl.dacsc.utils.KeystoreUtils;
import hepl.dacsc.utils.SessionKeyUtils;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MRPS implements Protocol {

    @Override
    public String getNom() {
        return "MRPS";
    }
    private final HashMap<String, String> salts = new HashMap<>();
    private KeyStore ksServer;
    private KeyStore ksClient;
    private PublicKey clientPublicKey;
    private PrivateKey serverPrivateKey;
    private final Map<Socket, SecretKey> sessionKeys = new HashMap<>();
    private final Map<Socket, Integer> connectedDoctorIds = new HashMap<>();

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        System.out.println("=== MRPS LOGIN CALLED ===");

        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);
        if (requete instanceof RequeteLOGIN_DIGEST) return TraitementLOGIN_DIGEST((RequeteLOGIN_DIGEST) requete, socket);
        if (requete instanceof RequeteLOGOUT) return TraitementLOGOUT((RequeteLOGOUT) requete, socket);

        if (requete instanceof RequeteADD_REPORT) return TraitementADD_REPORT((RequeteADD_REPORT) requete, socket);

        if (requete instanceof RequeteEDIT_REPORT) return TraitementEDIT_REPORT((RequeteEDIT_REPORT) requete, socket);

        if (requete instanceof RequeteLIST_REPORTS) return TraitementLIST_REPORTS((RequeteLIST_REPORTS) requete, socket);

        return new ReponseERREUR("Requête non reconnue par le protocole MRPS");
    }

    private synchronized ReponseLOGIN TraitementLOGIN(RequeteLOGIN requete) {

        String firstname = requete.getFirstname();
        String lastname = requete.getLastname();

        DoctorDAO dao = new DoctorDAO();
        DoctorSearchVM search = new DoctorSearchVM();

        search.setFirst_name(firstname);
        search.setLast_name(lastname);

        ArrayList<Doctor> doctor = dao.loadDoctor(search);

        if (doctor.isEmpty()) {
            return new ReponseLOGIN(false, "Médecin inexistant");
        }

        // Générer le sel
        ReponseLOGIN rep = new ReponseLOGIN(true, "OK");

        salts.put(lastname + firstname, rep.getSalt());

        return rep;
    }

    private synchronized ReponseLOGIN_DIGEST TraitementLOGIN_DIGEST(RequeteLOGIN_DIGEST requete, Socket socket) {
        byte[] encryptedSessionKey = null;
        try {
            String firstname = requete.getFirstname();
            String lastname = requete.getLastname();

            byte[] digestClient = requete.getDigest();

            // Sel mémorisé
            String salt = salts.get(lastname + firstname);
            if (salt == null) {
                return new ReponseLOGIN_DIGEST(false, encryptedSessionKey);
            }

            DoctorDAO dao = new DoctorDAO();
            Doctor doctor = dao.getDoctorByName(lastname, firstname);

            if (doctor == null) {
                return new ReponseLOGIN_DIGEST(false, encryptedSessionKey);
            }

            String passwordBD = doctor.getMdp();

            System.out.println("[DEBUG] passwordBD = " + passwordBD);

            // Digest Local
            MessageDigest md = MessageDigest.getInstance("SHA-256", "BC");
            md.update(firstname.getBytes());
            md.update(lastname.getBytes());
            md.update(passwordBD.getBytes());
            md.update(salt.getBytes());
            byte[] digestServeur = md.digest();

            boolean ok = MessageDigest.isEqual(digestClient, digestServeur);

            salts.remove(lastname, firstname);

            ksServer = KeystoreUtils.loadKeystore("KeystoreServer.jks", "123456789");
            serverPrivateKey = KeyUtils.getPrivateKey(ksServer, "mrpskey", "123456789");

            ksClient = KeystoreUtils.loadKeystore("KeystoreClient.jks", "123456789");
            clientPublicKey = KeyUtils.getPublicKey(ksClient, "mrpskey");

            if (!ok) {
                return new ReponseLOGIN_DIGEST(false, encryptedSessionKey);
            }

            SecretKey sessionKey = SessionKeyUtils.generateSessionKey();
            encryptedSessionKey = MyCrypto.CryptAsymRSA(sessionKey.getEncoded(), clientPublicKey);
            sessionKeys.put(socket, sessionKey);

            connectedDoctorIds.put(socket, doctor.getId());

            return new ReponseLOGIN_DIGEST(true, encryptedSessionKey);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseLOGIN_DIGEST(false, encryptedSessionKey);
        }
    }

    private synchronized ReponseLOGOUT TraitementLOGOUT(RequeteLOGOUT requete, Socket socket) {
        sessionKeys.remove(socket);
        System.out.println("[SERVER] Logout effectué, clé de session supprimée");
        return new ReponseLOGOUT(true);
    }

    private synchronized ReponseADD_REPORT TraitementADD_REPORT(RequeteADD_REPORT requete, Socket socket) {
        SecretKey sessionKey = sessionKeys.get(socket);
        if (sessionKey == null) {
            return new ReponseADD_REPORT(false);
        }

        Integer doctorId = connectedDoctorIds.get(socket);
        if (doctorId == null) {
            return new ReponseADD_REPORT(false);
        }

        try {
            // Déchiffrement du rapport
            byte[] rapportClairBytes = MyCrypto.DecryptSymDES(sessionKey, requete.getEncryptedReport());
            String rapportClair = new String(rapportClairBytes);

            // Vérification signature
            ksClient = KeystoreUtils.loadKeystore("KeystoreClient.jks", "123456789");
            PublicKey doctorPublicKey =
                    KeyUtils.getPublicKey(ksClient, "mrpskey");

            if (!MyCrypto.VerifyRSASignature(rapportClairBytes, requete.getSignature(), doctorPublicKey)) {
                System.out.println("Verify RSASignature failed");
                return new ReponseADD_REPORT(false);
            }

            // Vérification consultation médecin / patient
            ConsultationDAO consultDAO = new ConsultationDAO();
            ConsultationSearchVM vm = new ConsultationSearchVM();
            vm.setDoctor_id(doctorId);
            vm.setPatient_id(requete.getPatientId());

            if (consultDAO.loadConsultations(vm).isEmpty()) {
                System.out.println("Consultation not found");
                return new ReponseADD_REPORT(false);
            }

            // Insertion du rapport
            Rapport rapport = new Rapport();
            rapport.setIdDoctor(doctorId);
            rapport.setIdPatient(requete.getPatientId());
            rapport.setDate(requete.getReportDate());
            rapport.setTextRapport(rapportClair);

            RapportDAO rapportDAO = new RapportDAO();
            rapportDAO.save(rapport);

            return new ReponseADD_REPORT(true);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseADD_REPORT(false);
        }
    }
    private synchronized ReponseEDIT_REPORT TraitementEDIT_REPORT(RequeteEDIT_REPORT requete, Socket socket) {

        SecretKey sessionKey = sessionKeys.get(socket);
        if (sessionKey == null) {
            return new ReponseEDIT_REPORT(false);
        }

        try {
            // Déchiffrement du nouveau texte
            byte[] clearBytes = MyCrypto.DecryptSymDES(sessionKey, requete.getEncryptedReport());
            String newText = new String(clearBytes);

            // Update
            RapportDAO dao = new RapportDAO();
            RapportSearchVM search = new RapportSearchVM();
            search.setId(requete.getReportId());
            List<Rapport> result = dao.loadRapports(search);
            if(result.isEmpty()) {
                return new ReponseEDIT_REPORT(false);
            }
            Rapport save = result.get(0);
            save.setTextRapport(newText);

            dao.save(save);

            return new ReponseEDIT_REPORT(true);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseEDIT_REPORT(false);
        }
    }


    private synchronized ReponseLIST_REPORTS TraitementLIST_REPORTS(RequeteLIST_REPORTS requete, Socket socket) {
        SecretKey sessionKey = sessionKeys.get(socket);
        if (sessionKey == null) {
            return new ReponseLIST_REPORTS(false, null, null);
        }

        try {
            RapportSearchVM vm = new RapportSearchVM();

            if (requete.getPatientId() != null) {
                vm.setIdPatient(requete.getPatientId());
            }

            RapportDAO dao = new RapportDAO();
            var rapports = dao.loadRapports(vm);

            List<byte[]> encryptedReports = new ArrayList<>();

            for (Rapport r : rapports) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(r);
                oos.close();

                byte[] serializedRapport = baos.toByteArray();
                byte[] encrypted = MyCrypto.CryptSymDES(sessionKey, serializedRapport);

                encryptedReports.add(encrypted);
            }

            ByteArrayOutputStream hmacStream = new ByteArrayOutputStream();
            for (byte[] b : encryptedReports) {
                hmacStream.write(b);
            }

            byte[] hmac = MyCrypto.computeHmac(sessionKey, hmacStream.toByteArray());

            return new ReponseLIST_REPORTS(true, encryptedReports, hmac);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseLIST_REPORTS(false, null, null);
        }
    }
}

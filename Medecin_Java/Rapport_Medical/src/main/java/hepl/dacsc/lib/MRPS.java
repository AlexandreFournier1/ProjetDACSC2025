package hepl.dacsc.lib;


import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.lib.reponse.ReponseLOGIN;
import hepl.dacsc.lib.reponse.ReponseLOGIN_DIGEST;
import hepl.dacsc.lib.requete.RequeteLOGIN;
import hepl.dacsc.lib.requete.RequeteLOGIN_DIGEST;
import hepl.dacsc.model.dao.DoctorDAO;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.utils.KeyUtils;
import hepl.dacsc.utils.KeystoreUtils;
import hepl.dacsc.utils.SessionKeyUtils;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
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

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {

        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);
        if (requete instanceof RequeteLOGIN_DIGEST) return TraitementLOGIN_DIGEST((RequeteLOGIN_DIGEST) requete, socket);
//
//        if (requete instanceof RequeteADD_REPORT) return TraitementADD_REPORT((RequeteADD_REPORT) requete);
//
//        if (requete instanceof RequeteEDIT_REPORT) return TraitementEDIT_REPORT((RequeteEDIT_REPORT) requete);
//
//        if (requete instanceof RequeteLIST_REPORTS) return TraitementLIST_REPORTS((RequeteLIST_REPORTS) requete);

        return null;
    }

    private synchronized ReponseLOGIN TraitementLOGIN(RequeteLOGIN requete) {
        String firstname = requete.getFirstname();
        String lastname = requete.getLastname();

        DoctorDAO dao = new DoctorDAO();
        Doctor doctor = dao.getDoctorByName(lastname, firstname);

        if (doctor == null) {
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

            return new ReponseLOGIN_DIGEST(true, encryptedSessionKey);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseLOGIN_DIGEST(false, encryptedSessionKey);
        }
    }
//
//    private synchronized ReponseADD_REPORT TraiteRequeteADD_PATIENT(RequeteADD_REPORT requete) {
//
//        return reponse;
//    }
//
//    private synchronized ReponseEDIT_REPORT TraiteRequeteADD_PATIENT(RequeteEDIT_REPORT requete) {
//
//        return reponse;
//    }
//
//    private synchronized ReponseLIST_REPORTS TraiteRequeteADD_PATIENT(RequeteLIST_REPORTS requete) {
//
//        return reponse;
//    }
}

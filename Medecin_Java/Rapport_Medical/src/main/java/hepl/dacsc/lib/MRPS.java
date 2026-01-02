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

import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class MRPS implements Protocol {

    @Override
    public String getNom() {
        return "MRPS";
    }
    private final HashMap<String, String> salts = new HashMap<>();

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {

        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);
        if (requete instanceof RequeteLOGIN_DIGEST) return TraitementLOGIN_DIGEST((RequeteLOGIN_DIGEST) requete);
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

    private synchronized ReponseLOGIN_DIGEST TraitementLOGIN_DIGEST(RequeteLOGIN_DIGEST requete) {
        try {
            String firstname = requete.getFirstname();
            String lastname = requete.getLastname();

            byte[] digestClient = requete.getDigest();

            // Sel mémorisé
            String salt = salts.get(lastname + firstname);
            if (salt == null) {
                return new ReponseLOGIN_DIGEST(false);
            }

            DoctorDAO dao = new DoctorDAO();
            Doctor doctor = dao.getDoctorByName(lastname, firstname);

            if (doctor == null) {
                return new ReponseLOGIN_DIGEST(false);
            }

            String passwordBD = doctor.getMdp();

            // Digest Local
            MessageDigest md = MessageDigest.getInstance("SHA-256", "BC");
            md.update(firstname.getBytes());
            md.update(lastname.getBytes());
            md.update(passwordBD.getBytes());
            md.update(salt.getBytes());
            byte[] digestServeur = md.digest();

            boolean ok = MessageDigest.isEqual(digestClient, digestServeur);

            salts.remove(lastname, firstname);

            return new ReponseLOGIN_DIGEST(ok);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReponseLOGIN_DIGEST(false);
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

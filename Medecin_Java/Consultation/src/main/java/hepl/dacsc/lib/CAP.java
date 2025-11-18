package hepl.dacsc.lib;

import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.lib.reponse.ReponseADD_CONSULTATION;
import hepl.dacsc.lib.reponse.ReponseADD_PATIENT;
import hepl.dacsc.lib.reponse.ReponseUPDATE_CONSULTATION;
import hepl.dacsc.lib.requete.RequeteADD_CONSULTATION;
import hepl.dacsc.lib.requete.RequeteADD_PATIENT;
import hepl.dacsc.lib.requete.RequeteUPDATE_CONSULTATION;
import hepl.dacsc.model.dao.ConnectDB;
import hepl.dacsc.model.dao.ConsultationDAO;
import hepl.dacsc.model.dao.PatientDAO;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Patient;

import java.net.Socket;
import java.sql.Connection;
import java.time.LocalTime;
import java.util.HashMap;

public class CAP implements Protocol {
    private HashMap<String, String> passwords;
    private HashMap<String, Socket> medecinsConnectes;
    private Logger logger;

    private static Connection conn = null;
    private static int nbConsultation = 0;

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        // LOGIN

        // ADD_CONSULTATION
        if (requete instanceof RequeteADD_CONSULTATION) return TraiteRequeteADD_CONSULTATION((RequeteADD_CONSULTATION) requete);

        // ADD_PATIENT
        if (requete instanceof RequeteADD_PATIENT) return TraiteRequeteADD_PATIENT((RequeteADD_PATIENT) requete);

        // UPDATE_CONSULTATION
        if (requete instanceof RequeteUPDATE_CONSULTATION) return TraiteRequeteUPDATE_CONSULTATION((RequeteUPDATE_CONSULTATION) requete);

        // SEARCH_CONSULTATIONS

        // DELETE_CONSULTATION

        // LOGOUT


        return null;
    }

    // ADD_CONSULTATION
    private synchronized ReponseADD_CONSULTATION TraiteRequeteADD_CONSULTATION(RequeteADD_CONSULTATION requete) {
        logger.Trace("Requete ADD_CONSULTATION reçue !");

        ReponseADD_CONSULTATION reponse;
        LocalTime limit = LocalTime.of(17, 0);

        if (requete.getHour().isAfter(limit)) {
             reponse = new ReponseADD_CONSULTATION(true);
        }
        else {
            reponse = new ReponseADD_CONSULTATION(false);
            Consultation consultation = new Consultation();
            consultation.setDate(requete.getDate());
            consultation.setHour(requete.getHour());
            consultation.setDuree(requete.getDuration());

            ConsultationDAO dao = new ConsultationDAO();
            dao.save(consultation);

            nbConsultation += nbConsultation;
        }

        return reponse;
    }

    // ADD_PATIENT
    private synchronized ReponseADD_PATIENT TraiteRequeteADD_PATIENT(RequeteADD_PATIENT requete) {
        logger.Trace("Requete ADD_PATIENT reçue !");

        Patient patient = new Patient();
        patient.setFirst_name(requete.getPrenom());
        patient.setLast_name(requete.getNom());

        PatientDAO dao = new PatientDAO();
        dao.save(patient);

        Patient newPatient = dao.getPatientByName(requete.getNom(), requete.getPrenom());

        ReponseADD_PATIENT reponse = new ReponseADD_PATIENT(newPatient.getId());

        return reponse;
    }

    // UPDATE_CONSULTATION
    private synchronized ReponseUPDATE_CONSULTATION TraiteRequeteUPDATE_CONSULTATION(RequeteUPDATE_CONSULTATION requete) {
        logger.Trace("Requete UPDATE_CONSULTATION reçue !");
        return null;
    }
    @Override
    public String getNom() {
        return "CAP";
    }

    public static int getNbConsultation() {
        return nbConsultation;
    }

    public static void setNbConsultation(int nbConsultation) {
        CAP.nbConsultation = nbConsultation;
    }
}

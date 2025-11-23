package hepl.dacsc.lib;

import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Logger;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;
import hepl.dacsc.lib.reponse.*;
import hepl.dacsc.lib.requete.*;
import hepl.dacsc.model.dao.ConsultationDAO;
import hepl.dacsc.model.dao.DoctorDAO;
import hepl.dacsc.model.dao.PatientDAO;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.entity.Patient;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;

import java.net.Socket;
import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class CAP implements Protocol {
    private HashMap<String, String> passwords;
    private HashMap<String, Socket> medecinsConnectes;
    private Logger logger = new Logger() {
        @Override
        public void Trace(String message) {
            System.out.println("[PROTOCOL] " + message);
        }
    };;

    private static Connection conn = null;
    private static int nbConsultation = 0;

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        // LOGIN
        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);

        // ADD_CONSULTATION
        if (requete instanceof RequeteADD_CONSULTATION) return TraiteRequeteADD_CONSULTATION((RequeteADD_CONSULTATION) requete);

        // ADD_PATIENT
        if (requete instanceof RequeteADD_PATIENT) return TraiteRequeteADD_PATIENT((RequeteADD_PATIENT) requete);

        // UPDATE_CONSULTATION
        if (requete instanceof RequeteUPDATE_CONSULTATION) return TraiteRequeteUPDATE_CONSULTATION((RequeteUPDATE_CONSULTATION) requete);

        // SEARCH_CONSULTATIONS

        // DELETE_CONSULTATION

        // LOGOUT

        // GET_CONSULTATION -> Pas demandé mais ajout pour afficher les consultations dans la fenêtre
        if (requete instanceof RequeteGET_CONSULTATION) return TraiteRequeteGET_CONSULTATION((RequeteGET_CONSULTATION) requete);


        return null;
    }

    // LOGIN
    private synchronized ReponseLOGIN TraitementLOGIN(RequeteLOGIN requete) {
        logger.Trace("Requete LOGIN reçue de : " + requete.getId());

        DoctorSearchVM doc = new DoctorSearchVM();
        doc.setId(requete.getId());
        doc.setLast_name(requete.getLastName());
        doc.setFirst_name(requete.getFirstName());
        doc.setMdp(requete.getMdp());

        logger.Trace("Début requetteDAO");
        DoctorDAO dao = new DoctorDAO();
        ArrayList<Doctor> result = dao.loadDoctor(doc);
        logger.Trace("Fin requetteDAO");
        System.out.println("=== Résultat loadDoctor() ===");
        for (Doctor d : result) {
            System.out.println(
                    "ID=" + d.getId() +
                            ", specialty_id=" + d.getSpecialty_id() +
                            ", last_name=" + d.getLast_name() +
                            ", first_name=" + d.getFirst_name() +
                            ", mdp=" + d.getMdp()
            );
        }

        if (result.size() > 0)
            System.out.println("=== true ===");
        //return new ReponseLOGIN(true);
        else
            System.out.println("=== false ===");
        //return  new ReponseLOGIN(false);
        return new ReponseLOGIN(true);
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
            consultation.setDoctor_id(requete.getIdDoctor());
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

        //Patient newPatient = dao.getPatientByName(requete.getNom(), requete.getPrenom());

        ReponseADD_PATIENT reponse = new ReponseADD_PATIENT(patient.getId());

        return reponse;
    }

    // UPDATE_CONSULTATION
    private synchronized ReponseUPDATE_CONSULTATION TraiteRequeteUPDATE_CONSULTATION(RequeteUPDATE_CONSULTATION requete) {
        logger.Trace("Requete UPDATE_CONSULTATION reçue !");

        ConsultationDAO dao = new ConsultationDAO();

        ArrayList<Consultation> consultations = dao.loadConsultations();

        Consultation consultation = dao.getConsultationsById(requete.getId());

        if (consultation == null) {
            logger.Trace("Consultation avec ID " + requete.getId() + " introuvable !");
            return new ReponseUPDATE_CONSULTATION(false);
        }

        consultation.setPatient_id(requete.getIdPatient());
        consultation.setReason(requete.getReason());
        consultation.setDate(requete.getNewDate());
        consultation.setHour(requete.getNewHour());
        consultation.setDuree(requete.getDuration());

        dao.save(consultation);
        ReponseUPDATE_CONSULTATION reponse = new ReponseUPDATE_CONSULTATION(true);

        return reponse;
    }

    // GET_CONSULTATION
    private synchronized ReponseGET_CONSULTATION TraiteRequeteGET_CONSULTATION(RequeteGET_CONSULTATION requete) {
        ConsultationDAO dao = new ConsultationDAO();
        ArrayList<Consultation> all = dao.loadConsultations();

        System.out.println("Nombre de consultations chargées : " + all.size());
        System.out.println(all);

        for (Consultation consultation : all) {
            System.out.println("Id : " + consultation.getId());
        }

        ArrayList<Consultation> list = dao.getConsultationsByDoctorId(requete.getDoctorId());

        System.out.println("Id doctor dans CAP : " + requete.getDoctorId());
        System.out.println(list);

        for (Consultation consultation : list) {
            System.out.println("Id : " + consultation.getId());
        }

        ReponseGET_CONSULTATION reponse = new ReponseGET_CONSULTATION(list);

        return reponse;
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

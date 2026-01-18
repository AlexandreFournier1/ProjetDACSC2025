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
import hepl.dacsc.model.viewmodel.ConsultationSearchVM;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;

import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CAP implements Protocol {

    private final Logger logger = message ->
            System.out.println("[CAP] " + message);

    private static int nbConsultation = 0;

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket)
            throws FinConnexionException {

        if (requete instanceof RequeteLOGIN)
            return traiterLOGIN((RequeteLOGIN) requete);

        if (requete instanceof RequeteADD_CONSULTATION)
            return traiterADD_CONSULTATION((RequeteADD_CONSULTATION) requete);

        if (requete instanceof RequeteADD_PATIENT)
            return traiterADD_PATIENT((RequeteADD_PATIENT) requete);

        if (requete instanceof RequeteUPDATE_CONSULTATION)
            return traiterUPDATE_CONSULTATION((RequeteUPDATE_CONSULTATION) requete);

        if (requete instanceof RequeteSEARCH_CONSULTATION)
            return traiterSEARCH_CONSULTATION((RequeteSEARCH_CONSULTATION) requete);

        if (requete instanceof RequeteDELETE_CONSULTATION)
            return traiterDELETE_CONSULTATION((RequeteDELETE_CONSULTATION) requete);

        if (requete instanceof RequeteGET_CONSULTATION)
            return traiterGET_CONSULTATION((RequeteGET_CONSULTATION) requete);

        if (requete instanceof RequeteLOGOUT)
            return new ReponseLOGOUT(true);

        return null;
    }

    private ReponseLOGIN traiterLOGIN(RequeteLOGIN req) {
        logger.Trace("LOGIN reçu pour médecin ID=" + req.getId());

        DoctorSearchVM vm = new DoctorSearchVM();
        vm.setId(req.getId());
        vm.setLast_name(req.getLastName());
        vm.setFirst_name(req.getFirstName());
        vm.setMdp(req.getMdp());

        DoctorDAO dao = new DoctorDAO();
        ArrayList<Doctor> result = dao.loadDoctor(vm);

        boolean ok = !result.isEmpty();
        logger.Trace("LOGIN " + (ok ? "OK" : "REFUSÉ"));

        return new ReponseLOGIN(ok);
    }

    private ReponseADD_CONSULTATION traiterADD_CONSULTATION(
            RequeteADD_CONSULTATION req) {

        logger.Trace("ADD_CONSULTATION reçu");

        LocalTime start = req.getHour();
        int duration = req.getDuration();
        int count = req.getNbConsultation();

        LocalTime limit = LocalTime.of(17, 0);
        LocalTime lastEnd = start.plusMinutes((long) duration * count);

        if (lastEnd.isAfter(limit)) {
            return new ReponseADD_CONSULTATION(true); // dépasse 17h
        }

        ConsultationDAO dao = new ConsultationDAO();
        LocalTime current = start;

        for (int i = 0; i < count; i++) {
            Consultation c = new Consultation();
            c.setDoctor_id(req.getIdDoctor());
            c.setDate(req.getDate());
            c.setHour(current);
            c.setDuree(duration);
            c.setPatient_id(null);
            c.setReason(null);

            dao.save(c);
            current = current.plusMinutes(duration);
            nbConsultation++;
        }

        return new ReponseADD_CONSULTATION(false);
    }

    private ReponseADD_PATIENT traiterADD_PATIENT(RequeteADD_PATIENT req) {
        logger.Trace("ADD_PATIENT reçu");

        Patient p = new Patient();
        p.setLast_name(req.getNom());
        p.setFirst_name(req.getPrenom());

        PatientDAO dao = new PatientDAO();
        dao.save(p);

        return new ReponseADD_PATIENT(p.getId());
    }
    private ReponseUPDATE_CONSULTATION traiterUPDATE_CONSULTATION(
            RequeteUPDATE_CONSULTATION req) {

        logger.Trace("UPDATE_CONSULTATION reçu");

        ConsultationDAO dao = new ConsultationDAO();
        Consultation c = dao.getConsultationsById(req.getId());

        System.out.println("[DEBUG] ID reçu = " + req.getId());
        if (c == null) {
            System.out.println("[DEBUG] ID Consultation null");
            return new ReponseUPDATE_CONSULTATION(false);
        }

        c.setDate(req.getNewDate());
        c.setHour(req.getNewHour());
        c.setDuree(req.getDuration());
        c.setPatient_id(req.getIdPatient());
        c.setReason(req.getReason());

        dao.save(c);
        return new ReponseUPDATE_CONSULTATION(true);
    }

    private ReponseSEARCH_CONSULTATION traiterSEARCH_CONSULTATION(
            RequeteSEARCH_CONSULTATION req) {

        ConsultationSearchVM vm = new ConsultationSearchVM();
        vm.setDoctor_id(req.getDoctorId());

        if (req.getDate() != null)
            vm.setDate(req.getDate());

        if (req.getId() != null && req.getId() != 0)
            vm.setPatient_id(req.getId());

        ConsultationDAO dao = new ConsultationDAO();
        ArrayList<Consultation> result = dao.loadConsultations(vm);

        return new ReponseSEARCH_CONSULTATION(result);
    }

    private ReponseDELETE_CONSULTATION traiterDELETE_CONSULTATION(
            RequeteDELETE_CONSULTATION req) {

        ConsultationDAO dao = new ConsultationDAO();
        dao.delete(req.getConsultationId());

        return new ReponseDELETE_CONSULTATION(true);
    }

    private ReponseGET_CONSULTATION traiterGET_CONSULTATION(
            RequeteGET_CONSULTATION req) {

        ConsultationDAO dao = new ConsultationDAO();
        ArrayList<Consultation> list =
                dao.getConsultationsByDoctorId(req.getDoctorId());

        return new ReponseGET_CONSULTATION(list);
    }

    @Override
    public String getNom() {
        return "CAP";
    }

    public static int getNbConsultation() {
        return nbConsultation;
    }
}

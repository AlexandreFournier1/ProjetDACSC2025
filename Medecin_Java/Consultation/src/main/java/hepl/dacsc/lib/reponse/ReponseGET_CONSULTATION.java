package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.model.entity.Consultation;

import java.util.ArrayList;

public class ReponseGET_CONSULTATION implements Reponse {
    private ArrayList<Consultation> consultations;

    public ReponseGET_CONSULTATION(ArrayList<Consultation> consultations) {
        this.consultations = consultations;
    }

    public ArrayList<Consultation> getConsultations() {
        return consultations;
    }
}

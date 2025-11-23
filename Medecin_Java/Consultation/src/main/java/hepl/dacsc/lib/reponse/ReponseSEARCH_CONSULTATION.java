package hepl.dacsc.lib.reponse;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.model.entity.Consultation;

import java.util.ArrayList;

public class ReponseSEARCH_CONSULTATION implements Reponse {
    private ArrayList<Consultation> consultations;

    public ReponseSEARCH_CONSULTATION(ArrayList<Consultation> consultations) {
        this.consultations = consultations;
    }

    public ArrayList<Consultation> getSearchedConsultations() {
        return consultations;
    }
}

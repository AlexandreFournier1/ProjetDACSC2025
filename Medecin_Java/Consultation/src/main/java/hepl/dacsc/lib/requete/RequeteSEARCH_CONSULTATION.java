package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.time.LocalDate;

public class RequeteSEARCH_CONSULTATION implements Requete {
    private Integer doctorId;
    private Integer id;
    private LocalDate date;

    public RequeteSEARCH_CONSULTATION(Integer doctorId,Integer id, LocalDate date) {
        this.doctorId = doctorId;
        this.id = id;
        this.date = date;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}

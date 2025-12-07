package hepl.dacsc.lib;


import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.net.Socket;


public class MRPS implements Protocol {

    @Override
    public String getNom() {
        return "MRPS";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {

//        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);
//
//        if (requete instanceof RequeteADD_REPORT) return TraitementADD_REPORT((RequeteADD_REPORT) requete);
//
//        if (requete instanceof RequeteEDIT_REPORT) return TraitementEDIT_REPORT((RequeteEDIT_REPORT) requete);
//
//        if (requete instanceof RequeteLIST_REPORTS) return TraitementLIST_REPORTS((RequeteLIST_REPORTS) requete);

        return null;
    }

//    private synchronized ReponseLOGIN TraiteRequeteADD_PATIENT(RequeteLogin requete) {
//
//        return reponse;
//    }
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

package hepl.dacsc.lib;


import hepl.dacsc.ServerGeneriqueTCP.exceptions.FinConnexionException;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Protocol;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Reponse;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.net.Socket;


public class MRPS implements Protocol {

    @Override
    public String getNom() {
        return "";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {

        if (requete instanceof RequeteLOGIN) return TraitementLOGIN((RequeteLOGIN) requete);

        if (requete instanceof Requete_ADD_REPORT) return TraitementADD_REPORT((RequeteADD_REPORT) requete);

        if (requete instanceof Requete_EDIT_REPORT) return TraitementEDIT_REPORT((RequeteEDIT_REPORT) requete);

        if (requete instanceof Requete_LIST_REPORTS) return TraitementLIST_REPORTS((RequeteLIST_REPORTS) requete);

    }
}

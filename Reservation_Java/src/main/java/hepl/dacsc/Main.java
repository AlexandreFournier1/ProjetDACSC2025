package hepl.dacsc;

import hepl.dacsc.view.PageConnexion;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Création de la socket et connexion sur le serveur
//        Socket socket = new Socket("192.168.2.128", 60000);
//
//        System.out.println("Connexion établie !");
//
//        // Caractéristiques de la socket
//        System.out.println("--- Socket ---");
//        System.out.println("Adresse IP locale   : " + socket.getLocalAddress().getHostAddress());
//        System.out.println("Port local          : " + socket.getLocalPort());
//        System.out.println("Adresse IP distante : " + socket.getInetAddress().getHostAddress());
//        System.out.println("Port distant        : " + socket.getPort());
//
//        socket.close();

//        RequeteAdmin admin = new RequeteAdmin();
//        admin.Connexion();
//        String rep = admin.GetLoggedPatient();
//        System.out.println(rep);
        PageConnexion pageConnexion = new PageConnexion();
        pageConnexion.setVisible(true);
    }
}
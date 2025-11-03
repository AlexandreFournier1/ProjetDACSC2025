package hepl.dacsc;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;

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



        ClientAdmin clientAdmin = new ClientAdmin();
        clientAdmin.setVisible(true);
    }
}
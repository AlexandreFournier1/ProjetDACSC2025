package hepl.dacsc;

import java.io.*;
import java.net.Socket;

public class RequeteAdmin {
    private Socket socket;
    private String listPatients;
    public void Connexion() throws IOException {
        socket = new Socket("192.168.2.128", 60000);
        System.out.println("Connexion établie !");
    }

    public String GetLoggedPatient() throws IOException {
        if (socket == null) {
            System.out.println("Socket null");
            return null;
        }

        // Création des flux
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        // Envoi de la requête
        String requete = "LIST_CLIENTS#";
        String trame = requete + "/!";

        dos.write(trame.getBytes());
        dos.flush();

        // Récupération de la réponse
        String listPatients = dis.readUTF();

        return listPatients;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getListPatients() {
        return listPatients;
    }
}

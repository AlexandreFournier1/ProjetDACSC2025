package hepl.dacsc.model;

import java.io.IOException;
import java.net.Socket;

public class Admin {
    private Socket socket;

    public void Connexion() throws IOException {
        socket = new Socket("192.168.186.128", 60000);
        System.out.println("Connexion établie !");
    }

    public void Disconnect() throws IOException {
        socket.close();
        System.out.println("Déconnexion réussie !");
    }
    public Socket getSocket() {
        return socket;
    }
}

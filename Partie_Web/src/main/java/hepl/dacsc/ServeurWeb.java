package hepl.dacsc;

import com.sun.net.httpserver.HttpServer;
import hepl.dacsc.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServeurWeb {
    public static void main(String[] args) {
        HttpServer serveur = null;
        try
        {
            serveur = HttpServer.create(new InetSocketAddress(8080),0);

            serveur.createContext("/api/specialties", new HandlerSpecialities());
            serveur.createContext("/api/doctors", new HandlerDoctors());
            serveur.createContext("/api/patients", new HandlerPatients());
            serveur.createContext("/api/consultations", new HandlerConsultations());

            System.out.println("Demarrage du serveur HTTP...");
            serveur.start();
        }
        catch (IOException e)
        {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}

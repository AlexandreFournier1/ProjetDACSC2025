package hepl.dacsc.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestAdmin {
    public ArrayList<Patient> GetLoggedPatient(Admin admin) throws IOException {
        if (admin.getSocket() == null) return null;

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(admin.getSocket().getOutputStream()));
        DataInputStream dis = new DataInputStream(new BufferedInputStream(admin.getSocket().getInputStream()));

        // Envoi de la requête
        String reponse = "LIST_CLIENTS#";
        String trame = reponse + "/!";
        dos.write(trame.getBytes());
        dos.flush();

        // Lecture de la réponse
        StringBuffer buffer = new StringBuffer();
        boolean EOT = false;

        while(!EOT) // boucle de lecture byte par byte
        {
            byte b1 = dis.readByte();
            if (b1 == (byte)'/')
            {
                byte b2 = dis.readByte();
                if (b2 == (byte)'!') EOT = true;
                else
                {
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else buffer.append((char)b1);
        }

        System.out.println("Réponse reçue : " + buffer.toString());

        String response = buffer.toString();

        ArrayList<Patient> patients = new ArrayList<>();

        String[] lines = response.split("#");

        for (String line : lines) {
            if (line == null || line.isEmpty() || line.equals("/!"))
                continue;

            String[] tokens = line.split(";");

            if (tokens.length < 4)
                continue;

            Patient patient = new Patient();

            patient.setIp(tokens[0]);
            patient.setId(Integer.parseInt(tokens[1]));
            patient.setNom(tokens[2]);
            patient.setPrenom(tokens[3]);

            patients.add(patient);
        }

        return patients;
    }
}

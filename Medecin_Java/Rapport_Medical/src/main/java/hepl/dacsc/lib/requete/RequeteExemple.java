package hepl.dacsc.lib.requete;

import hepl.dacsc.lib.MyCrypto;
import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class RequeteExemple implements Requete {
    private byte[] data;

    public RequeteExemple(String nom, int age, SecretKey cle) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
    // Constructon du vecteur de bytes du message clair
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(nom);
        dos.writeInt(age);
        byte[] messageClair = baos.toByteArray();

        // Cryptage du message
        byte[] messageCrypte;
        data = MyCrypto.CryptSymDES(cle,messageClair);
    }

    public String getNom(SecretKey cle) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        // Décryptage du message
        byte[] messageDecrypte;
        messageDecrypte = MyCrypto.DecryptSymDES(cle,data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
        DataInputStream dis = new DataInputStream(bais);
        String nom = dis.readUTF();
        return nom;
    }

    public int getAge(SecretKey cle) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        // Décryptage du message
        byte[] messageDecrypte;
        messageDecrypte = MyCrypto.DecryptSymDES(cle,data);

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
        DataInputStream dis = new DataInputStream(bais);
        String nom = dis.readUTF();
        int age = dis.readInt();
        return age;
    }
}
package hepl.dacsc;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class GenereCleDES {

    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        Security.addProvider(new BouncyCastleProvider());

        // Génération de la clé secrète
        KeyGenerator cleGen = KeyGenerator.getInstance("DES", "BC");
        cleGen.init(new SecureRandom());
        SecretKey key = cleGen.generateKey();
        System.out.println("**** Clé généré = " + key.toString());

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("cleSecrete.ser"));
        oos.writeObject(key);
        oos.close();
        System.out.println("Serialisation de la clé dans le fichier cleSecrete.ser");
    }
}

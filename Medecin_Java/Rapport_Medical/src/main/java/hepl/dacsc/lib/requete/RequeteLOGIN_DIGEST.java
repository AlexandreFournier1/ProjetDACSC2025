package hepl.dacsc.lib.requete;

import hepl.dacsc.ServerGeneriqueTCP.interfaces.Requete;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

// Digest envoyé au serveur par le client
public class RequeteLOGIN_DIGEST implements Requete {
    private String firstname;
    private String lastname;
    private byte[] digest;

    public RequeteLOGIN_DIGEST(String firstname, String lastname, String password, String salt) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.firstname = firstname;
        this.lastname = lastname;

        // Construction du digest salé
        MessageDigest md = MessageDigest.getInstance("SHA-256","BC");
        md.update(firstname.getBytes());
        md.update(lastname.getBytes());
        md.update(password.getBytes());
        md.update(salt.getBytes());
        digest = md.digest();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public byte[] getDigest() { return digest; }
}

package hepl.dacsc;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class GenerateKeystore {

    public static void main(String[] args) throws Exception {

        // Enregistrement du provider BouncyCastle
        Security.addProvider(new BouncyCastleProvider());

        String keystoreName = "KeystoreClient.jks"; // ou KeystoreServer.jks

        // Mots de passe
        String keystorePassword = "123456789";
        String keyPassword = "123456789";

        String keyAlias = "mrpskey";

        // Génération paire de clés RSA
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        // Création certificat X509 auto-signé
        X509Certificate cert = generateSelfSignedCertificate(keyPair);

        // Création du keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, keystorePassword.toCharArray());

        // Stockage de la clé privée avec son mot de passe
        ks.setKeyEntry(keyAlias, keyPair.getPrivate(), keyPassword.toCharArray(), new java.security.cert.Certificate[]{cert});

        // Sauvegarde du keystore
        try (FileOutputStream fos = new FileOutputStream(keystoreName)) {
            ks.store(fos, keystorePassword.toCharArray());
        }

        System.out.println("Keystore créé : " + keystoreName);
    }

    private static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {

        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Date endDate = new Date(now + 365L * 24 * 60 * 60 * 1000);

        BigInteger serial = BigInteger.valueOf(now);
        X500Name dnName = new X500Name("CN=MRPS");
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.getPrivate());
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, serial, startDate, endDate, dnName, keyPair.getPublic());
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(signer));
    }
}
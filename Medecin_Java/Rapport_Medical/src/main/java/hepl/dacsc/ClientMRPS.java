package hepl.dacsc;

import hepl.dacsc.lib.MyCrypto;
import hepl.dacsc.lib.reponse.ReponseLOGIN;
import hepl.dacsc.lib.reponse.ReponseLOGIN_DIGEST;
import hepl.dacsc.lib.reponse.ReponseLOGOUT;
import hepl.dacsc.lib.requete.RequeteLOGIN;
import hepl.dacsc.lib.requete.RequeteLOGIN_DIGEST;
import hepl.dacsc.lib.requete.RequeteLOGOUT;
import hepl.dacsc.utils.KeyUtils;
import hepl.dacsc.utils.KeystoreUtils;
import hepl.dacsc.view.JDialog.LoginJDialog;
import hepl.dacsc.view.error.ErrorMessage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.Properties;

public class ClientMRPS extends JFrame {

    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel workArea;
    private DefaultTableModel tableModel;
    private SecretKey cleSession;
    private ErrorMessage errMsg = new ErrorMessage();
    private PrivateKey clientPrivateKey;

    public ClientMRPS() {
        Security.addProvider(new BouncyCastleProvider());
        initComponents();

        oos = null;
        ois = null;
    }

    private void initComponents() {
        setName("Client Rapport Médical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JButton btnLogin = new JButton("Login");
        JButton btnLogout = new JButton("Logout");
        btnLogout.setVisible(false);

        btnLogin.addActionListener(e -> {
            try {
                jButtonLoginActionPerformed(e, btnLogin, btnLogout);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnLogout.addActionListener(e -> {
            try {
                jButtonLogoutActionPerformed(e, btnLogin, btnLogout);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnLogin);
        topPanel.add(btnLogout);
        add(topPanel, BorderLayout.NORTH);

        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }
    private void showWorkPanel() {
        workArea.removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topButtons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Encoder Rapport");
        JButton btnEdit = new JButton("Modifier Rapport");
        JButton btnList = new JButton("Lister Rapports");
        JButton btnShow = new JButton("Afficher Texte");

        topButtons.add(btnAdd);
        topButtons.add(btnEdit);
        topButtons.add(btnList);
        topButtons.add(btnShow);

        String[] cols = {"ID", "Médecin", "Patient", "Date"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        mainPanel.add(topButtons, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        workArea.add(mainPanel);
        workArea.revalidate();
        workArea.repaint();

        btnAdd.addActionListener(e -> {
            try {
                jButtonADD_REPORT(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnList.addActionListener(e -> {
            try{
                jButtonLIST_REPORTS(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEdit.addActionListener(e -> {
            try {
                jButtonEDIT_REPORT(e, table);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnShow.addActionListener(e -> {
            try {
                jButtonSHOW_REPORT_TEXT(table);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


    }
    private void connect() throws IOException {
        if (socket == null) {
            Properties props = new Properties();
            props.load(new FileInputStream("src/main/java/hepl/dacsc/config.properties"));

            int port = Integer.parseInt(props.getProperty("PORT_REPORT_SECURE"));

            // Ip PC Noah
            //socket = new Socket("10.236.71.53", port);
            // Ip PC Alex
            socket = new Socket("192.168.56.1", port);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
    }

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) throws Exception {
        LoginJDialog login = new LoginJDialog(this);
        login.setVisible(true);

        if (login.isAuthenticated()) {
            connect();

            KeyStore ksClient = KeystoreUtils.loadKeystore("KeystoreClient.jks", "123456789");
            clientPrivateKey = KeyUtils.getPrivateKey(ksClient, "mrpskey", "123456789");

            String lastname = login.getTxtLastname();
            String firstname = login.getTxtFirstname();
            String password = login.getPassword();

            // Envoie du Login
            RequeteLOGIN reqLogin = new RequeteLOGIN(firstname, lastname);
            oos.writeObject(reqLogin);
            oos.flush();

            // Récupération du sel envoyé par le serveur
            ReponseLOGIN repLogin = (ReponseLOGIN) ois.readObject();

            if (!repLogin.isSuccess()) {
                errMsg.showErrorMessage(this, "Login inconnu");
                return;
            }

            String salt = repLogin.getSalt();

            // Envoie du digest
            RequeteLOGIN_DIGEST reqDigest = new RequeteLOGIN_DIGEST(firstname, lastname, password, salt);
            oos.writeObject(reqDigest);
            oos.flush();

            // Réponse finale
            ReponseLOGIN_DIGEST repDigest = (ReponseLOGIN_DIGEST) ois.readObject();

            if (!repDigest.isSuccess()) {
                errMsg.showErrorMessage(this, "Mot de passe incorrect");
                return;
            }

            byte[] encryptedSessionKey = repDigest.getEncryptedSessionKey();

            // Déchiffrement RSA
            byte[] rawKey = MyCrypto.DecryptAsymRSA(encryptedSessionKey, clientPrivateKey);

            // Reconstruction clé AES
            cleSession = MyCrypto.rebuildAESKey(rawKey);

            errMsg.showMessage(this, "Authentification réussie !", "Success");
            btnLogin.setVisible(false);
            btnLogout.setVisible(true);
            showWorkPanel();
        }
    }

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) throws IOException, ClassNotFoundException {
        // Envoi requête LOGOUT
        if (socket == null || oos == null || ois == null) return;

        oos.writeObject(new RequeteLOGOUT());
        oos.flush();

        ReponseLOGOUT rep = (ReponseLOGOUT) ois.readObject();

        if (rep.isSuccess()) {
            System.out.println("[CLIENT] Logout confirmé par le serveur");
        }

        // Nettoyage client
        cleSession = null;
        clientPrivateKey = null;

        ois.close();
        oos.close();
        socket.close();

        socket = null;

        btnLogout.setVisible(false);
        btnLogin.setVisible(true);

        workArea.removeAll();
        workArea.revalidate();
        workArea.repaint();
    }

    private void jButtonADD_REPORT(ActionEvent evt) {

    }

    private void jButtonEDIT_REPORT(ActionEvent evt, JTable table) {

    }

    private void jButtonLIST_REPORTS(java.awt.event.ActionEvent evt) {

    }

    private void jButtonSHOW_REPORT_TEXT(JTable evt) {

    }
}

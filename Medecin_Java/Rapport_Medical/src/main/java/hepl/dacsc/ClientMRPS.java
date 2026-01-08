package hepl.dacsc;

import hepl.dacsc.lib.MyCrypto;
import hepl.dacsc.lib.reponse.ReponseLIST_REPORTS;
import hepl.dacsc.lib.reponse.ReponseLOGIN;
import hepl.dacsc.lib.reponse.ReponseLOGIN_DIGEST;
import hepl.dacsc.lib.reponse.ReponseLOGOUT;
import hepl.dacsc.lib.requete.RequeteLIST_REPORTS;
import hepl.dacsc.lib.requete.RequeteLOGIN;
import hepl.dacsc.lib.requete.RequeteLOGIN_DIGEST;
import hepl.dacsc.lib.requete.RequeteLOGOUT;
import hepl.dacsc.model.entity.Rapport;
import hepl.dacsc.utils.KeyUtils;
import hepl.dacsc.utils.KeystoreUtils;
import hepl.dacsc.view.JDialog.LoginJDialog;
import hepl.dacsc.view.JTextArea.RapportJTextArea;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ClientMRPS extends JFrame {

    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel workArea;
    private JTextField txtIdPatient;
    private DefaultTableModel tableModel;
    private SecretKey cleSession;
    private ErrorMessage errMsg = new ErrorMessage();
    private PrivateKey clientPrivateKey;
    private List<Rapport> listRapport = new ArrayList<Rapport>();

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

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel lblIdPatient = new JLabel("id patient:");
        txtIdPatient = new JTextField(10);
        JButton btnSearch = new JButton("Chercher Rapports");

        searchPanel.add(lblIdPatient);
        searchPanel.add(txtIdPatient);
        searchPanel.add(btnSearch);

        String[] cols = {"ID", "Médecin", "Patient", "Date"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.add(topButtons);
        topSection.add(Box.createVerticalStrut(5));
        topSection.add(searchPanel);

        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        workArea.add(mainPanel, BorderLayout.CENTER);

        //workArea.add(mainPanel);
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
                jButtonLIST_REPORTS(e, null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnSearch.addActionListener(e -> {
            if (txtIdPatient.getText().trim().isEmpty()) {
                errMsg.showWarningMessage(this, "Veuillez indiquer un id", "Warning");
            }
            else {
                try{
                    jButtonLIST_REPORTS(e, Integer.parseInt(txtIdPatient.getText()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
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
            socket = new Socket("192.168.0.81", port);
            // Ip PC Alex
            //socket = new Socket("192.168.56.1", port);

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

            // Reconstruction clé DES
            cleSession = MyCrypto.rebuildDESKey(rawKey);

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

    private void jButtonLIST_REPORTS(ActionEvent evt, Integer patientId) throws Exception {
        tableModel.setRowCount(0);
        listRapport.clear();

        oos.writeObject(new RequeteLIST_REPORTS(patientId));
        oos.flush();

        ReponseLIST_REPORTS rep = (ReponseLIST_REPORTS) ois.readObject();

        if (!rep.isSuccess()) {
            errMsg.showErrorMessage(this, "Erreur LIST_REPORTS");
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte[] b : rep.getEncryptedReports()) {
            baos.write(b);
        }

        byte[] localHmac =
                MyCrypto.computeHmac(cleSession, baos.toByteArray());

        if (!MessageDigest.isEqual(localHmac, rep.getHmac())) {
            errMsg.showErrorMessage(this, "HMAC invalide");
            return;
        }

        for (byte[] encrypted : rep.getEncryptedReports()) {
            byte[] clear = MyCrypto.DecryptSymDES(cleSession, encrypted);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(clear));
            Rapport r = (Rapport) ois.readObject();

            if (r == null) {
                errMsg.showErrorMessage(this, "Aucun de rapports trouvés !");
                return;
            }

            listRapport.add(r);

            tableModel.addRow(new Object[]{r.getId(), r.getIdDoctor(), r.getIdPatient(), r.getDate()});
            System.out.println("[RAPPORT] id=" + r.getId() + " patient=" + r.getIdPatient() + " date=" + r.getDate());
        }
    }

    private void jButtonSHOW_REPORT_TEXT(JTable evt) {
        int row = evt.getSelectedRow();

        if (row == -1) {
            errMsg.showWarningMessage(this, "Veuillez sélectionner une consultation dans le tableau.", "Aucune sélection");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        for (Rapport rapport : listRapport) {
            if (rapport.getId().equals(id)) {
                RapportJTextArea dialog =
                        new RapportJTextArea(this, rapport, false);
                dialog.setVisible(true);
                return;
            }
        }

        errMsg.showErrorMessage(this, "Rapport introuvable en mémoire.");
    }
}

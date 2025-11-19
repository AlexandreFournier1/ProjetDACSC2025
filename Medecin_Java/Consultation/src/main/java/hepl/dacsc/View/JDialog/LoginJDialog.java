package hepl.dacsc.View.JDialog;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class LoginJDialog extends JDialog {
    private final JTextField txtId;
    private final JTextField txtNom;
    private final JTextField txtPrenom;
    private final JPasswordField txtPassword;
    private boolean authenticated = false;
    private boolean confirmed = false;

    public LoginJDialog(JFrame parent) {
        super(parent, "Connexion", true);

        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // ===== PANEL PRINCIPAL =====
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPanel);

        // ===== FORMULAIRE DE CONNEXION =====
        JPanel fieldPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.LINE_END;

        // === Ligne 1 : ID ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldPanel.add(new JLabel("ID :"), gbc);

        gbc.gridx = 1;
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtId, gbc);

        // === Ligne 2 : Nom ===
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        txtNom = new JTextField();
        txtNom.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtNom, gbc);

        // === Ligne 3 : Prénom ===
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldPanel.add(new JLabel("Prénom :"), gbc);

        gbc.gridx = 1;
        txtPrenom = new JTextField();
        txtPrenom.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtPrenom, gbc);

        // === Ligne 4 : Mot de passe ===
        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldPanel.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtPassword, gbc);

        contentPanel.add(fieldPanel, BorderLayout.CENTER);

        // ===== BOUTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnConnexion = new JButton("Connexion");
        JButton btnAnnuler = new JButton("Annuler");
        buttonPanel.add(btnConnexion);
        buttonPanel.add(btnAnnuler);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        btnConnexion.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (id.isEmpty() || nom.isEmpty() || prenom.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            // Ici, tu pourras appeler ton DAO / AuthService :
            // DoctorDAO dao = new DoctorDAOImpl(...);
            // boolean ok = dao.authenticate(id, nom, prenom, password);
            System.out.println("Tentative de connexion :");
            System.out.println("  ID = " + id);
            System.out.println("  Nom = " + nom);
            System.out.println("  Prénom = " + prenom);
            System.out.println("  MDP = " + password);

            // Exemple de succès fictif :
            //authenticated = true;
            dispose();
        });

        btnAnnuler.addActionListener(e -> dispose());
    }
    public boolean isConfirmed() { return confirmed; }

    public String getId(){
        return txtId.getText().trim();
    }
    public String getLastName(){
        return txtNom.getText().trim();
    }
    public String getFirstName(){
        return txtPrenom.getText().trim();
    }
    public String getMdp(){
        return txtPassword.getText().trim();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

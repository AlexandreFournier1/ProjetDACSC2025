package hepl.dacsc.view.JDialog;

import hepl.dacsc.view.error.ErrorMessage;

import javax.swing.*;
import java.awt.*;

public class LoginJDialog extends JDialog {
    private final JTextField txtLastname;
    private final JTextField txtFirstname;
    private final JPasswordField txtPassword;
    private boolean authenticated = false;
    private boolean confirmed = false;
    private ErrorMessage errMsg = new ErrorMessage();
    public LoginJDialog(JFrame parent) {
        super(parent, "Login", true);

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

        // === Ligne 1 : LASTNAME ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldPanel.add(new JLabel("Last Name :"), gbc);

        gbc.gridx = 1;
        txtLastname = new JTextField();
        txtLastname.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtLastname, gbc);

        // === Ligne 2 : FIRSTNAME ===
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldPanel.add(new JLabel("Last Name :"), gbc);

        gbc.gridx = 1;
        txtFirstname = new JTextField();
        txtFirstname.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtFirstname, gbc);

        // === Ligne 3 : MDP ===
        gbc.gridx = 0;
        gbc.gridy = 2;
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
            String lastname = txtLastname.getText().trim();
            String firstname = txtFirstname.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (lastname.isEmpty() || firstname.isEmpty() || password.isEmpty()) {
                errMsg.showErrorMessage(parent, "Tous les champs sont obligatoires.");
                return;
            }

            confirmed = true;

            System.out.println("Tentative de connexion :");
            System.out.println("  Login = " + lastname + " " + firstname);
            System.out.println("  Mot de passe = " + password);

            authenticated = true;
            dispose();
        });

        btnAnnuler.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() { return confirmed; }

    public String getTxtLastname() {
        return txtLastname.getText().trim();
    }

    public String getTxtFirstname() {
        return txtFirstname.getText().trim();
    }

    public String getPassword(){
        return txtPassword.getText().trim();
    }
    public boolean isAuthenticated() {
        return authenticated;
    }
}

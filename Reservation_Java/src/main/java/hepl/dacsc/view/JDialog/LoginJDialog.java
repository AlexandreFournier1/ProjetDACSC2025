package hepl.dacsc.view.JDialog;

import javax.swing.*;
import java.awt.*;

public class LoginJDialog extends JDialog {
    private final JTextField txtNom;
    private final JPasswordField txtPassword;
    private boolean authenticated = false;
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

        // === Ligne 1 : Nom ===
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        txtNom = new JTextField();
        txtNom.setPreferredSize(new Dimension(180, 25));
        fieldPanel.add(txtNom, gbc);

        // === Ligne 2 : Mot de passe ===
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
            String nom = txtNom.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (nom.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            authenticated = true;
            dispose();
        });

        btnAnnuler.addActionListener(e -> dispose());
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

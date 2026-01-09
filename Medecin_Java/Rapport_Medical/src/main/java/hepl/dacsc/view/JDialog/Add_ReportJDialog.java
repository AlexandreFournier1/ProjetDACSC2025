package hepl.dacsc.view.JDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class Add_ReportJDialog extends JDialog {

    private JTextField txtIdPatient;
    private JTextArea txtRapport;
    private JFormattedTextField dateField;

    private boolean confirmed = false;

    public Add_ReportJDialog(JFrame parent) {
        super(parent, "Encoder un rapport médical", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 450);
        setLayout(new BorderLayout(10, 10));

        // ===== PANEL FORM =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // ---- ID PATIENT ----
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID Patient :"), gbc);

        gbc.gridx = 1;
        txtIdPatient = new JTextField(10);
        formPanel.add(txtIdPatient, gbc);

        // ---- DATE ----
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Date :"), gbc);

        gbc.gridx = 1;
        dateField = new JFormattedTextField("yyyy-MM-dd");
        dateField.setColumns(10);
        formPanel.add(dateField, gbc);

        // ---- RAPPORT ----
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Rapport :"), gbc);

        gbc.gridx = 1;
        txtRapport = new JTextArea(10, 25);
        txtRapport.setLineWrap(true);
        txtRapport.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtRapport);
        formPanel.add(scroll, gbc);

        add(formPanel, BorderLayout.CENTER);

        // ===== BOUTONS =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnOk = new JButton("Valider");
        JButton btnCancel = new JButton("Annuler");

        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        btnOk.addActionListener(e -> {
            if (txtIdPatient.getText().trim().isEmpty()
                    || txtRapport.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tous les champs sont obligatoires",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                Integer.parseInt(txtIdPatient.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "ID patient invalide",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());
    }

    // ===== GETTERS =====

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getIdPatient() {
        return Integer.parseInt(txtIdPatient.getText());
    }

    public LocalDate getDate() {
        return LocalDate.parse(dateField.getText());
    }

    public String getRapportText() {
        return txtRapport.getText();
    }
}

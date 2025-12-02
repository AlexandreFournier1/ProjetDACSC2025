package hepl.dacsc.view.JDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EncoderRapportJDialog extends JDialog {
    private JTextField txtIdPatient;
    private JTextField txtIdDoctor;
    private JTextField txtDate;
    private JTextField txtMotifConsultation;
    private JTextField txtObservation;
    private JTextField txtDiagnostic;
    private JTextField txtRecommendation;
    private boolean confirmed = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public EncoderRapportJDialog(JFrame parent) {
        super(parent, "Encoder Rapport", true);

        setSize(350, 250);
        setMinimumSize(new Dimension(350, 250));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setContentPane(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== ID PATIENT =====
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Patient :"), gbc);

        gbc.gridx = 1;
        txtIdPatient = new JTextField(15);
        formPanel.add(txtIdPatient, gbc);

        // ===== ID DOCTOR =====
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID Médecin :"), gbc);

        gbc.gridx = 1;
        txtIdDoctor = new JTextField(15);
        formPanel.add(txtIdDoctor, gbc);

        // ===== DATE =====
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date :"), gbc);

        gbc.gridx = 1;
        txtDate = new JTextField(15);
        txtDate.setToolTipText("Format : JJ-MM-AAAA");
        formPanel.add(txtDate, gbc);

        // ===== MOTIF CONSULTATION =====
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Motif Consultation :"), gbc);

        gbc.gridx = 1;
        txtMotifConsultation = new JTextField(15);
        formPanel.add(txtMotifConsultation, gbc);

        // ===== OBSERVATION =====
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Observation :"), gbc);

        gbc.gridx = 1;
        txtObservation = new JTextField(15);
        formPanel.add(txtObservation, gbc);

        // ===== DIAGNOSTIC =====
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Diagnostic présumé :"), gbc);

        gbc.gridx = 1;
        txtDiagnostic = new JTextField(15);
        formPanel.add(txtDiagnostic, gbc);

        // ===== RECOMMENDATION =====
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Recommendation :"), gbc);

        gbc.gridx = 1;
        txtRecommendation = new JTextField(15);
        formPanel.add(txtRecommendation, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirm = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnConfirm.addActionListener(e -> {
            if (validateFields()) {
                confirmed = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    private boolean validateFields() {

        if (getTxtIdDoctor().isEmpty() ||
        getTxtIdPatient().isEmpty() ||
        getTxtDate().isEmpty() ||
        getTxtMotifConsultation().isEmpty() ||
        getTxtObservation().isEmpty() ||
        getTxtDiagnostic().isEmpty() ||
        getTxtRecommendation().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs.",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate.parse(txtDate.getText().trim(), dateFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "La date doit être au format JJ-MM-AAAA.",
                    "Erreur format date",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() { return confirmed; }

    public String getTxtIdPatient() {
        return txtIdPatient.getText().trim();
    }

    public String getTxtIdDoctor() {
        return txtIdDoctor.getText().trim();
    }

    public LocalDate getTxtDate() {
        return LocalDate.parse(txtDate.getText().trim(), dateFormatter);
    }

    public String getTxtMotifConsultation() {
        return txtMotifConsultation.getText().trim();
    }

    public String getTxtObservation() {
        return txtObservation.getText().trim();
    }

    public String getTxtDiagnostic() {
        return txtDiagnostic.getText().trim();
    }

    public String getTxtRecommendation() {
        return txtRecommendation.getText().trim();
    }
}

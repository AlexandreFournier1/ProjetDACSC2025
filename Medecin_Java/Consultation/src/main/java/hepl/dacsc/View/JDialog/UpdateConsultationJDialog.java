package hepl.dacsc.View.JDialog;

import hepl.dacsc.model.entity.Consultation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UpdateConsultationJDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtDate;
    private JTextField txtHour;
    private JTextField txtPatientId;
    private JTextField txtDuration;
    private JTextField txtReason;

    private boolean confirmed = false;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public UpdateConsultationJDialog(JFrame parent, Consultation c) {
        super(parent, "Modifier Consultation", true);

        setSize(400, 360);
        setMinimumSize(new Dimension(400, 320));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setContentPane(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- ID Consultation (readonly) ----
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Consultation :"), gbc);

        gbc.gridx = 1;
        txtId = new JTextField(String.valueOf(c.getId()), 20);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Date (JJ-MM-AAAA) :"), gbc);

        gbc.gridx = 1;
        txtDate = new JTextField(c.getDate().format(dateFormatter), 20);
        txtDate.setToolTipText("JJ-MM-AAAA");
        formPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Heure (HH:mm) :"), gbc);

        gbc.gridx = 1;
        txtHour = new JTextField(c.getHour().format(hourFormatter), 20);
        txtHour.setToolTipText("HH:mm");
        formPanel.add(txtHour, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Patient ID :"), gbc);

        gbc.gridx = 1;
        txtPatientId = new JTextField(String.valueOf(c.getPatient_id()), 20);
        formPanel.add(txtPatientId, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Durée (min) :"), gbc);

        gbc.gridx = 1;
        txtDuration = new JTextField(String.valueOf(c.getDuree()), 20);
        txtDuration.setToolTipText("Exemple : 30");
        formPanel.add(txtDuration, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Raison :"), gbc);

        gbc.gridx = 1;
        txtReason = new JTextField(c.getReason() == null ? "" : c.getReason(), 20);
        formPanel.add(txtReason, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ---- BOUTONS ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ---- Actions ----
        btnSave.addActionListener(e -> {
            if (validateFields()) {
                confirmed = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());
    }

    // =================== VALIDATION ===================
    private boolean validateFields() {

        try {
            LocalDate.parse(txtDate.getText().trim(), dateFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Date invalide (JJ-MM-AAAA).",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            LocalTime.parse(txtHour.getText().trim(), hourFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Heure invalide (HH:mm).",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            String txt = txtPatientId.getText().trim();

            if (!txt.isEmpty()) {
                Integer.parseInt(txt); // Vérifie que c’est bien un nombre
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "L'ID patient doit être un nombre ou laissé vide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }


        return true;
    }

    // =================== GETTERS ===================
    public boolean isConfirmed() { return confirmed; }

    public int getId() { return Integer.parseInt(txtId.getText()); }

    public LocalDate getDate() {
        return LocalDate.parse(txtDate.getText().trim(), dateFormatter);
    }

    public LocalTime getHour() {
        return LocalTime.parse(txtHour.getText().trim(), hourFormatter);
    }

    public Integer getPatientId() {
        String txt = txtPatientId.getText().trim();
        return txt.isEmpty() ? null : Integer.parseInt(txt);
    }

    public int getDuration() {
        return Integer.parseInt(txtDuration.getText().trim());
    }

    public String getReason() {
        return txtReason.getText().trim();
    }
}

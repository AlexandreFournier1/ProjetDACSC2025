package hepl.dacsc.view.JDialog;

import hepl.dacsc.model.entity.Rapport;
import hepl.dacsc.view.JTextArea.RapportJTextArea;
import hepl.dacsc.view.error.ErrorMessage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierRapportJDialog extends JDialog {
    private JTextField txtId;
    private JTextField txtIdDoctor;
    private JTextField txtIdPatient;
    private JTextField txtDate;
    private boolean confirmed = false;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ErrorMessage errMsg = new ErrorMessage();
    private JFrame parent;

    public ModifierRapportJDialog(JFrame parent, Rapport rapport) {
        super(parent, "Modifier Rapport", true);
        this.parent = parent;

        setSize(450, 360);
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

        // ---- ID rapport (readonly) ----
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Rapport :"), gbc);

        gbc.gridx = 1;
        txtId = new JTextField(String.valueOf(rapport.getId()), 20);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);

        // ---- ID médecin ----
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID Médecin :"), gbc);

        gbc.gridx = 1;
        txtIdDoctor = new JTextField(String.valueOf(rapport.getIdDoctor()), 20);
        formPanel.add(txtIdDoctor, gbc);

        // ---- ID patient ----
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("ID Patient :"), gbc);

        gbc.gridx = 1;
        txtIdPatient = new JTextField(String.valueOf(rapport.getIdPatient()), 20);
        formPanel.add(txtIdPatient, gbc);

        // ---- Date ----
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Date (JJ-MM-AAAA) :"), gbc);

        gbc.gridx = 1;
        txtDate = new JTextField(rapport.getDate().format(dateFormatter), 20);
        txtDate.setToolTipText("JJ-MM-AAAA");
        formPanel.add(txtDate, gbc);

        // ---- Rapport ----
        // Bouton rapport
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Rapport :"), gbc);

        gbc.gridx = 1;
        JButton showButton = new JButton("Show");

        showButton.addActionListener(e -> {
            RapportJTextArea jTextArea = new RapportJTextArea(parent, rapport, true);
            jTextArea.setVisible(true);

            if (jTextArea.isConfirmed()) {
                System.out.println("Nouveau texte : " + rapport.getTextRapport());
            }
        });
        formPanel.add(showButton, gbc);
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
            errMsg.showErrorMessage(parent, "Date invalide (JJ-MM-AAAA).");
            return false;
        }

        try {
            String txt = txtIdPatient.getText().trim();
            if (!txt.isEmpty()) {
                Integer.parseInt(txt);
            }
        } catch (NumberFormatException e) {
            errMsg.showErrorMessage(parent, "L'ID patient doit être un nombre ou laissé vide.");
            return false;
        }

        try {
            String txt = txtIdDoctor.getText().trim();
            if (!txt.isEmpty()) {
                Integer.parseInt(txt);
            }
        } catch (NumberFormatException e) {
            errMsg.showErrorMessage(parent, "L'ID médecin doit être un nombre ou laissé vide.");
            return false;
        }

        return true;
    }

    public boolean isConfirmed() { return confirmed; }
    public Integer getDoctorId() { return Integer.parseInt(txtIdDoctor.getText()); }
    public Integer getPatientId() { return Integer.parseInt(txtIdPatient.getText()); }
    public LocalDate getDate() {
        return LocalDate.parse(txtDate.getText().trim(), dateFormatter);
    }
}

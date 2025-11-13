package View.JDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddConsultationJDialog extends JDialog {

    private JTextField txtDate;
    private JTextField txtHour;
    private JTextField txtDuration;
    private JTextField txtCount;

    private boolean confirmed = false;
    private boolean exceeds17h = false;
    private int overflowMinutes = 0;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AddConsultationJDialog(JFrame parent) {
        super(parent, "Ajouter Consultation", true);

        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setContentPane(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // DATE
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Date :"), gbc);

        gbc.gridx = 1;
        txtDate = new JTextField(15);
        txtDate.setToolTipText("JJ-MM-AAAA");
        formPanel.add(txtDate, gbc);

        // HEURE
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Heure :"), gbc);

        gbc.gridx = 1;
        txtHour = new JTextField(15);
        txtHour.setToolTipText("HH:mm");
        formPanel.add(txtHour, gbc);

        // DURÉE
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Durée (min) :"), gbc);

        gbc.gridx = 1;
        txtDuration = new JTextField(15);
        txtDuration.setToolTipText("ex : 15");
        formPanel.add(txtDuration, gbc);

        // NOMBRE CONSÉCUTIVES
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Nb consultations :"), gbc);

        gbc.gridx = 1;
        txtCount = new JTextField(15);
        txtCount.setToolTipText("ex : 3");
        formPanel.add(txtCount, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirm = new JButton("Créer");
        JButton btnCancel = new JButton("Annuler");

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnConfirm.addActionListener(e -> {
            if (validateFields()) {

                computeIfExceeds17h();

                if (exceeds17h) {
                    JOptionPane.showMessageDialog(this,
                            "ATTENTION : la dernière consultation dépasse 17h00 de "
                                    + overflowMinutes + " minutes.\n" +
                                    "Veuillez modifier l'heure, la durée ou le nombre.",
                            "Dépassement horaire",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                confirmed = true;
                dispose();
            }
        });


        btnCancel.addActionListener(e -> dispose());
    }

    private boolean validateFields() {

        if (txtDate.getText().trim().isEmpty()
                || txtHour.getText().trim().isEmpty()
                || txtDuration.getText().trim().isEmpty()
                || txtCount.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate.parse(txtDate.getText().trim(), dateFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Date invalide (JJ-MM-AAAA)",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            LocalTime.parse(txtHour.getText().trim(), hourFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Heure invalide (HH:mm)",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(txtDuration.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Durée invalide",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(txtCount.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Nombre consultations invalide",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void computeIfExceeds17h() {
        LocalTime start = getHour();
        int duration = getDuration();
        int count = getCount();

        LocalTime finalTime = start.plusMinutes((long) duration * count);

        LocalTime limit = LocalTime.of(17, 0);

        if (finalTime.isAfter(limit)) {
            exceeds17h = true;
            overflowMinutes = (int) java.time.Duration.between(limit, finalTime).toMinutes();
        } else {
            exceeds17h = false;
            overflowMinutes = 0;
        }
    }
    public boolean isConfirmed() { return confirmed; }

    public boolean exceeds17h() { return exceeds17h; }

    public LocalDate getDate() {
        return LocalDate.parse(txtDate.getText().trim(), dateFormatter);
    }

    public LocalTime getHour() {
        return LocalTime.parse(txtHour.getText().trim(), hourFormatter);
    }

    public int getDuration() {
        return Integer.parseInt(txtDuration.getText().trim());
    }

    public int getCount() {
        return Integer.parseInt(txtCount.getText().trim());
    }
}

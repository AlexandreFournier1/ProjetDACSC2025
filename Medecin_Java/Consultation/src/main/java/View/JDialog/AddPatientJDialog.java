package View.JDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddPatientJDialog extends JDialog {

    private JTextField txtLastName;
    private JTextField txtFirstName;
    private JTextField txtBirthDate;

    private boolean confirmed = false;

    // Formatteur JJ-MM-AAAA
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public AddPatientJDialog(JFrame parent) {
        super(parent, "Ajouter un patient", true);

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

        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nom :"), gbc);

        gbc.gridx = 1;
        txtLastName = new JTextField(15);
        formPanel.add(txtLastName, gbc);

        // Prénom
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Prénom :"), gbc);

        gbc.gridx = 1;
        txtFirstName = new JTextField(15);
        formPanel.add(txtFirstName, gbc);

        // Date naissance
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Date de naissance :"), gbc);

        gbc.gridx = 1;
        txtBirthDate = new JTextField(15);
        txtBirthDate.setToolTipText("Format : JJ-MM-AAAA");
        formPanel.add(txtBirthDate, gbc);

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

        if (txtLastName.getText().trim().isEmpty() ||
                txtFirstName.getText().trim().isEmpty() ||
                txtBirthDate.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs.",
                    "Erreur",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            LocalDate.parse(txtBirthDate.getText().trim(), dateFormatter);
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

    public String getLastName() { return txtLastName.getText().trim(); }
    public String getFirstName() { return txtFirstName.getText().trim(); }

    public LocalDate getBirthDate() {
        return LocalDate.parse(txtBirthDate.getText().trim(), dateFormatter);
    }
}

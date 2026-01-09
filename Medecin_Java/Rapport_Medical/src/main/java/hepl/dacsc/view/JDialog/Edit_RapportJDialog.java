package hepl.dacsc.view.JDialog;

import javax.swing.*;
import java.awt.*;

public class Edit_RapportJDialog extends JDialog {

    private final int reportId;
    private JTextArea txtRapport;
    private boolean confirmed = false;

    public Edit_RapportJDialog(JFrame parent, int reportId, String initialText) {
        super(parent, "Modifier rapport #" + reportId, true);
        this.reportId = reportId;
        initComponents(initialText);
        setLocationRelativeTo(parent);
    }

    private void initComponents(String initialText) {
        setSize(550, 450);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("ID Rapport : " + reportId));
        add(top, BorderLayout.NORTH);

        txtRapport = new JTextArea(12, 35);
        txtRapport.setLineWrap(true);
        txtRapport.setWrapStyleWord(true);
        txtRapport.setText(initialText == null ? "" : initialText);

        add(new JScrollPane(txtRapport), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("Valider");
        JButton btnCancel = new JButton("Annuler");
        buttons.add(btnOk);
        buttons.add(btnCancel);
        add(buttons, BorderLayout.SOUTH);

        btnOk.addActionListener(e -> {
            if (txtRapport.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le texte du rapport ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public int getReportId() {
        return reportId;
    }

    public String getNewText() {
        return txtRapport.getText();
    }
}

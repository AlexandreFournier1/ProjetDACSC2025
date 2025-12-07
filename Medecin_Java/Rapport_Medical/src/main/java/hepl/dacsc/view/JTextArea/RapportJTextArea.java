package hepl.dacsc.view.JTextArea;

import hepl.dacsc.model.entity.Rapport;

import javax.swing.*;
import java.awt.*;

public class RapportJTextArea extends JDialog {

    private boolean confirmed = false;

    public RapportJTextArea(JFrame parent, Rapport rapport, boolean isEditable) {
        super(parent, "Rapport médical", true);

        JTextArea area = new JTextArea(rapport.getTextRapport());
        area.setEditable(isEditable);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(area);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        if (isEditable) {
            JButton btnValider = new JButton("Valider");
            JButton btnAnnuler = new JButton("Annuler");

            btnValider.addActionListener(e -> {
                rapport.setTextRapport(area.getText());
                confirmed = true;
                dispose();
            });

            btnAnnuler.addActionListener(e -> dispose());

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(btnAnnuler);
            bottomPanel.add(btnValider);

            getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        }

        setSize(500, 550);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
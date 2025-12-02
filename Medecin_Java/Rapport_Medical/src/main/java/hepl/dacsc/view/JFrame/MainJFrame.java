package hepl.dacsc.view.JFrame;

import hepl.dacsc.view.JDialog.EncoderRapportJDialog;
import hepl.dacsc.view.JDialog.LoginJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;

public class MainJFrame extends JFrame {
    private JPanel workArea;
    private boolean authenticated = false;
    private HashMap<Integer, String> txtRapport = new HashMap<>();
    public MainJFrame() {
        super("Client Rapport Medical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JButton login = new JButton("Login");
        JButton logout = new JButton("Logout");
        logout.setVisible(false);

        login.addActionListener(e -> showLoginDialog(login, logout));
        logout.addActionListener(e -> logout(login, logout));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(login);
        topPanel.add(logout);
        add(topPanel, BorderLayout.NORTH);

        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }

    public void showLoginDialog(JButton login, JButton logout) {
        LoginJDialog dialog = new LoginJDialog(this);
        dialog.setVisible(true);

        if (dialog.isAuthenticated()) {
            authenticated = true;
            login.setVisible(false);
            logout.setVisible(true);
            showWorkPanel();
        }
    }

    public void logout(JButton login, JButton logout) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            authenticated = false;
            login.setVisible(true);
            logout.setVisible(false);
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
        }
    }

    public void showWorkPanel() {
        workArea.removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== LIGNE 1 =====
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JButton btnEncoderRapport = new JButton("Encoder Rapport");
        JButton btnAfficherRapport = new JButton("Afficher Rapport");
        JButton btnCacherRapport = new JButton("Cacher Rapport");
        JButton btnModifierRapport = new JButton("Modifier Rapport");
        JButton btnAfficherTexteRapport = new JButton("Afficher Texte Rapport");
        topButtons.add(btnEncoderRapport);
        topButtons.add(btnAfficherRapport);
        topButtons.add(btnCacherRapport);
        topButtons.add(btnModifierRapport);
        topButtons.add(btnAfficherTexteRapport);

        // ===== LIGNE 2 =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel labelIdPatient = new JLabel("id patient:");
        JTextField txtIdPatient = new JTextField(10);
        JButton btnRechercher = new JButton("Rechercher");

        searchPanel.add(labelIdPatient);
        searchPanel.add(txtIdPatient);
        searchPanel.add(btnRechercher);

        // ===== TABLEAU =====
        String[] columnNames = {"ID", "Médecin ID", "Patient ID", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // ===== PANELS FIXES EN HAUT =====
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.add(topButtons);
        topSection.add(Box.createVerticalStrut(5));
        topSection.add(searchPanel);

        // ===== ASSEMBLAGE FINAL =====
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        workArea.add(mainPanel, BorderLayout.CENTER);

        workArea.revalidate();
        workArea.repaint();

        btnRechercher.addActionListener(e -> {
            String idPatient = txtIdPatient.getText().trim();

            tableModel.setRowCount(0);

            // Ajout fictif des lignes
            tableModel.addRow(new Object[]{1, 1, idPatient, "2025-02-01"});
            setTxtRapport(1, assembleRapport(
                    "Douleur au poignet droit",
                    "Le patient décrit une douleur modérée apparue après une séance de sport. Pas de gonflement significatif, mobilité conservée mais inconfort à la flexion.",
                    "Tendinite légère.",
                    "Repos du poignet 5 à 7 jours, application de glace, prise d’un anti-inflammatoire léger si nécessaire. Revoir si la douleur persiste plus de 10 jours."
            ));

            tableModel.addRow(new Object[]{2, 1, idPatient, "2025-02-01"});
            setTxtRapport(2, assembleRapport(
                    "Fatigue persistante depuis 2 semaines",
                    "La patiente présente une fatigue générale mais sans fièvre ni autres symptômes. Examen clinique normal.",
                    "Fatigue fonctionnelle liée au stress et au rythme de travail.",
                    "Repos de 48h, hydratation, rééquilibrage du sommeil. Bilan sanguin prescrit par précaution (résultats attendus dans 48h)."
            ));

            tableModel.addRow(new Object[]{3, 1, idPatient, "2025-02-01"});
            setTxtRapport(3, assembleRapport(
                    "Toux sèche nocturne depuis 5 jours",
                    "Toux non productive, auscultation pulmonaire claire, pas de signes d’infection sévère.",
                    "Irritation des voies respiratoires supérieures, probablement virale.",
                    "Humidifier la chambre la nuit, boisson tiède avant le coucher. Surveillance par les parents si apparition de fièvre ou aggravation."
            ));
        });

        btnEncoderRapport.addActionListener(e ->{
            EncoderRapportJDialog rapportJDialog = new EncoderRapportJDialog(this);
            rapportJDialog.setVisible(true);

            if (rapportJDialog.isConfirmed()){


                System.out.println(lastname + " " + firstname);
            }
        });
    }

    public String assembleRapport(String motifConsultation, String observation, String diagnostic, String recommendation) {
        return "Motif de consultation : " + motifConsultation + "\n" + "Observation : " + observation + "\n" + "Diagnostic présumé: " + diagnostic + "\n" + "Recommendation : " + recommendation + "\n";
    }

    public HashMap<Integer, String> getTxtRapport() {
        return txtRapport;
    }

    public void setTxtRapport(Integer idRapport, String txtRapport) {
        this.txtRapport.put(idRapport, txtRapport);
    }
}

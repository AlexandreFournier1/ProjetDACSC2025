package hepl.dacsc.view.JFrame;

import hepl.dacsc.model.entity.Rapport;
import hepl.dacsc.view.JDialog.EncoderRapportJDialog;
import hepl.dacsc.view.JDialog.LoginJDialog;
import hepl.dacsc.view.JDialog.ModifierRapportJDialog;
import hepl.dacsc.view.JTextArea.RapportJTextArea;
import hepl.dacsc.view.error.ErrorMessage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainJFrame extends JFrame {
    private JPanel workArea;
    private boolean authenticated = false;
    private ArrayList<Rapport> listRapport = new ArrayList<>();
    private ErrorMessage errMsg = new ErrorMessage();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JFrame currentFrame = this;
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

            if (idPatient.isEmpty()) {
                errMsg.showWarningMessage(this, "Veuillez saisir un id de patient", "Aucun filtre");
                return;
            }

            // Recherche BDD + affichage
        });

        btnEncoderRapport.addActionListener(e ->{
            EncoderRapportJDialog rapportJDialog = new EncoderRapportJDialog(this);
            rapportJDialog.setVisible(true);

            if (rapportJDialog.isConfirmed()){
                String idPatient = rapportJDialog.getTxtIdPatient();
                String idDoctor = rapportJDialog.getTxtIdDoctor();
                String date = String.valueOf(rapportJDialog.getTxtDate());
                String motif = rapportJDialog.getTxtMotifConsultation();
                String observation = rapportJDialog.getTxtObservation();
                String diagnostic = rapportJDialog.getTxtDiagnostic();
                String recommendation = rapportJDialog.getTxtRecommendation();

                String rapport = assembleRapport(motif, observation, diagnostic, recommendation);

                System.out.println("Rapport du médecin : " + idDoctor + "\n" + "Concernant le patient : " + idPatient + "\n" + date + "\n" + rapport);
            }
        });

        btnAfficherRapport.addActionListener(e -> {
            tableModel.setRowCount(0);

            // Ajout fictif des lignes
            tableModel.addRow(new Object[]{1, 1, 1, "01-02-2025"});
            String txtRapport1 = assembleRapport(
                    "Douleur au poignet droit",
                    "Le patient décrit une douleur modérée apparue après une séance de sport. Pas de gonflement significatif, mobilité conservée mais inconfort à la flexion.",
                    "Tendinite légère.",
                    "Repos du poignet 5 à 7 jours, application de glace, prise d’un anti-inflammatoire léger si nécessaire. Revoir si la douleur persiste plus de 10 jours."
            );
            LocalDate date = LocalDate.parse("01-02-2025", dateFormatter);
            Rapport rap1 = new Rapport(1, 1, 1, date, txtRapport1);

            tableModel.addRow(new Object[]{2, 1, 2, "01-02-2025"});
            String txtRapport2 = assembleRapport(
                    "Fatigue persistante depuis 2 semaines",
                    "La patiente présente une fatigue générale mais sans fièvre ni autres symptômes. Examen clinique normal.",
                    "Fatigue fonctionnelle liée au stress et au rythme de travail.",
                    "Repos de 48h, hydratation, rééquilibrage du sommeil. Bilan sanguin prescrit par précaution (résultats attendus dans 48h)."
            );
            Rapport rap2 = new Rapport(2, 1, 2, date, txtRapport2);

            tableModel.addRow(new Object[]{3, 1, 3, "01-02-2025"});
            String txtRapport3 = assembleRapport(
                    "Toux sèche nocturne depuis 5 jours",
                    "Toux non productive, auscultation pulmonaire claire, pas de signes d’infection sévère.",
                    "Irritation des voies respiratoires supérieures, probablement virale.",
                    "Humidifier la chambre la nuit, boisson tiède avant le coucher. Surveillance par les parents si apparition de fièvre ou aggravation."
            );
            Rapport rap3 = new Rapport(3, 1, 3, date, txtRapport3);

            listRapport.add(rap1);
            listRapport.add(rap2);
            listRapport.add(rap3);
        });

        btnCacherRapport.addActionListener(e -> {
            tableModel.setRowCount(0);
        });

        btnAfficherTexteRapport.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                errMsg.showWarningMessage(this, "Veuillez sélectionner une consultation dans le tableau.", "Aucune sélection");
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);

            for (Rapport rapport : listRapport) {
                if (rapport.getId() == id) {
                    RapportJTextArea rapportJTextArea = new RapportJTextArea(this, rapport, false);
                    rapportJTextArea.setVisible(true);
                }
            }
        });

        btnModifierRapport.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                errMsg.showWarningMessage(this, "Veuillez sélectionner une consultation dans le tableau.", "Aucune sélection");
                return;
            }

            // Récupération des données du tableau
            int id = (int) tableModel.getValueAt(row, 0);
            int docId = (int) tableModel.getValueAt(row, 1);
            int patientId = (int) tableModel.getValueAt(row, 2);
            LocalDate date = LocalDate.parse(tableModel.getValueAt(row, 3).toString(), dateFormatter);

            String txtRapport = "";

            for (Rapport rapport : listRapport) {
                if (rapport.getId() == id) {
                    txtRapport = rapport.getTextRapport();
                }
            }

            Rapport rapport = new Rapport(id, docId, patientId, date, txtRapport);

            ModifierRapportJDialog dialog = new ModifierRapportJDialog(this, rapport);
            dialog.setVisible(true);

            // Si confirmé, mise à jour du tableau
            if (dialog.isConfirmed()) {
                tableModel.setValueAt(dialog.getDoctorId().toString(), row, 1);
                tableModel.setValueAt(dialog.getPatientId().toString(), row, 2);
                tableModel.setValueAt(dialog.getDate(), row, 3);

                System.out.println("Rapport " + id + " mise à jour !");
            }
        });
    }

    public String assembleRapport(String motifConsultation, String observation, String diagnostic, String recommendation) {
        return "Motif de consultation : " + motifConsultation + "\n\n" + "Observation : " + observation + "\n\n" + "Diagnostic présumé: " + diagnostic + "\n\n" + "Recommendation : " + recommendation + "\n";
    }
}

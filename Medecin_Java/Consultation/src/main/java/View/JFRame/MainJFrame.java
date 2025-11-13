package View.JFRame;

import View.JDialog.AddConsultationJDialog;
import View.JDialog.AddPatientJDialog;
import View.JDialog.LoginJDialog;
import View.JDialog.UpdateConsultationJDialog;
import model.entity.Consultation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class MainJFrame extends JFrame {
    private JPanel workArea;
    private boolean authenticated = false;

    public MainJFrame() {
        super("Client Consultation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JButton btnLogin = new JButton("Login");
        JButton btnLogout = new JButton("Logout");
        btnLogout.setVisible(false);

        btnLogin.addActionListener(e -> showLoginDialog(btnLogin, btnLogout));
        btnLogout.addActionListener(e -> logout(btnLogin, btnLogout));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnLogin);
        topPanel.add(btnLogout);
        add(topPanel, BorderLayout.NORTH);

        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }

    private void showLoginDialog(JButton btnLogin, JButton btnLogout) {
        LoginJDialog loginDialog = new LoginJDialog(this);
        loginDialog.setVisible(true);

        if (loginDialog.isAuthenticated()) {
            authenticated = true;
            btnLogin.setVisible(false);
            btnLogout.setVisible(true);
            showWorkPanel();
        }
    }

    private void logout(JButton btnLogin, JButton btnLogout) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            authenticated = false;
            btnLogin.setVisible(true);
            btnLogout.setVisible(false);
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
        }
    }

    private void showWorkPanel() {
        workArea.removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ligne 1
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JButton btnAddPatient = new JButton("add patient");
        JButton btnAddConsultation = new JButton("add consultation");
        JButton btnUpdateConsultation = new JButton("update consultation");
        topButtons.add(btnAddPatient);
        topButtons.add(btnAddConsultation);
        topButtons.add(btnUpdateConsultation);

        // ligne 2
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel lblIdPatient = new JLabel("id patient:");
        JTextField txtIdPatient = new JTextField(10);
        JLabel lblDate = new JLabel("date:");
        JTextField txtDate = new JTextField(10);
        JButton btnSearch = new JButton("search consultation");

        searchPanel.add(lblIdPatient);
        searchPanel.add(txtIdPatient);
        searchPanel.add(lblDate);
        searchPanel.add(txtDate);
        searchPanel.add(btnSearch);

        //tableau
        String[] columnNames = {"ID", "Doc ID", "Patient ID", "Date", "Heure", "Durée", "Raison"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        // PANELS FIXES EN HAUT
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.add(topButtons);
        topSection.add(Box.createVerticalStrut(5));
        topSection.add(searchPanel);

        // ASSEMBLAGE FINA
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        workArea.add(mainPanel, BorderLayout.CENTER);

        workArea.revalidate();
        workArea.repaint();

        btnSearch.addActionListener(e -> {
            String idPatient = txtIdPatient.getText().trim();
            String dateText = txtDate.getText().trim();

            tableModel.setRowCount(0);

            // Vérification des champs

            // Ajoute les lignes avec durée
            tableModel.addRow(new Object[]{1, 1, idPatient, "2025-02-01", "09:00", 30, "Contrôle annuel du patient."});
            tableModel.addRow(new Object[]{2, 1, idPatient, "2025-02-01", "09:30", 30, "Vaccination antigrippale et vérification tension artérielle."});
            tableModel.addRow(new Object[]{3, 1, idPatient, "2025-02-01", "10:00", 30, "Suivi post-opératoire et renouvellement d’ordonnance."});
        });


        btnAddPatient.addActionListener(e ->{
            AddPatientJDialog addPatientJDialog = new AddPatientJDialog(this);
            addPatientJDialog.setVisible(true);

            if (addPatientJDialog.isConfirmed()){
                String lastname = addPatientJDialog.getLastName();
                String firstname = addPatientJDialog.getFirstName();
                LocalDate birthDate = addPatientJDialog.getBirthDate();

                System.out.println(lastname + " " + firstname + " " + birthDate);

                // Ajout DB
            }
        });

        btnAddConsultation.addActionListener(e -> {
            AddConsultationJDialog dialog = new AddConsultationJDialog(this);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {

                LocalDate date = dialog.getDate();
                LocalTime hour = dialog.getHour();
                int duration = dialog.getDuration();
                int count = dialog.getCount();

                System.out.println("Création " + count + " consultations à partir de " +
                        date + " " + hour + ", durée=" + duration + " min");

            }
        });

        btnUpdateConsultation.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner une consultation dans le tableau.",
                        "Aucune sélection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Récupération des données du tableau
            int id = (int) tableModel.getValueAt(row, 0);
            int docId = (int) tableModel.getValueAt(row, 1);
            Object patientObj = tableModel.getValueAt(row, 2);
            Integer patientId = null;

            if (patientObj != null && !patientObj.toString().isEmpty()) {
                patientId = Integer.parseInt(patientObj.toString());
            }
            LocalDate date = LocalDate.parse(tableModel.getValueAt(row, 3).toString());
            LocalTime hour = LocalTime.parse(tableModel.getValueAt(row, 4).toString());
            int duree = (int) tableModel.getValueAt(row, 5);
            String reason = tableModel.getValueAt(row, 6).toString();

            // Création de l'objet Consultation
            Consultation c = new Consultation(id, docId, patientId, date, hour, duree,reason);

            // Ouverture du JDialog
            UpdateConsultationJDialog dialog = new UpdateConsultationJDialog(this, c);
            dialog.setVisible(true);

            // Si confirmé, mise à jour du tableau
            if (dialog.isConfirmed()) {

                tableModel.setValueAt(dialog.getDate().toString(), row, 3);
                tableModel.setValueAt(dialog.getHour().toString(), row, 4);
                Integer pid = dialog.getPatientId();
                tableModel.setValueAt(pid == null ? "" : pid, row, 2);
                tableModel.setValueAt(dialog.getDuration(), row, 5);
                tableModel.setValueAt(dialog.getReason(), row, 6);

                System.out.println("Consultation " + id + " mise à jour !");
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainJFrame().setVisible(true));
    }
}
package View.JFRame;

import View.JDialog.LoginJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


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

        // ======= ZONE DE TRAVAIL =======
        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }

    private void showLoginDialog(JButton btnLogin, JButton btnLogout) {
        LoginJDialog loginDialog = new LoginJDialog(this);
        loginDialog.setVisible(true);

        if (loginDialog.isAuthenticated()) {
            authenticated = true;
            btnLogin.setEnabled(false);
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
            btnLogin.setEnabled(true);
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
        String[] columnNames = {"doc id", "patient id", "date", "heure", "reason"};
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
            String date = txtDate.getText().trim();

            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"D001", idPatient, date, "09:00", "Contrôle annuel du patient."});
            tableModel.addRow(new Object[]{"D001", idPatient, date, "09:30", "Vaccination antigrippale et vérification tension artérielle."});
            tableModel.addRow(new Object[]{"D001", idPatient, date, "10:00", "Suivi post-opératoire et renouvellement d’ordonnance du patient."});

        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainJFrame().setVisible(true));
    }
}
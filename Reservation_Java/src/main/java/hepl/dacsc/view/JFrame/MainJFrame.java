package hepl.dacsc.view.JFrame;

import hepl.dacsc.model.RequestAdmin;
import hepl.dacsc.model.Admin;
import hepl.dacsc.model.Patient;
import hepl.dacsc.view.JDialog.LoginJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainJFrame extends JFrame {
    private JPanel workArea;
    private boolean authenticated = false;

    private Admin admin = null;
    private RequestAdmin request = new RequestAdmin();

    public MainJFrame() {
        super("Client Consultation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JButton btnLogin = new JButton("Login");
        JButton btnLogout = new JButton("Logout");
        btnLogout.setVisible(false);

        btnLogin.addActionListener(e -> {
            try {
                showLoginDialog(btnLogin, btnLogout);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnLogout.addActionListener(e -> {
            try {
                logout(btnLogin, btnLogout);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnLogin);
        topPanel.add(btnLogout);
        add(topPanel, BorderLayout.NORTH);

        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }

    private void showLoginDialog(JButton btnLogin, JButton btnLogout) throws IOException {
        LoginJDialog loginDialog = new LoginJDialog(this);
        loginDialog.setVisible(true);

        if (loginDialog.isAuthenticated()) {
            admin = new Admin();
            admin.Connexion();

            authenticated = true;
            btnLogin.setVisible(false);
            btnLogout.setVisible(true);
            showWorkPanel();
        }
    }

    private void logout(JButton btnLogin, JButton btnLogout) throws IOException {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            admin.Disconnect();
            admin = null;

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

        JButton btnClearTable = new JButton("clear table");
        JButton btnShowPatients = new JButton("show patients");
        topButtons.add(btnClearTable);
        topButtons.add(btnShowPatients);

        // tableau
        String[] columnNames = {"ID", "Nom", "Prénom", "IP"};
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

        // ASSEMBLAGE FIN
        mainPanel.add(topSection, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        workArea.add(mainPanel, BorderLayout.CENTER);

        workArea.revalidate();
        workArea.repaint();

        btnClearTable.addActionListener(e -> {
            tableModel.setRowCount(0);
        });

        btnShowPatients.addActionListener(e -> {
            try {
                ArrayList<Patient> patients = request.GetLoggedPatient(admin);

                for (Patient patient : patients) {
                    tableModel.addRow(new Object[]{patient.getId(), patient.getNom(), patient.getPrenom(), patient.getIp()});
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}

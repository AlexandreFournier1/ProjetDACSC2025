package hepl.dacsc;

import hepl.dacsc.view.JDialog.LoginJDialog;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class ClientMRPS extends JFrame {

    private Socket socket = null;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private JPanel workArea;

    private DefaultTableModel tableModel;

    private SecretKey cleSession;

    public ClientMRPS() {
        initComponents();

        oos = null;
        ois = null;
    }

    private void initComponents() {
        setName("Client Rapport Médical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JButton btnLogin = new JButton("Login");
        JButton btnLogout = new JButton("Logout");
        btnLogout.setVisible(false);

        btnLogin.addActionListener(e -> jButtonLoginActionPerformed(e, btnLogin, btnLogout));
        btnLogout.addActionListener(e -> jButtonLogoutActionPerformed(e, btnLogin, btnLogout));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(btnLogin);
        topPanel.add(btnLogout);
        add(topPanel, BorderLayout.NORTH);

        workArea = new JPanel(new BorderLayout());
        workArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(workArea, BorderLayout.CENTER);
    }
    private void showWorkPanel() {
        workArea.removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topButtons = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Encoder Rapport");
        JButton btnEdit = new JButton("Modifier Rapport");
        JButton btnList = new JButton("Lister Rapports");
        JButton btnShow = new JButton("Afficher Texte");

        topButtons.add(btnAdd);
        topButtons.add(btnEdit);
        topButtons.add(btnList);
        topButtons.add(btnShow);

        String[] cols = {"ID", "Médecin", "Patient", "Date"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);

        mainPanel.add(topButtons, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        workArea.add(mainPanel);
        workArea.revalidate();
        workArea.repaint();

        btnAdd.addActionListener(e -> {
            try {
                jButtonADD_REPORT(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnList.addActionListener(e -> {
            try{
                jButtonLIST_REPORTS(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnEdit.addActionListener(e -> {
            try {
                jButtonEDIT_REPORT(e, table);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        btnShow.addActionListener(e -> {
            try {
                jButtonSHOW_REPORT_TEXT(table);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


    }
    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {
        LoginJDialog login = new LoginJDialog(this);
        login.setVisible(true);
    }

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {

    }

    private void jButtonADD_REPORT(ActionEvent evt) {

    }

    private void jButtonEDIT_REPORT(ActionEvent evt, JTable table) {

    }

    private void jButtonLIST_REPORTS(java.awt.event.ActionEvent evt) {

    }

    private void jButtonSHOW_REPORT_TEXT(JTable evt) {

    }
}

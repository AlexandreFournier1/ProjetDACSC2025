package hepl.dacsc.view.JFrame;

import hepl.dacsc.view.JDialog.LoginJDialog;

import javax.swing.*;
import java.awt.*;

public class MainJFrame extends JFrame {
    private JPanel workArea;
    private boolean authenticated = false;
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

    }
}

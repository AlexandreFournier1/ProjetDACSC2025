package hepl.dacsc.view.error;

import javax.swing.*;

public class ErrorMessage {
    public void showMessage(JFrame parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWarningMessage(JFrame parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    public void showErrorMessage(JFrame parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

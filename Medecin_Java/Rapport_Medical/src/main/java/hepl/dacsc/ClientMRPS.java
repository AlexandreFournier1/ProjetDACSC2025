package hepl.dacsc;

import hepl.dacsc.lib.requete.RequeteExemple;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;

public class ClientMRPS extends JFrame {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private SecretKey cleSession;

    public ClientMRPS() {
        initComponents();

        oos = null;
        ois = null;
    }

    private void initComponents() {

    }

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {

    }

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {

    }

    private void jButtonADD_REPORT(java.awt.event.ActionEvent evt) {

    }

    private void jButtonEDIT_REPORT(java.awt.event.ActionEvent evt) {

    }

    private void jButtonLIST_REPORTS(java.awt.event.ActionEvent evt) {

    }


    private void showMessage(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarningMessage(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    private void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

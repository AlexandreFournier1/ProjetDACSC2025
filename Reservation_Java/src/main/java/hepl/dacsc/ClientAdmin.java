package hepl.dacsc;

import javax.swing.*;
import java.awt.*;
import java.net.*;

public class ClientAdmin extends JFrame {
    public ClientAdmin() {
        setTitle("Client Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,500);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new FlowLayout());

        JButton patientsButton = new JButton();
        patientsButton.setText("Show Patients");
        patientsButton.setHorizontalAlignment(SwingConstants.LEFT);
        //patientsButton.setHorizontalTextPosition(SwingConstants.LEFT);

        panel.add(patientsButton);
    }
}

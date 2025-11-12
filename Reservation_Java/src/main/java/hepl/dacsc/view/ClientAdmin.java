package hepl.dacsc.view;

import javax.swing.*;
import java.awt.*;

public class ClientAdmin extends JFrame {
    public ClientAdmin() {
        super("Client Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new FlowLayout());

        JButton patientsButton = new JButton();
        patientsButton.setText("Récupérer Patients");

        panel.add(patientsButton);
    }
}

package hepl.dacsc.view;

import javax.swing.*;
import java.awt.*;

public class PageConnexion extends JFrame {
    public PageConnexion() {
        super("Connexion Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,300);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        setContentPane(panel);

        JButton button = new JButton("Connexion");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(e -> {
                setVisible(false);
                ClientAdmin admin = new ClientAdmin();
                admin.setVisible(true);
            }
        );

        panel.add(button);
    }
}

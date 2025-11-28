package hepl.dacsc;

import hepl.dacsc.View.JDialog.AddConsultationJDialog;
import hepl.dacsc.View.JDialog.AddPatientJDialog;
import hepl.dacsc.View.JDialog.LoginJDialog;
import hepl.dacsc.View.JDialog.UpdateConsultationJDialog;
import hepl.dacsc.lib.reponse.*;
import hepl.dacsc.lib.requete.*;
import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.entity.Doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ClientCAP extends JFrame {
    private Socket socket = null;
    private String login;
    private JTextField txtIdPatient;
    private JTextField txtDate;


    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    private Doctor doctorConnected;
    private JPanel workArea;
    private int nbConsultations;
    private boolean authenticated = false;

    private DefaultTableModel tableModel;

    public ClientCAP() {
        initComponents();

        oos = null;
        ois = null;
    }

    private void initComponents() {
        setName("Client Consultation");
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

        JLabel lblIdPatient = new JLabel("id patient: (0 = tout)");
        txtIdPatient = new JTextField(10);
        JLabel lblDate = new JLabel("date: (0 = tout)");
        txtDate = new JTextField(10);
        JButton btnSearch = new JButton("search consultation");
        JButton btnDelete = new JButton("delete consultation");


        searchPanel.add(lblIdPatient);
        searchPanel.add(txtIdPatient);
        searchPanel.add(lblDate);
        searchPanel.add(txtDate);
        searchPanel.add(btnSearch);
        searchPanel.add(btnDelete);

        //tableau
        String[] columnNames = {"ID", "Doc ID", "Patient ID", "Date", "Heure", "Durée", "Raison"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        //showConsultation();

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
            try {
                jButtonSearchConsultationActionPerformed(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                jButtonDeleteConsultation(e, table, tableModel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        btnAddPatient.addActionListener(e -> {
            try {
                jButtonAddPatientActionPerformed(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnAddConsultation.addActionListener(e -> {
            try {
                jButtonAddConsultationActionPerformed(e, table, tableModel);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnUpdateConsultation.addActionListener(e -> {
            try {
                jButtonUpdateConsultationActionPerformed(e, table, tableModel);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void showConsultation() {
        try {
            RequeteGET_CONSULTATION req = new RequeteGET_CONSULTATION(doctorConnected.getId());
            oos.writeObject(req);
            oos.flush();

            ReponseGET_CONSULTATION rep = (ReponseGET_CONSULTATION) ois.readObject();

            System.out.println("Dans ShowConsultation :");
            for (Consultation c : rep.getConsultations()) {
                System.out.println("Id : " + c.getId());
            }

            tableModel.setRowCount(0);
            for (Consultation c : rep.getConsultations()) {
                System.out.println(c.getId());
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getDoctor_id(),
                        c.getPatient_id(),
                        c.getDate(),
                        c.getHour(),
                        c.getDuree(),
                        c.getReason()
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {
        LoginJDialog loginJDialog = new LoginJDialog(this);
        loginJDialog.setVisible(true);

        if(loginJDialog.isConfirmed()) {
            try {
                Integer id = Integer.valueOf(loginJDialog.getId());
                String last_name = loginJDialog.getLastName();
                String first_name = loginJDialog.getFirstName();
                String mdp = loginJDialog.getMdp();

                doctorConnected = new Doctor();

                doctorConnected.setId(id);
                doctorConnected.setLast_name(last_name);
                doctorConnected.setFirst_name(first_name);
                doctorConnected.setMdp(mdp);

                //Ip PC Noah
                socket = new Socket("10.236.71.53", 50000);
                //Ip PC Alex
                //socket = new Socket("192.168.56.1", 50000);
                RequeteLOGIN requete = new RequeteLOGIN(id, last_name, first_name, mdp);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                oos.writeObject(requete);
                oos.flush();
                ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();

                System.out.println("test valid : " + reponse.isValid());
                if(reponse.isValid()){
                    System.out.println("test valid : " + reponse.isValid());
                    btnLogin.setVisible(false);
                    btnLogout.setVisible(true);
                    showWorkPanel();
                    this.login = String.valueOf(id);

                    addToTable(0, null);
                }
                else {
                    showErrorMessage("Erreur de connexion");
                    socket.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt, JButton btnLogin, JButton btnLogout) {
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
    private void jButtonAddPatientActionPerformed(java.awt.event.ActionEvent evt) throws IOException, ClassNotFoundException {
        AddPatientJDialog addPatientJDialog = new AddPatientJDialog(this);
        addPatientJDialog.setVisible(true);

        if (addPatientJDialog.isConfirmed()) {
            String lastname = addPatientJDialog.getLastName();
            String firstname = addPatientJDialog.getFirstName();

            System.out.println(lastname + " " + firstname);

            RequeteADD_PATIENT requete = new RequeteADD_PATIENT(lastname, firstname);

            oos.writeObject(requete);
            oos.flush();

            ReponseADD_PATIENT reponse = (ReponseADD_PATIENT) ois.readObject();

            showMessage("Id du patient créé = " + reponse.getId(), "Information");
        }
    }

    private void jButtonAddConsultationActionPerformed(java.awt.event.ActionEvent evt, JTable table, DefaultTableModel tableModel) throws IOException, ClassNotFoundException {
        AddConsultationJDialog dialog = new AddConsultationJDialog(this);
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        LocalDate date = dialog.getDate();
        LocalTime hour = dialog.getHour();
        int duration = dialog.getDuration();
        int count = dialog.getCount();

        System.out.println("Ajout consultations : " + count + " à partir de " + date + " " + hour + " durée=" + duration);

        RequeteADD_CONSULTATION requete = new RequeteADD_CONSULTATION(doctorConnected.getId(), date, hour, duration, count);

        oos.writeObject(requete);
        oos.flush();

        ReponseADD_CONSULTATION reponse = (ReponseADD_CONSULTATION) ois.readObject();

        if (reponse.isOver17hours()) {
            showWarningMessage("La consultation est au delà de 17h !", "Warning");
            return;
        }
        addToTable(0, null);
    }

    private void jButtonUpdateConsultationActionPerformed(java.awt.event.ActionEvent evt, JTable table,DefaultTableModel tableModel) throws IOException, ClassNotFoundException {
        int row = table.getSelectedRow();

        if (row == -1) {
            showWarningMessage("Veuillez sélectionner une consultation dans le tableau.", "Aucune sélection");
            return;
        }

        Object idObj = tableModel.getValueAt(row, 0);
        if (idObj == null) {
            showWarningMessage("La consultation sélectionnée est invalide.", "Erreur");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int docId = (int) tableModel.getValueAt(row, 1);

        Object patientObj = tableModel.getValueAt(row, 2);
        Integer patientId = null;
        if (patientObj != null && !patientObj.toString().isEmpty()) {
            try {
                patientId = Integer.valueOf(patientObj.toString());
            } catch (NumberFormatException e) {
                patientId = null;
            }
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
            LocalDate newDate = dialog.getDate();
            LocalTime newHour = dialog.getHour();
            Integer newPatientId = dialog.getPatientId();
            int newDuration = dialog.getDuration();
            String newReason = dialog.getReason();

            RequeteUPDATE_CONSULTATION requete = new RequeteUPDATE_CONSULTATION(id, newDate, newHour, newDuration,newPatientId, newReason);

            oos.writeObject(requete);
            oos.flush();

            ReponseUPDATE_CONSULTATION reponse = (ReponseUPDATE_CONSULTATION) ois.readObject();

            if (reponse.isValid())
            {
                tableModel.setValueAt(dialog.getDate().toString(), row, 3);
                tableModel.setValueAt(dialog.getHour().toString(), row, 4);
                Integer pid = dialog.getPatientId();
                tableModel.setValueAt(pid == null ? "" : pid, row, 2);
                tableModel.setValueAt(dialog.getDuration(), row, 5);
                tableModel.setValueAt(dialog.getReason(), row, 6);

                System.out.println("Consultation " + id + " mise à jour !");
            }
            else
                showErrorMessage("Une erreur est survenue lors de l'ajout");
        }
    }

    private void jButtonSearchConsultationActionPerformed(java.awt.event.ActionEvent evt) throws IOException, ClassNotFoundException {
        String Id = txtIdPatient.getText().trim();
        String Date = txtDate.getText().trim();

        if(Id.isEmpty() || Date.isEmpty()){
            showWarningMessage("Veuillez enter un id de patient et une date", "Warning");
            return;
        }

        Integer idPatient;
        try {
            idPatient = Integer.valueOf(Id);
        } catch (NumberFormatException ex) {
            showWarningMessage("L'ID patient doit être un nombre entier.", "Erreur");
            return;
        }

        LocalDate date = null;
        if (!Date.equals("0")) {
            try {
                date = LocalDate.parse(Date);
            } catch (Exception ex) {
                showWarningMessage("La date doit être au format YYYY-MM-DD ou 0.", "Erreur");
                return;
            }
        }

        System.out.println(Id + " " + date);

        addToTable(idPatient, date);
    }

    private void jButtonDeleteConsultation(ActionEvent evt, JTable table, DefaultTableModel tableModel) throws IOException, ClassNotFoundException {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            showWarningMessage("Veuillez sélectionner une consultation avant de supprimer.", "Aucune sélection");
            return;
        }
        Object idObj = tableModel.getValueAt(selectedRow, 0);

        if (idObj == null) {
            showErrorMessage("Impossible de récupérer l'ID de la consultation.");
            return;
        }

        int consultationId = Integer.parseInt(idObj.toString());

        // Confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer la consultation " + consultationId + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        RequeteDELETE_CONSULTATION requete = new RequeteDELETE_CONSULTATION(consultationId);
        oos.writeObject(requete);
        oos.flush();

        ReponseDELETE_CONSULTATION reponse = (ReponseDELETE_CONSULTATION) ois.readObject();

        if (reponse.isValid()) {
            tableModel.removeRow(selectedRow);
            showMessage("Consultation supprimée avec succès.", "Succès");
        } else {
            showErrorMessage("Impossible de supprimer la consultation.");
        }
    }

    public void addToTable(Integer idPatient, LocalDate date) throws IOException, ClassNotFoundException {
        RequeteSEARCH_CONSULTATION requete = new RequeteSEARCH_CONSULTATION(doctorConnected.getId(), idPatient, date);

        oos.writeObject(requete);
        oos.flush();

        ReponseSEARCH_CONSULTATION reponse = (ReponseSEARCH_CONSULTATION) ois.readObject();

        ArrayList<Consultation> consultations = reponse.getSearchedConsultations();

        if (consultations == null || consultations.isEmpty()) {
            showMessage("Aucune consultation trouvée pour ces critères.", "Information");
            return;
        }

        tableModel.setRowCount(0);

        for (Consultation c : consultations) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getDoctor_id(),
                    c.getPatient_id(),
                    c.getDate(),
                    c.getHour(),
                    c.getDuree(),
                    c.getReason()
            });
        }
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

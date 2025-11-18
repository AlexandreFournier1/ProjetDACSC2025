package hepl.dacsc.model.dao;

import hepl.dacsc.model.entity.Patient;
import hepl.dacsc.model.viewmodel.PatientSearchVM;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;
public class PatientDAO {
    private ConnectDB connectDB;
    private ArrayList<Patient> patients;

    public PatientDAO() {
        connectDB = new ConnectDB();
        patients = new ArrayList<>();
    }

    public Patient getPatientById(Integer id) {
        for (Patient entity : patients) {
            if (Objects.equals(entity.getId(), id))
            {
                return entity;
            }
        }
        return null;
    }

    public Patient getPatientByName(String lastname, String firstname) {
        for (Patient entity : patients) {
            if (Objects.equals(entity.getLast_name(), lastname) && Objects.equals(entity.getFirst_name(), firstname)) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Patient> loadPatients() {
        return this.loadPatients(null);
    }

    public ArrayList<Patient> loadPatients(PatientSearchVM csvm) {
        patients.clear();
        try {
            String requete = "SELECT " +
                    "patients.id, " +
                    "patients.last_name, " +
                    "patients.first_name, " +
                    "patients.birth_date " +
                    "FROM patients";
            if (csvm != null) {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null) {
                    where += "AND patients.id = ? ";
                }
                if (csvm.getLast_name() != null) {
                    where += "AND patients.last_name like ? ";
                }
                if (csvm.getFirst_name() != null) {
                    where += "AND patients.first_name like ? ";
                }
                if (csvm.getBirth_date() != null) {
                    where += "AND patients.birth_date = ? ";
                }
                requete += where + " ORDER BY patients.id ASC";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (csvm != null) {
                int paramNumber = 0;
                if(csvm.getId() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if(csvm.getLast_name() != null){
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getLast_name());
                }
                if(csvm.getFirst_name() != null){
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getFirst_name());
                }
                if(csvm.getBirth_date() != null){
                    paramNumber++;
                    stmt.setDate(paramNumber, Date.valueOf(csvm.getBirth_date()));
                }

            }
            ResultSet rs = stmt.executeQuery();
            patients.clear();

            while (rs.next()) {
                Integer patientId = rs.getInt("id");
                String last_name = rs.getString("last_name");
                String first_name = rs.getString("first_name");
                LocalDate birth_date = rs.getDate("birth_date").toLocalDate();

                Patient patient1 = new Patient(patientId, last_name, first_name, birth_date);

                patients.add(patient1);
            }
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            return patients;
        }
    }

    public void save(Patient c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE patients SET "
                            + " last_name = ?, "
                            + " first_name = ?, "
                            + " birth_date = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setString(1, c.getLast_name());
                    pStmt.setString(2, c.getFirst_name());
                    pStmt.setDate(3, Date.valueOf(c.getBirth_date()));
                    pStmt.setInt(4, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO patients ("
                            + "last_name, "
                            + "first_name, "
                            + "birth_date "
                            + ") VALUES ("
                            + "?, "
                            + "?, "
                            + "? "
                            + ")";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    pStmt.setString(1, c.getLast_name());
                    pStmt.setString(2, c.getFirst_name());
                    pStmt.setDate(3, Date.valueOf(c.getBirth_date()));
                    pStmt.executeUpdate();

                    ResultSet rs = pStmt.getGeneratedKeys();
                    rs.next();
                    c.setId(rs.getInt(1));

                    rs.close();
                    pStmt.close();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Patient entity) {
        if(entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if(id != null) {
            try {
                String requete;

                requete = " DELETE FROM patients WHERE id = ?";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

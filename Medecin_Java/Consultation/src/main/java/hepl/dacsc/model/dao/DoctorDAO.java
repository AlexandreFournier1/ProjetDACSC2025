package hepl.dacsc.model.dao;

import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;

import java.sql.*;
import java.util.*;

public class DoctorDAO {
    private ConnectDB connectDB;
    private ArrayList<Doctor> doctors;

    public DoctorDAO() {
        connectDB = new ConnectDB();
        doctors = new ArrayList<>();
    }

    public Doctor getDoctorsById(Integer id) {
        for (Doctor entity : doctors) {
            if (Objects.equals(entity.getId(), id))
            {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Doctor> loadDoctor() {
        return this.loadDoctor(null);
    }

    public ArrayList<Doctor> loadDoctor(DoctorSearchVM csvm) {
        doctors.clear();
        System.out.println("[DAO] debut requette");
        try {
            String requete = "SELECT " +
                    "doctors.id, " +
                    "doctors.speciality_id, " +
                    "doctors.last_name, " +
                    "doctors.first_name " +
                    "FROM doctors";
            if (csvm != null) {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null) {
                    where += "AND doctors.id = ? ";
                }
                if (csvm.getSpeciality_id() != null) {
                    where += "AND doctors.speciality_id = ? ";
                }
                if (csvm.getLast_name() != null) {
                    where += "AND doctors.last_name like ? ";
                }
                if (csvm.getFirst_name() != null) {
                    where += "AND doctors.first_name like ? ";
                }
                // Ajout Pour le connect
                if (csvm.getMdp() != null) {
                    where += "AND doctors.mdp like ? ";
                }
                requete += where + " ORDER BY doctors.id ASC";
            }
            System.out.println("[DAO] Fin requette");
            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (csvm != null) {
                int paramNumber = 0;
                if(csvm.getId() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if(csvm.getSpeciality_id() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getSpeciality_id());
                }
                if(csvm.getLast_name() != null){
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getLast_name());
                }
                if(csvm.getFirst_name() != null){
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getFirst_name());
                }

            }
            ResultSet rs = stmt.executeQuery();
            doctors.clear();

            while (rs.next()) {
                Integer doctorId = rs.getInt("id");
                Integer speciality_id = rs.getInt("speciality_id");
                String last_name = rs.getString("last_name");
                String first_name = rs.getString("first_name");

                Doctor doctor1 = new Doctor(doctorId, speciality_id, last_name, first_name);

                doctors.add(doctor1);
            }
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            return doctors;
        }
    }

    public void save(Doctor c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE doctors SET "
                            + " speciality_id = ?, "
                            + " last_name = ?, "
                            + " first_name = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setInt(1, c.getSpeciality_id());
                    pStmt.setString(2, c.getLast_name());
                    pStmt.setString(3, c.getFirst_name());
                    pStmt.setInt(4, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO doctors ("
                            + "speciality_id, "
                            + "last_name, "
                            + "first_name, "
                            + "mdp "
                            + ") VALUES ("
                            + "?, "
                            + "?, "
                            + "? "
                            + "? "
                            + ")";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    pStmt.setInt(1, c.getSpeciality_id());
                    pStmt.setString(2, c.getLast_name());
                    pStmt.setString(3, c.getFirst_name());
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

    public void delete(Doctor entity) {
        if(entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if(id != null) {
            try {
                String requete;

                requete = " DELETE FROM doctors WHERE id = ?";
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

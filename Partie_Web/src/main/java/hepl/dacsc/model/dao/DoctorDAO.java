package hepl.dacsc.model.dao;

import hepl.dacsc.model.entity.Doctor;
import hepl.dacsc.model.viewmodel.DoctorSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

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
        System.out.println("[DAO] Début requête loadDoctor");

        try {
            String requete =
                    "SELECT " +
                            "doctors.id, " +
                            "doctors.specialty_id, " +
                            "doctors.last_name, " +
                            "doctors.first_name " +
                            "FROM doctors " +
                            "JOIN specialities ON doctors.specialty_id = specialities.id ";

            if (csvm != null) {
                String where = "WHERE 1=1 ";

                if (csvm.getId() != null) {
                    where += "AND doctors.id = ? ";
                }
                if (csvm.getSpecialty_id() != null) {
                    where += "AND doctors.specialty_id = ? ";
                }
                if (csvm.getSpecialty_name() != null) {
                    where += "AND specialities.name LIKE ? ";
                }
                if (csvm.getLast_name() != null) {
                    where += "AND doctors.last_name LIKE ? ";
                }
                if (csvm.getFirst_name() != null) {
                    where += "AND doctors.first_name LIKE ? ";
                }
                if (csvm.getMdp() != null) {
                    where += "AND doctors.mdp LIKE ? ";
                }

                requete += where;
            }

            requete += "ORDER BY doctors.id ASC;";

            System.out.println("[DAO] Requête SQL = " + requete);

            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (csvm != null) {
                int paramNumber = 0;

                if (csvm.getId() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if (csvm.getSpecialty_id() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getSpecialty_id());
                }
                if (csvm.getSpecialty_name() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getSpecialty_name());
                }
                if (csvm.getLast_name() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getLast_name());
                }
                if (csvm.getFirst_name() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getFirst_name());
                }
                if (csvm.getMdp() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getMdp());
                }

                System.out.println("[DAO] Nombre de paramètres bindés = " + paramNumber);
            }

            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("[DAO] Aucun médecin trouvé.");
            } else {
                System.out.println("[DAO] Résultats trouvés.");
            }

            while (rs.next()) {
                Integer doctorId = rs.getInt("id");
                Integer specialtyId = rs.getInt("specialty_id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");

                Doctor doctor = new Doctor(doctorId, specialtyId, lastName, firstName);
                doctors.add(doctor);
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            System.out.println("[DAO] ERREUR SQL : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return doctors;
    }

    public void save(Doctor c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE doctors SET "
                            + " specialty_id = ?, "
                            + " last_name = ?, "
                            + " first_name = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setInt(1, c.getSpecialty_id());
                    pStmt.setString(2, c.getLast_name());
                    pStmt.setString(3, c.getFirst_name());
                    pStmt.setInt(4, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO doctors ("
                            + "specialty_id, "
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

                    pStmt.setInt(1, c.getSpecialty_id());
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

package hepl.dacsc.model.dao;

import hepl.dacsc.model.entity.Consultation;
import hepl.dacsc.model.viewmodel.ConsultationSearchVM;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
public class ConsultationDAO {
    private ConnectDB connectDB;
    private ArrayList<Consultation> consultations;

    public ConsultationDAO() {
        connectDB = new ConnectDB();
        consultations = new ArrayList<>();
    }

    public Consultation getConsultationsById(Integer id) {
        for (Consultation entity : consultations) {
            if (Objects.equals(entity.getId(), id))
            {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Consultation> getConsultationsByDoctorId(Integer id) {
        // Toujours recharger depuis la DB
        loadConsultations();

        ArrayList<Consultation> result = new ArrayList<>();

        for (Consultation entity : consultations) {
            if (Objects.equals(entity.getDoctor_id(), id)) {
                result.add(entity);
            }
        }
        return result;
    }

    public void updateConsultation(Integer consultationId,
                                      Integer patientId,
                                      String reason) {
        try {
            String sql = "UPDATE consultations SET " +
                    "patient_id = ?, " +
                    "reason = ? " +
                    "WHERE id = ?";

            PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

            stmt.setInt(1, patientId);
            stmt.setString(2, reason);
            stmt.setInt(3, consultationId);

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Consultation> loadConsultations() {
        return this.loadConsultations(null);
    }

    public ArrayList<Consultation> loadConsultations(ConsultationSearchVM csvm) {
        consultations.clear();
        try {
            String requete = "SELECT " +
                    "consultations.id, " +
                    "consultations.doctor_id, " +
                    "doctors.last_name, " +
                    "specialties.name, " +
                    "consultations.patient_id, " +
                    "consultations.date, " +
                    "consultations.hour, " +
                    "consultations.duree, " +
                    "consultations.reason " +
                    "FROM consultations " +
                    "JOIN doctors ON doctors.id = consultations.doctor_id " +
                    "JOIN specialties ON doctors.specialty_id = specialties.id ";

            if (csvm != null) {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null) {
                    where += "AND consultations.id = ? ";
                }
                if (csvm.getDoctor_id() != null) {
                    where += "AND consultations.doctor_id = ? ";
                }
                if (csvm.getDoctor_name() != null) {
                    where += "AND doctors.last_name = ? ";
                }
                if (csvm.getSpeciality_name() != null) {
                    where += "AND specialties.name LIKE ? ";
                }
                if (csvm.getPatient_id() != null) {
                    where += "AND consultations.patient_id = ? ";
                }
                if (csvm.getDate() != null) {
                    where += "AND consultations.date = ? ";
                }
                if(csvm.getHour() != null) {
                    where += "AND consultations.hour = ? ";
                }
                if(csvm.getDuree() != null) {
                    where += "AND consultations.duree = ? ";
                }
                if(csvm.getReason() != null) {
                    where += "AND consultations.reason = ? ";
                }
                requete += where + "ORDER BY consultations.id ASC";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (csvm != null) {
                int paramNumber = 0;
                if(csvm.getId() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if(csvm.getDoctor_id() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getDoctor_id());
                }
                if (csvm.getDoctor_name() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getDoctor_name());
                }
                if (csvm.getSpeciality_name() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getSpeciality_name());
                }
                if(csvm.getPatient_id() != null) {
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getPatient_id());
                }
                if(csvm.getDate() != null) {
                    paramNumber++;
                    stmt.setDate(paramNumber, Date.valueOf(csvm.getDate()));
                }
                if(csvm.getHour() != null){
                    paramNumber++;
                    stmt.setTime(paramNumber, Time.valueOf(csvm.getHour()));
                }
                if(csvm.getDuree() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getDuree());
                }
                if (csvm.getReason() != null) {
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getReason());
                }
            }
            ResultSet rs = stmt.executeQuery();
            consultations.clear();

            while (rs.next()) {
                Integer consultationId = rs.getInt("id");
                Integer doctorId = rs.getInt("doctor_id");
                String doctorName = rs.getString("last_name");
                String specialtyName = rs.getString("name");
                Integer patientId = rs.getObject("patient_id", Integer.class);
                LocalDate date = rs.getDate("date").toLocalDate();

                String hourStr = rs.getString("hour");
                if (hourStr.length() == 4) {            // Si format H:mm : exemple 9:00
                    hourStr = "0" + hourStr;            // devient 09:00
                }
                LocalTime hour = LocalTime.parse(hourStr);

                Integer duree = rs.getInt("duree");
                String reason = rs.getString("reason");

                Consultation consultation = new Consultation(consultationId, doctorId, doctorName, specialtyName, patientId, date, hour, duree, reason);

                consultations.add(consultation);
            }
            stmt.close();

            return consultations;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Consultation c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE consultations SET "
                            + " doctor_id = ?, "
                            + " patient_id = ?, "
                            + " date = ?, "
                            + " hour = ?, "
                            + " duree = ?, "
                            + " reason = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setObject(2, c.getPatient_id(), Types.INTEGER);
                    pStmt.setDate(3, Date.valueOf(c.getDate()));
                    pStmt.setTime(4, Time.valueOf(c.getHour()));
                    pStmt.setInt(5, c.getDuree());
                    pStmt.setString(6, c.getReason());
                    pStmt.setInt(7, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO consultations ("
                            + "doctor_id, "
                            + "patient_id, "
                            + "date, "
                            + "hour, "
                            + "duree, "
                            + "reason"
                            + ") VALUES ("
                            + "?, "
                            + "?, "
                            + "?, "
                            + "?, "
                            + "?, "
                            + "? "
                            + ")";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setObject(2, c.getPatient_id(), Types.INTEGER);
                    pStmt.setDate(3, Date.valueOf(c.getDate()));
                    pStmt.setTime(4, Time.valueOf(c.getHour()));
                    pStmt.setInt(5, c.getDuree());
                    pStmt.setString(6, c.getReason());

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

    public void delete(Consultation entity) {
        if(entity != null || entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if(id != null) {
            try {
                String requete;

                requete = " DELETE FROM consultations WHERE id = ? ;";
                PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean deleteReservation(Integer id) {
        if (id == null) {
            return false;
        }

        try {
            String requete = "SELECT patient_id " +
                            "FROM consultations " +
                            "WHERE id = ?";

            PreparedStatement stmt1 = connectDB.getConn().prepareStatement(requete);
            stmt1.setInt(1, id);

            ResultSet rs = stmt1.executeQuery();

            if (!rs.next()) {
                rs.close();
                stmt1.close();
                return false;
            }

            Integer patientId = rs.getObject("patient_id", Integer.class);

            rs.close();
            stmt1.close();

            if (patientId == null) {
                return false;
            }

            requete = "UPDATE consultations SET " +
                    "patient_id = NULL, " +
                    "reason = NULL " +
                    "WHERE id = ?";

            PreparedStatement stmt2 = connectDB.getConn().prepareStatement(requete);
            stmt2.setInt(1, id);
            stmt2.executeUpdate();
            stmt2.close();

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

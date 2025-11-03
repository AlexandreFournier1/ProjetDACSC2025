package model.dao;

import model.entity.Consultation;
import model.viewmodel.ConsultationSearchVM;

import java.sql.*;
import java.sql.Date;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.*;
import java.time.LocalDate;
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

    public ArrayList<Consultation> loadConsultations() {
        return this.loadConsultations(null);
    }

    public ArrayList<Consultation> loadConsultations(ConsultationSearchVM csvm) {
        consultations.clear();
        try {
            String requete = "SELECT " +
                    "consultations.id, " +
                    "consultations.doctor_id, " +
                    "consultations.patient_id, " +
                    "consultations.date, " +
                    "consultations.hour, " +
                    "consultations.reason " +
                    "FROM consultations";
            if (csvm != null) {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != 0) {
                    where += "AND consultations.id = ? ";
                }
                if (csvm.getDoctor_id() != 0) {
                    where += "AND consultations.doctor_id = ? ";
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
                Integer patientId = rs.getInt("patient_id");
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime hour = rs.getTime("hour").toLocalTime();
                String reason = rs.getString("reason");

                Consultation consultation = new Consultation(consultationId, doctorId,
                        patientId, date, hour, reason);

                consultations.add(consultation);
            }
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            return consultations;
        }
    }

    public void save(Consultation c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE Consultations SET "
                            + " doctor_id = ?, "
                            + " patient_id = ?, "
                            + " date = ?, "
                            + " hour = ?, "
                            + " reason = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setInt(2, c.getPatient_id());
                    pStmt.setDate(3, Date.valueOf(c.getDate()));
                    pStmt.setTime(4, Time.valueOf(c.getHour()));
                    pStmt.setString(5, c.getReason());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO Consultations ("
                            + "doctor_id, "
                            + "patient_id, "
                            + "date, "
                            + "hour, "
                            + ") VALUES ("
                            + "?, "
                            + "?, "
                            + "?, "
                            + "?, "
                            + ")";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    pStmt.setInt(1, c.getDoctor_id());
                    pStmt.setInt(2, c.getPatient_id());
                    pStmt.setDate(3, Date.valueOf(c.getDate()));
                    pStmt.setTime(4, Time.valueOf(c.getHour()));
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

                requete = " DELETE FROM Consultations WHERE id = ?";
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

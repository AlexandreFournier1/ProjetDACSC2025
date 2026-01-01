package hepl.dacsc.model.dao;

import hepl.dacsc.model.entity.Rapport;
import hepl.dacsc.model.viewmodel.RapportSearchVM;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class RapportDAO {
    private ConnectDB connectDB;
    private ArrayList<Rapport> rapports;

    public RapportDAO() {
        connectDB = new ConnectDB();
        rapports = new ArrayList<>();
    }

    public Rapport getRapportById(Integer id) {
        for (Rapport r : rapports) {
            if (Objects.equals(r.getId(), id)) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<Rapport> loadRapports() {
        return loadRapports(null);
    }

    public ArrayList<Rapport> loadRapports(RapportSearchVM rsvm) {
        rapports.clear();

        try {
            String requete = "SELECT " +
                    "reports.id, " +
                    "reports.doctor_id, " +
                    "reports.patient_id, " +
                    "reports.report_date, " +
                    "reports.report_text " +
                    "FROM reports";

            if (rsvm != null) {
                String where = " WHERE 1=1 ";

                if (rsvm.getId() != null) {
                    where += "AND reports.id = ? ";
                }
                if (rsvm.getIdDoctor() != null) {
                    where += "AND reports.doctor_id = ? ";
                }
                if (rsvm.getIdPatient() != null) {
                    where += "AND reports.patient_id = ? ";
                }
                if (rsvm.getDate() != null) {
                    where += "AND reports.report_date = ? ";
                }
                if (rsvm.getTextRapport() != null) {
                    where += "AND reports.report_text LIKE ? ";
                }

                requete += where + " ORDER BY reports.id ASC";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (rsvm != null) {
                int param = 0;

                if (rsvm.getId() != null) {
                    stmt.setInt(++param, rsvm.getId());
                }
                if (rsvm.getIdDoctor() != null) {
                    stmt.setInt(++param, rsvm.getIdDoctor());
                }
                if (rsvm.getIdPatient() != null) {
                    stmt.setInt(++param, rsvm.getIdPatient());
                }
                if (rsvm.getDate() != null) {
                    stmt.setDate(++param, Date.valueOf(rsvm.getDate()));
                }
                if (rsvm.getTextRapport() != null) {
                    stmt.setString(++param, "%" + rsvm.getTextRapport() + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                Integer doctorId = rs.getInt("doctor_id");
                Integer patientId = rs.getInt("patient_id");
                LocalDate date = rs.getDate("report_date").toLocalDate();
                String text = rs.getString("report_text");

                Rapport rapport = new Rapport(id, doctorId, patientId, date, text);
                rapports.add(rapport);
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rapports;
    }

    public void save(Rapport r) {
        if (r == null) return;

        try {
            String requete;

            if (r.getId() != null) { // UPDATE
                requete = "UPDATE reports SET " +
                        "doctor_id = ?, " +
                        "patient_id = ?, " +
                        "report_date = ?, " +
                        "report_text = ? " +
                        "WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

                stmt.setInt(1, r.getIdDoctor());
                stmt.setInt(2, r.getIdPatient());
                stmt.setDate(3, Date.valueOf(r.getDate()));
                stmt.setString(4, r.getTextRapport());
                stmt.setInt(5, r.getId());

                stmt.executeUpdate();
                stmt.close();

            } else { // INSERT
                requete = "INSERT INTO reports (" +
                        "doctor_id, patient_id, report_date, report_text" +
                        ") VALUES (?, ?, ?, ?)";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(
                        requete,
                        PreparedStatement.RETURN_GENERATED_KEYS
                );

                stmt.setInt(1, r.getIdDoctor());
                stmt.setInt(2, r.getIdPatient());
                stmt.setDate(3, Date.valueOf(r.getDate()));
                stmt.setString(4, r.getTextRapport());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    r.setId(rs.getInt(1));
                }

                rs.close();
                stmt.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Rapport r) {
        if (r != null && r.getId() != null) {
            delete(r.getId());
        }
    }

    public void delete(Integer id) {
        if (id == null) return;

        try {
            String requete = "DELETE FROM reports WHERE id = ?";
            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

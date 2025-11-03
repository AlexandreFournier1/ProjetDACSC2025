package model.dao;

import model.entity.Speciality;
import model.viewmodel.SpecialitySearchVM;

import java.sql.*;
import java.sql.Date;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.*;
import java.time.LocalDate;
public class SpecialityDAO {
    private ConnectDB connectDB;
    private ArrayList<Speciality> specialities;

    public SpecialityDAO() {
        connectDB = new ConnectDB();
        specialities = new ArrayList<>();
    }

    public Speciality getSpecialitiesById(Integer id) {
        for (Speciality entity : specialities) {
            if (Objects.equals(entity.getId(), id))
            {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Speciality> loadSpeciality() {
        return this.loadSpeciality(null);
    }

    public ArrayList<Speciality> loadSpeciality(SpecialitySearchVM csvm) {
        specialities.clear();
        try {
            String requete = "SELECT " +
                    "specialities.id, " +
                    "specialities.name " +
                    "FROM specialities";
            if (csvm != null) {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null) {
                    where += "AND specialities.id = ? ";
                }
                if (csvm.getName() != null) {
                    where += "AND specialities.name like ? ";
                }
                requete += where + " ORDER BY specialities.id ASC";
            }

            PreparedStatement stmt = connectDB.getConn().prepareStatement(requete);

            if (csvm != null) {
                int paramNumber = 0;
                if(csvm.getId() != null){
                    paramNumber++;
                    stmt.setInt(paramNumber, csvm.getId());
                }
                if(csvm.getName() != null){
                    paramNumber++;
                    stmt.setString(paramNumber, csvm.getName());
                }

            }
            ResultSet rs = stmt.executeQuery();
            specialities.clear();

            while (rs.next()) {
                Integer specialityId = rs.getInt("id");
                String name = rs.getString("name");

                Speciality speciality = new Speciality(specialityId, name);

                specialities.add(speciality);
            }
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            return specialities;
        }
    }

    public void save(Speciality c) {
        try {
            String requete;

            if(c != null) {
                if (c.getId() != null) { // Update

                    requete = "UPDATE specialities SET "
                            + " name = ? "
                            + " WHERE id = ?";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete);

                    pStmt.setString(1, c.getName());
                    pStmt.setInt(2, c.getId());
                    pStmt.executeUpdate();
                    pStmt.close();

                } else {
                    requete = "INSERT INTO specialities ("
                            + "name "
                            + ") VALUES ("
                            + "? "
                            + ")";

                    PreparedStatement pStmt = connectDB.getConn().prepareStatement(requete,
                            PreparedStatement.RETURN_GENERATED_KEYS);

                    pStmt.setString(1, c.getName());
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

    public void delete(Speciality entity) {
        if(entity != null && entity.getId() != null) {
            this.delete(entity.getId());
        }
    }

    public void delete(Integer id) {
        if(id != null) {
            try {
                String requete;

                requete = " DELETE FROM specialities WHERE id = ?";
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

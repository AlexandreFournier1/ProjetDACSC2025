package hepl.dacsc.model.dao;

import java.sql.*;
import java.util.logging.*;

public class ConnectDB {
    private static Connection conn = null;
    public Connection getConn() {
        return conn;
    }
    public ConnectDB() {
        try {
            if (conn == null || conn.isClosed()) {
                // IP Noah
                //String sCon = "jdbc:mysql://192.168.162.128/PourStudent";
                // IP Alex
                String sCon = "jdbc:mysql://192.168.186.128/PourStudent";
                String sUser = "Student";
                String sPwd = "PassStudent1_";
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(sCon, sUser, sPwd);
                System.out.println("[DEBUG] Connecter avec JDBC");
            }
        }
        catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void close() {
        try {
            conn.close();
            System.out.println("Closing DB connection");
        }
        catch (SQLException ex) {
            Logger.getLogger(ConnectDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

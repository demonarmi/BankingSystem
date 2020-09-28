import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBCConnector {
    private static String url = "jdbc:sqlite:src/BankingSystem.db";

    public static void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cards( " +
                "id INTEGER PRIMARY KEY," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0" + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insert(String sql){
        try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

}



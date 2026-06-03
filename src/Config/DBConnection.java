package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hinh
 */
public class DBConnection {
    // Connection settings. Defaults keep the old local setup, GARAGE_DB_* can override them.
    private static final String SERVER = getConfig("GARAGE_DB_SERVER", "localhost");
    private static final String PORT = getConfig("GARAGE_DB_PORT", "1433");
    private static final String DATABASE = getConfig("GARAGE_DB_NAME", "Garage");
    private static final String USERNAME = getConfig("GARAGE_DB_USER", "sa");
    private static final String PASSWORD = getConfig("GARAGE_DB_PASSWORD", "123456");
    
    // SQL Server connection URL.
    private static final String URL = 
        "jdbc:sqlserver://" + SERVER + ":" + PORT 
        + ";databaseName=" + DATABASE 
        + ";encrypt=true;trustServerCertificate=true";
    
    // All DAOs use this method to open a database connection.
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }

    private static String getConfig(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }

        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        return defaultValue;
    }
    
    // Quick connection test.
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Ket noi thanh cong toi database Garage!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ket noi that bai!");
        }
    }
}

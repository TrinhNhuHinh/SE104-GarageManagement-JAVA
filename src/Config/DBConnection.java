package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String SERVER = getConfig("GARAGE_DB_SERVER", "127.0.0.1");
    private static final String PORT = getConfig("GARAGE_DB_PORT", "1433");
    private static final String DATABASE = getConfig("GARAGE_DB_NAME", "Garage");
    private static final String USERNAME = getConfig("GARAGE_DB_USER", "sa");
    private static final String PASSWORD = getConfig("GARAGE_DB_PASSWORD", "123456");

    private static final String URL =
            "jdbc:sqlserver://" + SERVER + ":" + PORT
            + ";databaseName=" + DATABASE
            + ";encrypt=false"
            + ";trustServerCertificate=true"
            + ";loginTimeout=3";

    static {
        try {
            DriverManager.setLoginTimeout(3);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Khong tim thay JDBC Driver SQL Server.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
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

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Ket noi thanh cong toi database Garage!");
        } catch (SQLException e) {
            System.out.println("Ket noi that bai!");
            e.printStackTrace();
        }
    }
}
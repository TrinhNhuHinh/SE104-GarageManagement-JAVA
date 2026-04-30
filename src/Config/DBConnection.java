package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hinh
 */
public class DBConnection {
    // Thông tin kết nối
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "Garage";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456"; // password của sa
    
    // URL kết nối SQL Server
    private static final String URL = 
        "jdbc:sqlserver://" + SERVER + ":" + PORT 
        + ";databaseName=" + DATABASE 
        + ";encrypt=true;trustServerCertificate=true";
    
    // Hàm lấy kết nối tới database, mọi DAO đều gọi hàm này
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại!");
            e.printStackTrace();
        }
        return conn;
    }
    
    // Hàm test
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
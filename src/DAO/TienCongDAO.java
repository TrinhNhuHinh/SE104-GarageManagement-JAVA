package DAO;

import Config.DBConnection;
import MODEL.TienCong;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TienCongDAO {

    public boolean insert(TienCong tc) {
        String sql = """
            INSERT INTO TIENCONG
            (MaTienCong, SoTienCong, NoiDungTienCong)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tc.getMaTienCong());
            ps.setDouble(2, tc.getSoTienCong());
            ps.setString(3, tc.getNoiDungTienCong());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<TienCong> getAll() {
        List<TienCong> list = new ArrayList<>();

        String sql = "SELECT * FROM TIENCONG ORDER BY MaTienCong";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToTienCong(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public List<String> getAllIds() {
        List<String> list = new ArrayList<>();

        String sql = "SELECT MaTienCong FROM TIENCONG ORDER BY MaTienCong";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("MaTienCong").trim());
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public TienCong getById(String maTienCong) {
        String sql = "SELECT * FROM TIENCONG WHERE MaTienCong = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTienCong);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToTienCong(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(TienCong tc) {
        String sql = """
            UPDATE TIENCONG
            SET SoTienCong = ?,
                NoiDungTienCong = ?
            WHERE MaTienCong = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, tc.getSoTienCong());
            ps.setString(2, tc.getNoiDungTienCong());
            ps.setString(3, tc.getMaTienCong());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maTienCong) {
        String sql = "DELETE FROM TIENCONG WHERE MaTienCong = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTienCong);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM TIENCONG";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return 0;
    }

    private TienCong mapResultSetToTienCong(ResultSet rs) throws SQLException {
        TienCong tc = new TienCong();

        tc.setMaTienCong(rs.getString("MaTienCong"));
        tc.setSoTienCong(rs.getDouble("SoTienCong"));
        tc.setNoiDungTienCong(rs.getString("NoiDungTienCong"));

        return tc;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
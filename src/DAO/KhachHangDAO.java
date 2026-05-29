package DAO;

import Config.DBConnection;
import MODEL.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public boolean insert(KhachHang kh) {
        String sql = """
            INSERT INTO KHACHHANG
            (MaKhachHang, TenKhachHang, DiaChiKhachHang, SoDienThoaiKhachHang)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getDiaChiKhachHang());
            ps.setString(4, kh.getSoDienThoaiKhachHang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();

        String sql = "SELECT * FROM KHACHHANG ORDER BY MaKhachHang";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public KhachHang getById(String maKhachHang) {
        String sql = "SELECT * FROM KHACHHANG WHERE MaKhachHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToKhachHang(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(KhachHang kh) {
        String sql = """
            UPDATE KHACHHANG
            SET TenKhachHang = ?,
                DiaChiKhachHang = ?,
                SoDienThoaiKhachHang = ?
            WHERE MaKhachHang = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getDiaChiKhachHang());
            ps.setString(3, kh.getSoDienThoaiKhachHang());
            ps.setString(4, kh.getMaKhachHang());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maKhachHang) {
        String sql = "DELETE FROM KHACHHANG WHERE MaKhachHang = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKhachHang);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();

        String sql = """
            SELECT * FROM KHACHHANG
            WHERE MaKhachHang LIKE ?
               OR TenKhachHang LIKE ?
               OR DiaChiKhachHang LIKE ?
               OR SoDienThoaiKhachHang LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToKhachHang(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();

        kh.setMaKhachHang(rs.getString("MaKhachHang"));
        kh.setTenKhachHang(rs.getString("TenKhachHang"));
        kh.setDiaChiKhachHang(rs.getString("DiaChiKhachHang"));
        kh.setSoDienThoaiKhachHang(rs.getString("SoDienThoaiKhachHang"));

        return kh;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
package DAO;

import Config.DBConnection;
import MODEL.PhieuThuTien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuThuTienDAO {

    public boolean insert(PhieuThuTien pt) {
        String sql = """
            INSERT INTO PHIEUTHUTIEN
            (MaPhieuThuTien, Ma_TiepNhanXe, NgayThuTien, BienSoXe, Email, SoDienThoai, SoTienThu)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pt.getMaPhieuThuTien());
            ps.setString(2, pt.getMaTiepNhanXe());
            ps.setDate(3, pt.getNgayThuTien());
            ps.setString(4, pt.getBienSoXe());
            ps.setString(5, pt.getEmail());
            ps.setString(6, pt.getSoDienThoai());
            ps.setDouble(7, pt.getSoTienThu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<PhieuThuTien> getAll() {
        List<PhieuThuTien> list = new ArrayList<>();

        String sql = "SELECT * FROM PHIEUTHUTIEN ORDER BY MaPhieuThuTien";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToPhieuThu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public PhieuThuTien getById(String maPhieuThuTien) {
        String sql = "SELECT * FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieuThuTien);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToPhieuThu(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(PhieuThuTien pt) {
        String sql = """
            UPDATE PHIEUTHUTIEN
            SET NgayThuTien = ?,
                BienSoXe = ?,
                Email = ?,
                SoDienThoai = ?,
                SoTienThu = ?
            WHERE MaPhieuThuTien = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, pt.getNgayThuTien());
            ps.setString(2, pt.getBienSoXe());
            ps.setString(3, pt.getEmail());
            ps.setString(4, pt.getSoDienThoai());
            ps.setDouble(5, pt.getSoTienThu());
            ps.setString(6, pt.getMaPhieuThuTien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maPhieuThuTien) {
        String sql = "DELETE FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieuThuTien);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<PhieuThuTien> search(String keyword) {
        List<PhieuThuTien> list = new ArrayList<>();

        String sql = """
            SELECT * FROM PHIEUTHUTIEN
            WHERE MaPhieuThuTien LIKE ?
               OR Ma_TiepNhanXe LIKE ?
               OR BienSoXe LIKE ?
               OR SoDienThoai LIKE ?
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
                list.add(mapResultSetToPhieuThu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    private PhieuThuTien mapResultSetToPhieuThu(ResultSet rs) throws SQLException {
        PhieuThuTien pt = new PhieuThuTien();

        pt.setMaPhieuThuTien(rs.getString("MaPhieuThuTien"));
        pt.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
        pt.setNgayThuTien(rs.getDate("NgayThuTien"));
        pt.setBienSoXe(rs.getString("BienSoXe"));
        pt.setEmail(rs.getString("Email"));
        pt.setSoDienThoai(rs.getString("SoDienThoai"));
        pt.setSoTienThu(rs.getDouble("SoTienThu"));

        return pt;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
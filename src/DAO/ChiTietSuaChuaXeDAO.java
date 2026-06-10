package DAO;

import Config.DBConnection;
import MODEL.ChiTietSuaChuaXe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSuaChuaXeDAO {

    public boolean insert(ChiTietSuaChuaXe ct) {
        String sql = """
            INSERT INTO CHITIETSUACHUAXE
            (MaChiTietSuaChuaXe, Ma_SuaChuaXe, NoiDung, Ma_VatTuPhuTung, SoLuong, DonGia, Ma_TienCong, ThanhTien, SoTienCong)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getMaChiTietSuaChuaXe());
            ps.setString(2, ct.getMaSuaChuaXe());
            ps.setString(3, ct.getNoiDung());
            if (ct.getMaVatTuPhuTung() == null || ct.getMaVatTuPhuTung().trim().isEmpty()) {
                ps.setNull(4, Types.CHAR);
            } else {
                ps.setString(4, ct.getMaVatTuPhuTung());
            }
            ps.setInt(5, ct.getSoLuong());
            ps.setDouble(6, ct.getDonGia());

            if (ct.getMaTienCong() == null || ct.getMaTienCong().trim().isEmpty()) {
                ps.setNull(7, Types.CHAR);
            } else {
                ps.setString(7, ct.getMaTienCong());
            }

            ps.setDouble(8, ct.getThanhTien());
            ps.setDouble(9, ct.getSoTienCong());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<ChiTietSuaChuaXe> getAll() {
        List<ChiTietSuaChuaXe> list = new ArrayList<>();

        String sql = "SELECT * FROM CHITIETSUACHUAXE ORDER BY Ma_SuaChuaXe, MaChiTietSuaChuaXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToChiTiet(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public ChiTietSuaChuaXe getById(String maChiTietSuaChuaXe) {
        String sql = "SELECT * FROM CHITIETSUACHUAXE WHERE MaChiTietSuaChuaXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChiTietSuaChuaXe);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToChiTiet(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public List<ChiTietSuaChuaXe> getByMaSuaChua(String maSuaChuaXe) {
        List<ChiTietSuaChuaXe> list = new ArrayList<>();

        String sql = "SELECT * FROM CHITIETSUACHUAXE WHERE Ma_SuaChuaXe = ? ORDER BY MaChiTietSuaChuaXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSuaChuaXe);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToChiTiet(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public boolean update(ChiTietSuaChuaXe ct) {
        String sql = """
            UPDATE CHITIETSUACHUAXE
            SET NoiDung = ?,
                Ma_VatTuPhuTung = ?,
                SoLuong = ?,
                DonGia = ?,
                Ma_TienCong = ?,
                ThanhTien = ?,
                SoTienCong = ?
            WHERE MaChiTietSuaChuaXe = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getNoiDung());
            if (ct.getMaVatTuPhuTung() == null || ct.getMaVatTuPhuTung().trim().isEmpty()) {
                ps.setNull(2, Types.CHAR);
            } else {
                ps.setString(2, ct.getMaVatTuPhuTung());
            }
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());

            if (ct.getMaTienCong() == null || ct.getMaTienCong().trim().isEmpty()) {
                ps.setNull(5, Types.CHAR);
            } else {
                ps.setString(5, ct.getMaTienCong());
            }

            ps.setDouble(6, ct.getThanhTien());
            ps.setDouble(7, ct.getSoTienCong());
            ps.setString(8, ct.getMaChiTietSuaChuaXe());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maChiTietSuaChuaXe) {
        String sql = "DELETE FROM CHITIETSUACHUAXE WHERE MaChiTietSuaChuaXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maChiTietSuaChuaXe);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private ChiTietSuaChuaXe mapResultSetToChiTiet(ResultSet rs) throws SQLException {
        ChiTietSuaChuaXe ct = new ChiTietSuaChuaXe();

        ct.setMaChiTietSuaChuaXe(rs.getString("MaChiTietSuaChuaXe"));
        ct.setMaSuaChuaXe(rs.getString("Ma_SuaChuaXe"));
        ct.setNoiDung(rs.getString("NoiDung"));
        ct.setMaVatTuPhuTung(rs.getString("Ma_VatTuPhuTung"));
        ct.setSoLuong(rs.getInt("SoLuong"));
        ct.setDonGia(rs.getDouble("DonGia"));
        ct.setMaTienCong(rs.getString("Ma_TienCong"));
        ct.setThanhTien(rs.getDouble("ThanhTien"));
        ct.setSoTienCong(rs.getDouble("SoTienCong"));

        return ct;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}

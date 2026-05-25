package DAO;

import Config.DBConnection;
import MODEL.SuaChuaXe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuaChuaXeDAO {

    public boolean insert(SuaChuaXe sc) {
        String sql = """
            INSERT INTO SUACHUAXE
            (MaSuaChuaXe, Ma_TiepNhanXe, NgaySuaChua, ThanhTien)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sc.getMaSuaChuaXe());
            ps.setString(2, sc.getMaTiepNhanXe());
            ps.setDate(3, sc.getNgaySuaChua());
            ps.setDouble(4, sc.getThanhTien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<SuaChuaXe> getAll() {
        List<SuaChuaXe> list = new ArrayList<>();

        String sql = "SELECT * FROM SUACHUAXE ORDER BY MaSuaChuaXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SuaChuaXe sc = new SuaChuaXe();

                sc.setMaSuaChuaXe(rs.getString("MaSuaChuaXe"));
                sc.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
                sc.setNgaySuaChua(rs.getDate("NgaySuaChua"));
                sc.setThanhTien(rs.getDouble("ThanhTien"));

                list.add(sc);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public SuaChuaXe getById(String maSuaChuaXe) {
        String sql = "SELECT * FROM SUACHUAXE WHERE MaSuaChuaXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSuaChuaXe);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                SuaChuaXe sc = new SuaChuaXe();

                sc.setMaSuaChuaXe(rs.getString("MaSuaChuaXe"));
                sc.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
                sc.setNgaySuaChua(rs.getDate("NgaySuaChua"));
                sc.setThanhTien(rs.getDouble("ThanhTien"));

                return sc;
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(SuaChuaXe sc) {
        String sql = """
            UPDATE SUACHUAXE
            SET Ma_TiepNhanXe = ?,
                NgaySuaChua = ?,
                ThanhTien = ?
            WHERE MaSuaChuaXe = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sc.getMaTiepNhanXe());
            ps.setDate(2, sc.getNgaySuaChua());
            ps.setDouble(3, sc.getThanhTien());
            ps.setString(4, sc.getMaSuaChuaXe());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maSuaChuaXe) {
        String sql = "DELETE FROM SUACHUAXE WHERE MaSuaChuaXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSuaChuaXe);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
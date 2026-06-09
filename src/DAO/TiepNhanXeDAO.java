package DAO;

import Config.DBConnection;
import MODEL.TiepNhanXe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TiepNhanXeDAO {

    public boolean insert(TiepNhanXe tnx) {
        String sql = """
            INSERT INTO TIEPNHANXE
            (MaTiepNhanXe, Ma_KhachHang, BienSoXe, Ma_HieuXe, NgayTiepNhan, TienNo, TrangThai)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tnx.getMaTiepNhanXe());
            ps.setString(2, tnx.getMaKhachHang());
            ps.setString(3, tnx.getBienSoXe());
            ps.setString(4, tnx.getMaHieuXe());
            ps.setDate(5, tnx.getNgayTiepNhan());
            ps.setDouble(6, tnx.getTienNo());
            ps.setString(7, TiepNhanXe.normalizeStatus(tnx.getTrangThai()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<TiepNhanXe> getALL() {
        List<TiepNhanXe> list = new ArrayList<>();
        String sql = "SELECT * FROM TIEPNHANXE ORDER BY MaTiepNhanXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToTiepNhanXe(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public TiepNhanXe getById(String maTiepNhanXe) {
        String sql = "SELECT * FROM TIEPNHANXE WHERE MaTiepNhanXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTiepNhanXe);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToTiepNhanXe(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(TiepNhanXe tnx) {
        String sql = """
            UPDATE TIEPNHANXE
            SET Ma_KhachHang = ?,
                BienSoXe = ?,
                Ma_HieuXe = ?,
                NgayTiepNhan = ?,
                TienNo = ?,
                TrangThai = ?
            WHERE MaTiepNhanXe = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tnx.getMaKhachHang());
            ps.setString(2, tnx.getBienSoXe());
            ps.setString(3, tnx.getMaHieuXe());
            ps.setDate(4, tnx.getNgayTiepNhan());
            ps.setDouble(5, tnx.getTienNo());
            ps.setString(6, TiepNhanXe.normalizeStatus(tnx.getTrangThai()));
            ps.setString(7, tnx.getMaTiepNhanXe());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maTiepNhanXe) {
        String sql = "DELETE FROM TIEPNHANXE WHERE MaTiepNhanXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTiepNhanXe);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<TiepNhanXe> search(String keyword) {
        List<TiepNhanXe> list = new ArrayList<>();
        String sql = """
            SELECT * FROM TIEPNHANXE
            WHERE MaTiepNhanXe LIKE ?
               OR Ma_KhachHang LIKE ?
               OR BienSoXe LIKE ?
               OR Ma_HieuXe LIKE ?
               OR TrangThai LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);
            ps.setString(5, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToTiepNhanXe(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public List<String> getAllIds() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaTiepNhanXe FROM TIEPNHANXE ORDER BY MaTiepNhanXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("MaTiepNhanXe").trim());
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public boolean updateTienNo(String maTiepNhanXe, double tienNoMoi) {
        String sql = "UPDATE TIEPNHANXE SET TienNo = ? WHERE MaTiepNhanXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, tienNoMoi);
            ps.setString(2, maTiepNhanXe);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean updateTrangThai(String maTiepNhanXe, String trangThai) {
        String sql = "UPDATE TIEPNHANXE SET TrangThai = ? WHERE MaTiepNhanXe = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, TiepNhanXe.normalizeStatus(trangThai));
            ps.setString(2, maTiepNhanXe);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean existsActivePlate(String bienSoXe, String excludeMaTiepNhanXe) {
        String sql = """
            SELECT COUNT(*)
            FROM TIEPNHANXE
            WHERE UPPER(LTRIM(RTRIM(BienSoXe))) = UPPER(LTRIM(RTRIM(?)))
            AND TrangThai IN (?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bienSoXe.trim().toUpperCase());
            ps.setString(2, TiepNhanXe.STATUS_RECEIVED);
            ps.setString(3, TiepNhanXe.STATUS_REPAIRING);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public int countByDate(java.sql.Date ngayTiepNhan) {
        String sql = "SELECT COUNT(*) FROM TIEPNHANXE WHERE NgayTiepNhan = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, ngayTiepNhan);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return 0;
    }

    private TiepNhanXe mapResultSetToTiepNhanXe(ResultSet rs) throws SQLException {
        TiepNhanXe tnx = new TiepNhanXe();

        tnx.setMaTiepNhanXe(rs.getString("MaTiepNhanXe"));
        tnx.setMaKhachHang(rs.getString("Ma_KhachHang"));
        tnx.setBienSoXe(rs.getString("BienSoXe"));
        tnx.setMaHieuXe(rs.getString("Ma_HieuXe"));
        tnx.setNgayTiepNhan(rs.getDate("NgayTiepNhan"));
        tnx.setTienNo(rs.getDouble("TienNo"));
        tnx.setTrangThai(getStringIfPresent(rs, "TrangThai"));

        return tnx;
    }

    private String getStringIfPresent(ResultSet rs, String columnName) {
        try {
            return TiepNhanXe.normalizeStatus(rs.getString(columnName));
        } catch (SQLException e) {
            return TiepNhanXe.STATUS_RECEIVED;
        }
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}

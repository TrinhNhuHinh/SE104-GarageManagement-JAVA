package DAO;

import Config.DBConnection;
import MODEL.VatTuPhuTung;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VatTuPhuTungDAO {

    public boolean insert(VatTuPhuTung vt) {
        String sql = """
            INSERT INTO VATTUPHUTUNG
            (MaVatTuPhuTung, TenVatTuPhuTung, DonGiaVatTuPhuTung, SoLuongVatTuPhuTung, DonViTinh)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vt.getMaVatTuPhuTung());
            ps.setString(2, vt.getTenVatTuPhuTung());
            ps.setDouble(3, vt.getDonGiaVatTuPhuTung());
            ps.setInt(4, vt.getSoLuongVatTuPhuTung());
            ps.setString(5, vt.getDonViTinh());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<VatTuPhuTung> getAll() {
        List<VatTuPhuTung> list = new ArrayList<>();

        String sql = "SELECT * FROM VATTUPHUTUNG";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToVatTu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public VatTuPhuTung getById(String maVatTuPhuTung) {
        String sql = "SELECT * FROM VATTUPHUTUNG WHERE MaVatTuPhuTung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVatTuPhuTung);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToVatTu(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean update(VatTuPhuTung vt) {
        String sql = """
            UPDATE VATTUPHUTUNG
            SET TenVatTuPhuTung = ?,
                DonGiaVatTuPhuTung = ?,
                SoLuongVatTuPhuTung = ?,
                DonViTinh = ?
            WHERE MaVatTuPhuTung = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vt.getTenVatTuPhuTung());
            ps.setDouble(2, vt.getDonGiaVatTuPhuTung());
            ps.setInt(3, vt.getSoLuongVatTuPhuTung());
            ps.setString(4, vt.getDonViTinh());
            ps.setString(5, vt.getMaVatTuPhuTung());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean delete(String maVatTuPhuTung) {
        String sql = "DELETE FROM VATTUPHUTUNG WHERE MaVatTuPhuTung = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVatTuPhuTung);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public List<VatTuPhuTung> search(String keyword) {
        List<VatTuPhuTung> list = new ArrayList<>();

        String sql = """
            SELECT * FROM VATTUPHUTUNG
            WHERE MaVatTuPhuTung LIKE ?
               OR TenVatTuPhuTung LIKE ?
               OR DonViTinh LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToVatTu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public List<String> getAllIds() {
        List<String> list = new ArrayList<>();

        String sql = "SELECT MaVatTuPhuTung FROM VATTUPHUTUNG ORDER BY MaVatTuPhuTung";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("MaVatTuPhuTung").trim());
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public boolean updateSoLuong(String maVatTuPhuTung, int soLuongMoi) {
        String sql = """
            UPDATE VATTUPHUTUNG
            SET SoLuongVatTuPhuTung = ?
            WHERE MaVatTuPhuTung = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuongMoi);
            ps.setString(2, maVatTuPhuTung);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean updateQuantity(String maVatTuPhuTung, int newQuantity) {
        return updateSoLuong(maVatTuPhuTung, newQuantity);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM VATTUPHUTUNG";

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

    public int countLowStock(int minQuantity) {
        String sql = "SELECT COUNT(*) FROM VATTUPHUTUNG WHERE SoLuongVatTuPhuTung <= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, minQuantity);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return 0;
    }

    public double getInventoryValue() {
        String sql = """
            SELECT SUM(DonGiaVatTuPhuTung * SoLuongVatTuPhuTung)
            FROM VATTUPHUTUNG
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return 0;
    }

    private VatTuPhuTung mapResultSetToVatTu(ResultSet rs) throws SQLException {
        VatTuPhuTung vt = new VatTuPhuTung();

        vt.setMaVatTuPhuTung(rs.getString("MaVatTuPhuTung"));
        vt.setTenVatTuPhuTung(rs.getString("TenVatTuPhuTung"));
        vt.setDonGiaVatTuPhuTung(rs.getDouble("DonGiaVatTuPhuTung"));
        vt.setSoLuongVatTuPhuTung(rs.getInt("SoLuongVatTuPhuTung"));
        vt.setDonViTinh(rs.getString("DonViTinh"));

        return vt;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
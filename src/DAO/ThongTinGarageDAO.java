package DAO;

import Config.DBConnection;
import MODEL.ThongTinGarage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThongTinGarageDAO {

    public ThongTinGarage get() {
        String sql = "SELECT * FROM THONGTINGARAGE WHERE Id = 'GARAGE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToThongTinGarage(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public boolean insert(ThongTinGarage tt) {
        String sql = """
            INSERT INTO THONGTINGARAGE
            (Id, SoLuongXeToiDa, TongSoHieuXe, SoTienThuSoVoiSoTienNo, SoLuongVatTuToiDa, SoLuongTienCongToiDa)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tt.getId());
            ps.setInt(2, tt.getSoLuongXeToiDa());
            ps.setInt(3, tt.getTongSoHieuXe());
            ps.setDouble(4, tt.getSoTienThuSoVoiSoTienNo());
            ps.setInt(5, tt.getSoLuongVatTuToiDa());
            ps.setInt(6, tt.getSoLuongTienCongToiDa());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    public boolean update(ThongTinGarage tt) {
        String sql = """
            UPDATE THONGTINGARAGE
            SET SoLuongXeToiDa = ?,
                TongSoHieuXe = ?,
                SoTienThuSoVoiSoTienNo = ?,
                SoLuongVatTuToiDa = ?,
                SoLuongTienCongToiDa = ?
            WHERE Id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tt.getSoLuongXeToiDa());
            ps.setInt(2, tt.getTongSoHieuXe());
            ps.setDouble(3, tt.getSoTienThuSoVoiSoTienNo());
            ps.setInt(4, tt.getSoLuongVatTuToiDa());
            ps.setInt(5, tt.getSoLuongTienCongToiDa());
            ps.setString(6, tt.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private ThongTinGarage mapResultSetToThongTinGarage(ResultSet rs) throws SQLException {
        ThongTinGarage tt = new ThongTinGarage();

        tt.setId(rs.getString("Id"));
        tt.setSoLuongXeToiDa(rs.getInt("SoLuongXeToiDa"));
        tt.setTongSoHieuXe(rs.getInt("TongSoHieuXe"));
        tt.setSoTienThuSoVoiSoTienNo(rs.getDouble("SoTienThuSoVoiSoTienNo"));
        tt.setSoLuongVatTuToiDa(rs.getInt("SoLuongVatTuToiDa"));
        tt.setSoLuongTienCongToiDa(rs.getInt("SoLuongTienCongToiDa"));

        return tt;
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}
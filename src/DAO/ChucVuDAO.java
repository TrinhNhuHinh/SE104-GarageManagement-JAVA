package DAO;

import Config.DBConnection;
import MODEL.ChucVu;
import MODEL.QuyenHan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChucVuDAO {

    public List<ChucVu> getAllRoles() {
        List<ChucVu> roles = new ArrayList<>();
        String sql = "SELECT MaChucVu, TenChucVu FROM CHUCVU ORDER BY MaChucVu";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roles.add(new ChucVu(rs.getString("MaChucVu"), rs.getString("TenChucVu")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public ChucVu getRoleById(String roleId) {
        String sql = "SELECT MaChucVu, TenChucVu FROM CHUCVU WHERE MaChucVu = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roleId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ChucVu(rs.getString("MaChucVu"), rs.getString("TenChucVu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertRole(ChucVu role) {
        String sql = "INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getMaChucVu());
            ps.setString(2, role.getTenChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateRole(ChucVu role) {
        String sql = "UPDATE CHUCVU SET TenChucVu = ? WHERE MaChucVu = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role.getTenChucVu());
            ps.setString(2, role.getMaChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRole(String roleId) {
        String deleteDetailsSql = "DELETE FROM CHITIETCHUCVU WHERE Ma_ChucVu = ?";
        String deleteRoleSql = "DELETE FROM CHUCVU WHERE MaChucVu = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDetails = conn.prepareStatement(deleteDetailsSql);
                 PreparedStatement psRole = conn.prepareStatement(deleteRoleSql)) {

                psDetails.setString(1, roleId);
                psDetails.executeUpdate();

                psRole.setString(1, roleId);
                boolean deleted = psRole.executeUpdate() > 0;
                conn.commit();
                return deleted;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<QuyenHan> getAllPermissions() {
        List<QuyenHan> permissions = new ArrayList<>();
        String sql = "SELECT MaQuyenHan, TenQuyenHan, NoiDungQuyenHan FROM QUYENHAN ORDER BY MaQuyenHan";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                permissions.add(new QuyenHan(
                        rs.getString("MaQuyenHan"),
                        rs.getString("TenQuyenHan"),
                        rs.getString("NoiDungQuyenHan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissions;
    }

    public Set<String> getPermissionIdsByRole(String roleId) {
        Set<String> permissionIds = new HashSet<>();
        String sql = "SELECT Ma_QuyenHan FROM CHITIETCHUCVU WHERE Ma_ChucVu = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roleId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                permissionIds.add(rs.getString("Ma_QuyenHan").trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissionIds;
    }

    public boolean updateRolePermissions(String roleId, Set<String> permissionIds) {
        String deleteSql = "DELETE FROM CHITIETCHUCVU WHERE Ma_ChucVu = ?";
        String insertSql = "INSERT INTO CHITIETCHUCVU (MaChiTietChucVu, Ma_ChucVu, Ma_QuyenHan) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDelete = conn.prepareStatement(deleteSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertSql)) {

                psDelete.setString(1, roleId);
                psDelete.executeUpdate();

                int index = 1;
                for (String permissionId : permissionIds) {
                    psInsert.setString(1, buildDetailId(roleId, index++));
                    psInsert.setString(2, roleId);
                    psInsert.setString(3, permissionId);
                    psInsert.addBatch();
                }

                psInsert.executeBatch();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getNextRoleId() {
        String sql = "SELECT MaChucVu FROM CHUCVU";
        int max = 0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("MaChucVu");
                if (id != null && id.trim().startsWith("CV")) {
                    try {
                        max = Math.max(max, Integer.parseInt(id.trim().substring(2)));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return String.format("CV%02d", max + 1);
    }

    private String buildDetailId(String roleId, int index) {
        String id = ("CT" + roleId.trim() + String.format("%03d", index));
        return id.length() > 10 ? id.substring(0, 10) : id;
    }
}

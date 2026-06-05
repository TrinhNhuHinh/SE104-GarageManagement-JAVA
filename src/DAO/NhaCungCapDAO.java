package DAO;

import Config.DBConnection;
import MODEL.NhaCungCap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {
    
    //insert
    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NHACUNGCAP VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNhaCungCap());
            ps.setString(2, ncc.getTenNhaCungCap());
            ps.setString(3, ncc.getSoDienThoaiNhaCungCap());
            ps.setString(4, ncc.getEmailNhaCungCap());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //getAll
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NHACUNGCAP";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                ncc.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                ncc.setSoDienThoaiNhaCungCap(rs.getString("SoDienThoaiNhaCungCap"));
                ncc.setEmailNhaCungCap(rs.getString("EmailNhaCungCap"));
                list.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    //getById
    public NhaCungCap getById(String maNhaCungCap) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhaCungCap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                ncc.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                ncc.setSoDienThoaiNhaCungCap(rs.getString("SoDienThoaiNhaCungCap"));
                ncc.setEmailNhaCungCap(rs.getString("EmailNhaCungCap"));
                return ncc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NhaCungCap getByName(String tenNhaCungCap) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE TenNhaCungCap = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenNhaCungCap);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                NhaCungCap ncc = new NhaCungCap();
                ncc.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
                ncc.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                ncc.setSoDienThoaiNhaCungCap(rs.getString("SoDienThoaiNhaCungCap"));
                ncc.setEmailNhaCungCap(rs.getString("EmailNhaCungCap"));
                return ncc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    //update
    public boolean update(NhaCungCap ncc) {
        String sql = "UPDATE NHACUNGCAP SET TenNhaCungCap = ?, SoDienThoaiNhaCungCap = ?, EmailNhaCungCap = ? WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNhaCungCap());
            ps.setString(2, ncc.getSoDienThoaiNhaCungCap());
            ps.setString(3, ncc.getEmailNhaCungCap());
            ps.setString(4, ncc.getMaNhaCungCap());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //delete
    public boolean delete(String maNhaCungCap) {
        String sql = "DELETE FROM NHACUNGCAP WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNhaCungCap);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

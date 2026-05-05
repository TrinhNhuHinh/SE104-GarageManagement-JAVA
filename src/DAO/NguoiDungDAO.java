package DAO;

import Config.DBConnection;
import MODEL.NguoiDung; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {
    
    // insert
    public boolean insert(NguoiDung nd) {
        String sql = "INSERT INTO NGUOIDUNG (MaNguoiDung, TenTaiKhoan, MatKhau, Ma_ChucVu) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getMaNguoiDung());
            ps.setString(2, nd.getTenTaiKhoan());
            ps.setString(3, nd.getMatKhau());
            ps.setString(4, nd.getMaChucVu());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // getAll
    public List<NguoiDung> getAll() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM NGUOIDUNG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getString("MaNguoiDung"));
                nd.setTenTaiKhoan(rs.getString("TenTaiKhoan"));
                nd.setMatKhau(rs.getString("MatKhau"));
                nd.setMaChucVu(rs.getString("Ma_ChucVu"));
                list.add(nd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // getById
    public NguoiDung getById(String maNguoiDung) {
        String sql = "SELECT * FROM NGUOIDUNG WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNguoiDung);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getString("MaNguoiDung"));
                nd.setTenTaiKhoan(rs.getString("TenTaiKhoan"));
                nd.setMatKhau(rs.getString("MatKhau"));
                nd.setMaChucVu(rs.getString("Ma_ChucVu"));
                return nd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // update
    public boolean update(NguoiDung nd) {
        String sql = "UPDATE NGUOIDUNG SET TenTaiKhoan = ?, MatKhau = ?, Ma_ChucVu = ? WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getTenTaiKhoan());
            ps.setString(2, nd.getMatKhau());
            ps.setString(3, nd.getMaChucVu());
            ps.setString(4, nd.getMaNguoiDung());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // delete
    public boolean delete(String maNguoiDung) {
        String sql = "DELETE FROM NGUOIDUNG WHERE MaNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNguoiDung);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // checkLogin (Hỗ trợ form đăng nhập)
    public NguoiDung checkLogin(String username, String password) {
        String sql = "SELECT * FROM NGUOIDUNG WHERE TenTaiKhoan = ? AND MatKhau = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getString("MaNguoiDung"));
                nd.setTenTaiKhoan(rs.getString("TenTaiKhoan"));
                nd.setMatKhau(rs.getString("MatKhau"));
                nd.setMaChucVu(rs.getString("Ma_ChucVu"));
                return nd; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    //Check tồn tại
    // Hàm kiểm tra xem tên tài khoản đã có ai xài chưa
    public boolean checkTaiKhoanTonTai(String username) {
    String sql = "SELECT * FROM NGUOIDUNG WHERE TenTaiKhoan = ?";
    try (Connection conn = Config.DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return true; 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; 
}
    
    // Hàm tự động sinh mã người dùng mới (ND01, ND02,...)
    public String getMaNguoiDungTiepTheo() {
        String sql = "SELECT MaNguoiDung FROM NGUOIDUNG";
        int maxNumber = 0; 

        try (java.sql.Connection conn = Config.DBConnection.getConnection(); // Ní tự chỉnh lại chỗ lấy Connection cho khớp nha
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String maND = rs.getString("MaNguoiDung");
                // Kiểm tra xem mã có chữ "ND" ở đầu không
                if (maND != null && maND.startsWith("ND")) {
                    maND = maND.trim();
                    try {
                        int so = Integer.parseInt(maND.substring(2));
                        if (so > maxNumber) {
                            maxNumber = so; 
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        maxNumber++;

        return String.format("ND%02d", maxNumber);
    }
}
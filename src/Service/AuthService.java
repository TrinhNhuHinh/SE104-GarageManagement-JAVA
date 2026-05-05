/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.NguoiDungDAO;
import MODEL.NguoiDung;

/**
 *
 * @author hinh
 */
public class AuthService {
    private NguoiDungDAO nguoiDungDAO;
    
    // Bất kỳ màn hình nào gọi AuthService.currentUser đều biết ai đang đăng nhập
    public static NguoiDung currentUser = null;
    
    //Constructor
    public AuthService(){
        this.nguoiDungDAO = new NguoiDungDAO();
    }
    
    //Xử lý đăng nhập
    public NguoiDung login(String userName, String password){
        //trống
        if(userName.trim().isEmpty() || password.trim().isEmpty()){
            return null;
        }
        NguoiDung nd = nguoiDungDAO.checkLogin(userName, password);
        //Nếu đăng nhập thành công thì lưu thông tin
        if (nd != null) {
            currentUser = nd; 
        }
        
        return nd;
    }
    
    //Xử lý đăng ký
    public String register(String userName, String password, String confirmPass){
        //kiểm tra rỗng
        if(userName.trim().isEmpty() || password.trim() .isEmpty() || confirmPass.trim().isEmpty()){
            return "Tài khoản và mật khẩu không được để trống!";
        }
        //Mật khẩu không khớp nhau
        if(!password.equals(confirmPass)){
            return "Mật khẩu không khớp nhau";
        }
        //kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự!";
        }
        //kiểm tra trùng tên tài khoản
        if (nguoiDungDAO.checkTaiKhoanTonTai(userName)) {
            return "Tài khoản '" + userName + "' đã tồn tại! Vui lòng chọn tên khác.";
        }
        
        //tạo tài khoản mới
        NguoiDung newUser = new NguoiDung();
        String newId = nguoiDungDAO.getMaNguoiDungTiepTheo(); 
        newUser.setMaNguoiDung(newId);
        newUser.setTenTaiKhoan(userName);
        newUser.setMatKhau(password);
        
        newUser.setMaChucVu("CV02");
        
        boolean isSuccess = nguoiDungDAO.insert(newUser);
        if (isSuccess) {
            return "SUCCESS"; // Trả về đúng chữ SUCCESS để Controller nhận biết
        } else {
            return "Lỗi hệ thống! Không thể lưu dữ liệu vào cơ sở dữ liệu.";
        }
    }
    
    //Hàm đăng xuất;
    public static void logOut(){
        currentUser = null;
    }
    
}

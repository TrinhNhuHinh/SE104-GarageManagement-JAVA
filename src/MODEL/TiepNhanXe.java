    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package MODEL;

    import java.sql.Date;

    /**
     *
     * @author hinh
     */
    public class TiepNhanXe {
        //Thuộc tính
        private String maTiepNhanXe;
        private String maKhachHang; //Khóa ngoại tham chiếu KhachHang
        private String bienSoXe;
        private String maHieuXe; //Khóa ngoại tham chiếu HieuXe
        private Date ngayTiepNhan;
        private double tienNo;

        //Constructor 
        public TiepNhanXe(){

        }

        //Constructor 
        public TiepNhanXe(String maTiepNhanXe, String maKhachHang, String bienSoXe, String maHieuXe, Date ngayTiepNhan, double tienNo){
            this.maTiepNhanXe = maTiepNhanXe;
            this.maKhachHang = maKhachHang;
            this.maHieuXe = maHieuXe;
            this.bienSoXe = bienSoXe;
            this.ngayTiepNhan = ngayTiepNhan;
            this.tienNo = tienNo;
        }

        //Getter
        public String getMaTiepNhanXe() {
            return maTiepNhanXe;
        }

        public String getMaKhachHang() {
            return maKhachHang;
        }

        public String getBienSoXe() {
            return bienSoXe;
        }

        public String getMaHieuXe() {
            return maHieuXe;
        }

        public Date getNgayTiepNhan() {
            return ngayTiepNhan;
        }

    public double getTienNo() {
        return tienNo;
    }

        //Setter
        public void setMaTiepNhanXe(String maTiepNhanXe) {
            this.maTiepNhanXe = maTiepNhanXe;
        }

        public void setMaKhachHang(String maKhachHang) {
            this.maKhachHang = maKhachHang;
        }

        public void setBienSoXe(String bienSoXe) {
            this.bienSoXe = bienSoXe;
        }

        public void setMaHieuXe(String maHieuXe) {
            this.maHieuXe = maHieuXe;
        }

        public void setNgayTiepNhan(Date ngayTiepNhan) {
            this.ngayTiepNhan = ngayTiepNhan;
        }

    public void setTienNo(double tienNo) {
        this.tienNo = tienNo;
    }

        //toString
        @Override
        public String toString() {
            return "TiepNhanXe{" + "maTiepNhanXe=" + maTiepNhanXe + ", maKhachHang=" + maKhachHang + ", bienSoXe=" + bienSoXe + ", maHieuXe=" + maHieuXe + ", ngayTiepNhan=" + ngayTiepNhan + '}';
        }


    }

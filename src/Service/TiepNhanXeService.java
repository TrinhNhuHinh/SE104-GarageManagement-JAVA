package Service;

import DAO.KhachHangDAO;
import DAO.ThongTinGarageDAO;
import DAO.TiepNhanXeDAO;
import MODEL.KhachHang;
import MODEL.ThongTinGarage;
import MODEL.TiepNhanXe;
import java.util.List;

public class TiepNhanXeService {

    private final TiepNhanXeDAO tiepNhanXeDAO;
    private final KhachHangDAO khachHangDAO;
    private final ThongTinGarageDAO thongTinGarageDAO;

    public TiepNhanXeService() {
        this.tiepNhanXeDAO = new TiepNhanXeDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.thongTinGarageDAO = new ThongTinGarageDAO();
    }

    public List<TiepNhanXe> getAll() {
        return tiepNhanXeDAO.getALL();
    }

    public boolean add(TiepNhanXe tnx) {
        return addWithCustomer(tnx, null);
    }

    public boolean addWithCustomer(TiepNhanXe tnx, KhachHang khachHangMoi) {
        if (!isValid(tnx)) {
            return false;
        }

        if (tiepNhanXeDAO.getById(tnx.getMaTiepNhanXe()) != null) {
            return false;
        }

        if (isOverDailyLimit(tnx)) {
            return false;
        }

        KhachHang khachHangDaCo = khachHangDAO.getById(tnx.getMaKhachHang());

        if (khachHangDaCo == null) {
            if (!isValidCustomer(khachHangMoi)) {
                return false;
            }

            boolean insertedCustomer = khachHangDAO.insert(khachHangMoi);

            if (!insertedCustomer) {
                return false;
            }
        }

        return tiepNhanXeDAO.insert(tnx);
    }

    public boolean update(TiepNhanXe tnx) {
        if (!isValid(tnx)) {
            return false;
        }

        if (tiepNhanXeDAO.getById(tnx.getMaTiepNhanXe()) == null) {
            return false;
        }

        return tiepNhanXeDAO.update(tnx);
    }

    public boolean delete(String maTiepNhanXe) {
        if (maTiepNhanXe == null || maTiepNhanXe.trim().isEmpty()) {
            return false;
        }

        return tiepNhanXeDAO.delete(maTiepNhanXe.trim());
    }

    public List<TiepNhanXe> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return tiepNhanXeDAO.search(keyword.trim());
    }

    private boolean isOverDailyLimit(TiepNhanXe tnx) {
        ThongTinGarage tt = thongTinGarageDAO.get();

        int maxXeTrongNgay = 30;

        if (tt != null) {
            maxXeTrongNgay = tt.getSoLuongXeToiDa();
        }

        int soXeDaTiepNhan = tiepNhanXeDAO.countByDate(tnx.getNgayTiepNhan());

        return soXeDaTiepNhan >= maxXeTrongNgay;
    }

    private boolean isValid(TiepNhanXe tnx) {
        if (tnx == null) return false;
        if (tnx.getMaTiepNhanXe() == null || tnx.getMaTiepNhanXe().trim().isEmpty()) return false;
        if (tnx.getMaKhachHang() == null || tnx.getMaKhachHang().trim().isEmpty()) return false;
        if (tnx.getBienSoXe() == null || tnx.getBienSoXe().trim().isEmpty()) return false;
        if (tnx.getMaHieuXe() == null || tnx.getMaHieuXe().trim().isEmpty()) return false;
        if (tnx.getNgayTiepNhan() == null) return false;
        if (tnx.getTienNo() < 0) return false;

        return true;
    }

    private boolean isValidCustomer(KhachHang kh) {
        if (kh == null) return false;
        if (kh.getMaKhachHang() == null || kh.getMaKhachHang().trim().isEmpty()) return false;
        if (kh.getTenKhachHang() == null || kh.getTenKhachHang().trim().isEmpty()) return false;
        if (kh.getDiaChiKhachHang() == null || kh.getDiaChiKhachHang().trim().isEmpty()) return false;
        if (kh.getSoDienThoaiKhachHang() == null || kh.getSoDienThoaiKhachHang().trim().isEmpty()) return false;

        return true;
    }
}
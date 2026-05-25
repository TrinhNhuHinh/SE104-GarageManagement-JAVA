package Service;

import DAO.KhachHangDAO;
import DAO.PhieuThuTienDAO;
import DAO.TiepNhanXeDAO;
import MODEL.KhachHang;
import MODEL.PhieuThuTien;
import MODEL.TiepNhanXe;
import java.sql.Date;
import java.util.List;

public class PhieuThuTienService {

    private final PhieuThuTienDAO phieuThuDAO = new PhieuThuTienDAO();
    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public List<PhieuThuTien> getAll() {
        return phieuThuDAO.getAll();
    }

    public List<String> getAllIntakeIds() {
        return tiepNhanXeDAO.getAllIds();
    }

    public List<PhieuThuTien> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return phieuThuDAO.search(keyword.trim());
    }

    public double getCurrentDebt(String maTiepNhanXe) {
        if (maTiepNhanXe == null || maTiepNhanXe.trim().isEmpty()) {
            return 0;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (tnx == null) {
            return 0;
        }

        return tnx.getTienNo();
    }

    public boolean add(String maPhieuThu, String maTiepNhanXe, Date ngayThuTien, double soTienThu) {
        if (!isValid(maPhieuThu, maTiepNhanXe, ngayThuTien, soTienThu)) {
            return false;
        }

        if (phieuThuDAO.getById(maPhieuThu) != null) {
            return false;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (tnx == null) {
            return false;
        }

        if (soTienThu > tnx.getTienNo()) {
            return false;
        }

        KhachHang kh = khachHangDAO.getById(tnx.getMaKhachHang());

        String phone = "";

        if (kh != null) {
            phone = kh.getSoDienThoaiKhachHang();
        }

        PhieuThuTien pt = new PhieuThuTien(
                maPhieuThu,
                maTiepNhanXe,
                ngayThuTien,
                tnx.getBienSoXe(),
                "",
                phone,
                soTienThu
        );

        boolean inserted = phieuThuDAO.insert(pt);

        if (!inserted) {
            return false;
        }

        return tiepNhanXeDAO.updateTienNo(maTiepNhanXe, tnx.getTienNo() - soTienThu);
    }

    public boolean update(String maPhieuThu, Date ngayThuTien, double soTienThuMoi) {
        PhieuThuTien old = phieuThuDAO.getById(maPhieuThu);

        if (old == null || ngayThuTien == null || soTienThuMoi <= 0) {
            return false;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(old.getMaTiepNhanXe());

        if (tnx == null) {
            return false;
        }

        double debtBeforeOldReceipt = tnx.getTienNo() + old.getSoTienThu();

        if (soTienThuMoi > debtBeforeOldReceipt) {
            return false;
        }

        PhieuThuTien newPt = new PhieuThuTien(
                old.getMaPhieuThuTien(),
                old.getMaTiepNhanXe(),
                ngayThuTien,
                old.getBienSoXe(),
                old.getEmail(),
                old.getSoDienThoai(),
                soTienThuMoi
        );

        boolean updated = phieuThuDAO.update(newPt);

        if (!updated) {
            return false;
        }

        return tiepNhanXeDAO.updateTienNo(old.getMaTiepNhanXe(), debtBeforeOldReceipt - soTienThuMoi);
    }

    public boolean delete(String maPhieuThu) {
        if (maPhieuThu == null || maPhieuThu.trim().isEmpty()) {
            return false;
        }

        PhieuThuTien old = phieuThuDAO.getById(maPhieuThu);

        if (old == null) {
            return false;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(old.getMaTiepNhanXe());

        if (tnx == null) {
            return false;
        }

        boolean restored = tiepNhanXeDAO.updateTienNo(
                old.getMaTiepNhanXe(),
                tnx.getTienNo() + old.getSoTienThu()
        );

        if (!restored) {
            return false;
        }

        return phieuThuDAO.delete(maPhieuThu);
    }

    private boolean isValid(String maPhieuThu, String maTiepNhanXe, Date ngayThuTien, double soTienThu) {
        if (maPhieuThu == null || maPhieuThu.trim().isEmpty()) return false;
        if (maTiepNhanXe == null || maTiepNhanXe.trim().isEmpty()) return false;
        if (ngayThuTien == null) return false;
        if (soTienThu <= 0) return false;

        return true;
    }
}
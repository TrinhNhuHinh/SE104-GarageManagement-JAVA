package Service;

import DAO.KhachHangDAO;
import DAO.PhieuThuTienDAO;
import DAO.SuaChuaXeDAO;
import DAO.TiepNhanXeDAO;
import MODEL.KhachHang;
import MODEL.PhieuThuTien;
import MODEL.SuaChuaXe;
import MODEL.TiepNhanXe;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PhieuThuTienService {

    private final PhieuThuTienDAO phieuThuDAO = new PhieuThuTienDAO();
    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final SuaChuaXeDAO suaChuaXeDAO = new SuaChuaXeDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public List<PhieuThuTien> getAll() {
        return phieuThuDAO.getAll();
    }

    public List<String> getAllRepairIds() {
        List<String> ids = new ArrayList<>();

        for (SuaChuaXe sc : suaChuaXeDAO.getAll()) {
            ids.add(sc.getMaSuaChuaXe().trim());
        }

        return ids;
    }

    public List<PhieuThuTien> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return phieuThuDAO.search(keyword.trim());
    }

    public SuaChuaXe getRepairById(String maSuaChuaXe) {
        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) {
            return null;
        }

        return suaChuaXeDAO.getById(maSuaChuaXe.trim());
    }

    public String getIntakeIdByRepairId(String maSuaChuaXe) {
        SuaChuaXe sc = getRepairById(maSuaChuaXe);

        if (sc == null) {
            return "";
        }

        return sc.getMaTiepNhanXe();
    }

    public double getRepairTotal(String maSuaChuaXe) {
        SuaChuaXe sc = getRepairById(maSuaChuaXe);

        if (sc == null) {
            return 0;
        }

        return sc.getThanhTien();
    }

    public double getPaidByRepairId(String maSuaChuaXe) {
        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) {
            return 0;
        }

        return phieuThuDAO.getTotalPaidByRepairId(maSuaChuaXe.trim());
    }

    public double getRemainingByRepairId(String maSuaChuaXe) {
        double repairTotal = getRepairTotal(maSuaChuaXe);
        double paid = getPaidByRepairId(maSuaChuaXe);

        double remaining = repairTotal - paid;

        if (remaining < 0) {
            return 0;
        }

        return remaining;
    }

    public double getCurrentDebtByRepairId(String maSuaChuaXe) {
        SuaChuaXe sc = getRepairById(maSuaChuaXe);

        if (sc == null) {
            return 0;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(sc.getMaTiepNhanXe());

        if (tnx == null) {
            return 0;
        }

        return tnx.getTienNo();
    }

    public boolean add(String maPhieuThu, String maSuaChuaXe, Date ngayThuTien, double soTienThu) {
        if (!isValid(maPhieuThu, maSuaChuaXe, ngayThuTien, soTienThu)) {
            return false;
        }

        if (phieuThuDAO.getById(maPhieuThu) != null) {
            return false;
        }

        SuaChuaXe sc = suaChuaXeDAO.getById(maSuaChuaXe);

        if (sc == null) {
            return false;
        }

        TiepNhanXe tnx = tiepNhanXeDAO.getById(sc.getMaTiepNhanXe());

        if (tnx == null) {
            return false;
        }

        double remainingRepair = getRemainingByRepairId(maSuaChuaXe);

        if (soTienThu > remainingRepair) {
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
                tnx.getMaTiepNhanXe(),
                maSuaChuaXe,
                ngayThuTien,
                tnx.getBienSoXe(),
                "",
                phone,
                soTienThu
        );

        return phieuThuDAO.insert(pt);
    }

    public boolean update(String maPhieuThu, Date ngayThuTien, double soTienThuMoi) {
        PhieuThuTien old = phieuThuDAO.getById(maPhieuThu);

        if (old == null || ngayThuTien == null || soTienThuMoi <= 0) {
            return false;
        }

        double remainingWithoutOld = getRemainingByRepairId(old.getMaSuaChuaXe()) + old.getSoTienThu();
        double debtBeforeOld = getCurrentDebtByRepairId(old.getMaSuaChuaXe()) + old.getSoTienThu();

        if (soTienThuMoi > remainingWithoutOld) {
            return false;
        }

        if (soTienThuMoi > debtBeforeOld) {
            return false;
        }

        PhieuThuTien newPt = new PhieuThuTien(
                old.getMaPhieuThuTien(),
                old.getMaTiepNhanXe(),
                old.getMaSuaChuaXe(),
                ngayThuTien,
                old.getBienSoXe(),
                old.getEmail(),
                old.getSoDienThoai(),
                soTienThuMoi
        );

        return phieuThuDAO.update(newPt);
    }

    public boolean delete(String maPhieuThu) {
        if (maPhieuThu == null || maPhieuThu.trim().isEmpty()) {
            return false;
        }

        return phieuThuDAO.delete(maPhieuThu.trim());
    }

    private boolean isValid(String maPhieuThu, String maSuaChuaXe, Date ngayThuTien, double soTienThu) {
        if (maPhieuThu == null || maPhieuThu.trim().isEmpty()) return false;
        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) return false;
        if (ngayThuTien == null) return false;
        if (soTienThu <= 0) return false;

        return true;
    }
}
package Service;

import DAO.ChiTietSuaChuaXeDAO;
import DAO.PhieuThuTienDAO;
import DAO.SuaChuaXeDAO;
import DAO.TienCongDAO;
import DAO.TiepNhanXeDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.ChiTietSuaChuaXe;
import MODEL.SuaChuaXe;
import MODEL.TienCong;
import MODEL.TiepNhanXe;
import MODEL.VatTuPhuTung;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SuaChuaXeService {

    private final SuaChuaXeDAO suaChuaXeDAO = new SuaChuaXeDAO();
    private final ChiTietSuaChuaXeDAO chiTietDAO = new ChiTietSuaChuaXeDAO();
    private final VatTuPhuTungDAO vatTuDAO = new VatTuPhuTungDAO();
    private final TiepNhanXeDAO tiepNhanXeDAO = new TiepNhanXeDAO();
    private final TienCongDAO tienCongDAO = new TienCongDAO();
    private final PhieuThuTienDAO phieuThuTienDAO = new PhieuThuTienDAO();

    public List<SuaChuaXe> getAll() {
        return suaChuaXeDAO.getAll();
    }

    public List<TiepNhanXe> getAllIntakes() {
        return tiepNhanXeDAO.getALL();
    }

    public List<String> getAllIntakeIds() {
        return tiepNhanXeDAO.getAllIds();
    }

    public List<String> getAllPartIds() {
        return vatTuDAO.getAllIds();
    }

    public List<VatTuPhuTung> getAllParts() {
        return vatTuDAO.getAll();
    }

    public List<String> getAllLaborIds() {
        return tienCongDAO.getAllIds();
    }

    public List<ChiTietSuaChuaXe> getDetailsByRepairId(String maSuaChuaXe) {
        if (isBlank(maSuaChuaXe)) {
            return List.of();
        }

        return chiTietDAO.getByMaSuaChua(maSuaChuaXe.trim());
    }

    public ChiTietSuaChuaXe getDetailByRepairId(String maSuaChuaXe) {
        List<ChiTietSuaChuaXe> list = getDetailsByRepairId(maSuaChuaXe);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public String getBienSoXeByIntakeId(String maTiepNhanXe) {
        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (tnx == null) {
            return "";
        }

        return safeTrim(tnx.getBienSoXe());
    }

    public String getNoiDungByRepairId(String maSuaChuaXe) {
        List<ChiTietSuaChuaXe> details = getDetailsByRepairId(maSuaChuaXe);

        if (details.isEmpty()) {
            return "";
        }

        StringBuilder content = new StringBuilder();

        for (ChiTietSuaChuaXe detail : details) {
            if (!isBlank(detail.getNoiDung())) {
                if (content.length() > 0) {
                    content.append("; ");
                }

                content.append(detail.getNoiDung().trim());
            }
        }

        return content.toString();
    }

    public String getMaVatTuByRepairId(String maSuaChuaXe) {
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (ct == null) {
            return "";
        }

        return safeTrim(ct.getMaVatTuPhuTung());
    }

    public String getMaTienCongByRepairId(String maSuaChuaXe) {
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (ct == null) {
            return "";
        }

        return safeTrim(ct.getMaTienCong());
    }

    public int getSoLuongByRepairId(String maSuaChuaXe) {
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (ct == null) {
            return 0;
        }

        return ct.getSoLuong();
    }

    public List<SuaChuaXe> search(String keyword) {
        List<SuaChuaXe> all = getAll();

        if (keyword == null || keyword.trim().isEmpty()) {
            return all;
        }

        String key = keyword.trim().toLowerCase();
        List<SuaChuaXe> result = new ArrayList<>();

        for (SuaChuaXe sc : all) {
            String maSuaChua = safeTrim(sc.getMaSuaChuaXe()).toLowerCase();
            String maTiepNhan = safeTrim(sc.getMaTiepNhanXe()).toLowerCase();
            String bienSo = getBienSoXeByIntakeId(sc.getMaTiepNhanXe()).toLowerCase();
            String noiDung = getNoiDungByRepairId(sc.getMaSuaChuaXe()).toLowerCase();

            if (maSuaChua.contains(key)
                    || maTiepNhan.contains(key)
                    || bienSo.contains(key)
                    || noiDung.contains(key)) {
                result.add(sc);
            }
        }

        return result;
    }

    public boolean add(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua,
                       String maVatTu, int soLuong, String maTienCong, String noiDung) {

        if (!isValidHeader(maSuaChuaXe, maTiepNhanXe, ngaySuaChua)
                || !isValidDetail(maVatTu, soLuong, maTienCong, noiDung)) {
            return false;
        }

        if (suaChuaXeDAO.getById(maSuaChuaXe) != null) {
            return false;
        }

        if (tiepNhanXeDAO.getById(maTiepNhanXe) == null) {
            return false;
        }

        if (hasUnfinishedRepairForIntake(maTiepNhanXe)) {
            return false;
        }

        SuaChuaXe sc = new SuaChuaXe(maSuaChuaXe.trim(), maTiepNhanXe.trim(), ngaySuaChua, 0);

        if (!suaChuaXeDAO.insert(sc)) {
            return false;
        }

        boolean insertedDetail = addDetail(maSuaChuaXe, maVatTu, soLuong, maTienCong, noiDung);

        if (!insertedDetail) {
            suaChuaXeDAO.delete(maSuaChuaXe);
            return false;
        }

        return true;
    }

    public boolean addDetail(String maSuaChuaXe, String maVatTu, int soLuong, String maTienCong, String noiDung) {
        if (!isValidDetail(maVatTu, soLuong, maTienCong, noiDung)) {
            return false;
        }

        SuaChuaXe sc = suaChuaXeDAO.getById(maSuaChuaXe);

        if (sc == null || hasAnyReceipt(maSuaChuaXe)) {
            return false;
        }

        DetailMoney money = calculateDetailMoney(maVatTu, soLuong, maTienCong);

        if (money == null) {
            return false;
        }

        ChiTietSuaChuaXe ct = new ChiTietSuaChuaXe(
                buildDetailId(maSuaChuaXe),
                maSuaChuaXe.trim(),
                noiDung.trim(),
                normalizeNullable(maVatTu),
                hasPart(maVatTu) ? soLuong : 0,
                money.partPrice(),
                maTienCong.trim(),
                money.total(),
                money.laborPrice()
        );

        if (!chiTietDAO.insert(ct)) {
            return false;
        }

        if (!reduceStock(maVatTu, soLuong)) {
            chiTietDAO.delete(ct.getMaChiTietSuaChuaXe());
            return false;
        }

        if (!adjustDebt(sc.getMaTiepNhanXe(), money.total())) {
            restoreStock(maVatTu, soLuong);
            chiTietDAO.delete(ct.getMaChiTietSuaChuaXe());
            return false;
        }

        return refreshRepairTotal(maSuaChuaXe);
    }

    public boolean updateHeader(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua) {
        if (!isValidHeader(maSuaChuaXe, maTiepNhanXe, ngaySuaChua)) {
            return false;
        }

        SuaChuaXe oldSc = suaChuaXeDAO.getById(maSuaChuaXe);
        TiepNhanXe newTnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (oldSc == null || newTnx == null) {
            return false;
        }

        String oldIntakeId = safeTrim(oldSc.getMaTiepNhanXe());
        String newIntakeId = safeTrim(maTiepNhanXe);

        if (!oldIntakeId.equalsIgnoreCase(newIntakeId)) {
            if (hasAnyReceipt(maSuaChuaXe)) {
                return false;
            }

            if (!adjustDebt(oldIntakeId, -oldSc.getThanhTien())) {
                return false;
            }

            if (!adjustDebt(newIntakeId, oldSc.getThanhTien())) {
                adjustDebt(oldIntakeId, oldSc.getThanhTien());
                return false;
            }
        }

        SuaChuaXe updated = new SuaChuaXe(maSuaChuaXe.trim(), newIntakeId, ngaySuaChua, oldSc.getThanhTien());
        return suaChuaXeDAO.update(updated);
    }

    public boolean update(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua,
                          String maVatTu, int soLuong, String maTienCong, String noiDung) {

        if (hasAnyReceipt(maSuaChuaXe)) {
            return false;
        }

        if (!updateHeader(maSuaChuaXe, maTiepNhanXe, ngaySuaChua)) {
            return false;
        }

        ChiTietSuaChuaXe oldCt = getDetailByRepairId(maSuaChuaXe);

        if (oldCt != null && !deleteDetail(oldCt.getMaChiTietSuaChuaXe())) {
            return false;
        }

        return addDetail(maSuaChuaXe, maVatTu, soLuong, maTienCong, noiDung);
    }

    public boolean deleteDetail(String maChiTietSuaChuaXe) {
        if (isBlank(maChiTietSuaChuaXe)) {
            return false;
        }

        ChiTietSuaChuaXe ct = chiTietDAO.getById(maChiTietSuaChuaXe.trim());

        if (ct == null || hasAnyReceipt(ct.getMaSuaChuaXe())) {
            return false;
        }

        SuaChuaXe sc = suaChuaXeDAO.getById(ct.getMaSuaChuaXe());

        if (sc == null) {
            return false;
        }

        if (!chiTietDAO.delete(ct.getMaChiTietSuaChuaXe())) {
            return false;
        }

        if (!restoreStock(ct.getMaVatTuPhuTung(), ct.getSoLuong())) {
            return false;
        }

        if (!adjustDebt(sc.getMaTiepNhanXe(), -ct.getThanhTien())) {
            return false;
        }

        return refreshRepairTotal(sc.getMaSuaChuaXe());
    }

    public boolean delete(String maSuaChuaXe) {
        if (isBlank(maSuaChuaXe) || hasAnyReceipt(maSuaChuaXe)) {
            return false;
        }

        SuaChuaXe sc = suaChuaXeDAO.getById(maSuaChuaXe);

        if (sc == null) {
            return false;
        }

        List<ChiTietSuaChuaXe> details = getDetailsByRepairId(maSuaChuaXe);

        for (ChiTietSuaChuaXe detail : details) {
            if (!deleteDetail(detail.getMaChiTietSuaChuaXe())) {
                return false;
            }
        }

        return suaChuaXeDAO.delete(maSuaChuaXe.trim());
    }

    private DetailMoney calculateDetailMoney(String maVatTu, int soLuong, String maTienCong) {
        TienCong tc = tienCongDAO.getById(maTienCong);

        if (tc == null) {
            return null;
        }

        double donGia = 0;

        if (hasPart(maVatTu)) {
            VatTuPhuTung vt = vatTuDAO.getById(maVatTu);

            if (vt == null || vt.getSoLuongVatTuPhuTung() < soLuong) {
                return null;
            }

            donGia = vt.getDonGiaVatTuPhuTung();
        }

        double tienCong = tc.getSoTienCong();
        double thanhTien = donGia * (hasPart(maVatTu) ? soLuong : 0) + tienCong;

        return new DetailMoney(donGia, tienCong, thanhTien);
    }

    private boolean reduceStock(String maVatTu, int soLuong) {
        if (!hasPart(maVatTu)) {
            return true;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTu);

        if (vt == null || vt.getSoLuongVatTuPhuTung() < soLuong) {
            return false;
        }

        return vatTuDAO.updateSoLuong(maVatTu.trim(), vt.getSoLuongVatTuPhuTung() - soLuong);
    }

    private boolean restoreStock(String maVatTu, int soLuong) {
        if (!hasPart(maVatTu)) {
            return true;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTu);

        if (vt == null) {
            return false;
        }

        return vatTuDAO.updateSoLuong(maVatTu.trim(), vt.getSoLuongVatTuPhuTung() + soLuong);
    }

    private boolean adjustDebt(String maTiepNhanXe, double amount) {
        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (tnx == null) {
            return false;
        }

        double newDebt = tnx.getTienNo() + amount;

        if (newDebt < 0 && Math.abs(newDebt) < 0.01) {
            newDebt = 0;
        }

        if (newDebt < 0) {
            return false;
        }

        return tiepNhanXeDAO.updateTienNo(maTiepNhanXe.trim(), newDebt);
    }

    private boolean refreshRepairTotal(String maSuaChuaXe) {
        SuaChuaXe sc = suaChuaXeDAO.getById(maSuaChuaXe);

        if (sc == null) {
            return false;
        }

        double total = 0;

        for (ChiTietSuaChuaXe detail : getDetailsByRepairId(maSuaChuaXe)) {
            total += detail.getThanhTien();
        }

        SuaChuaXe updated = new SuaChuaXe(
                safeTrim(sc.getMaSuaChuaXe()),
                safeTrim(sc.getMaTiepNhanXe()),
                sc.getNgaySuaChua(),
                total
        );

        return suaChuaXeDAO.update(updated);
    }

    private boolean isValidHeader(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua) {
        if (isBlank(maSuaChuaXe)) return false;
        if (isBlank(maTiepNhanXe)) return false;
        return ngaySuaChua != null;
    }

    private boolean isValidDetail(String maVatTu, int soLuong, String maTienCong, String noiDung) {
        if (isBlank(maTienCong)) return false;
        if (isBlank(noiDung)) return false;

        if (hasPart(maVatTu)) {
            return soLuong > 0;
        }

        return soLuong == 0;
    }

    private boolean hasAnyReceipt(String maSuaChuaXe) {
        if (isBlank(maSuaChuaXe)) {
            return false;
        }

        return phieuThuTienDAO.getTotalPaidByRepairId(maSuaChuaXe.trim()) > 0;
    }

    private boolean hasUnfinishedRepairForIntake(String maTiepNhanXe) {
        if (isBlank(maTiepNhanXe)) {
            return false;
        }

        String intakeId = maTiepNhanXe.trim();

        for (SuaChuaXe repair : suaChuaXeDAO.getAll()) {
            if (repair == null || !safeTrim(repair.getMaTiepNhanXe()).equalsIgnoreCase(intakeId)) {
                continue;
            }

            if (!isRepairCompleted(repair)) {
                return true;
            }
        }

        return false;
    }

    private boolean isRepairCompleted(SuaChuaXe repair) {
        if (repair == null) {
            return false;
        }

        double paid = phieuThuTienDAO.getTotalPaidByRepairId(repair.getMaSuaChuaXe());
        return paid + 0.01 >= repair.getThanhTien();
    }

    private boolean hasPart(String maVatTu) {
        return !isBlank(maVatTu);
    }

    private String buildDetailId(String maSuaChuaXe) {
        String base = safeTrim(maSuaChuaXe).replaceAll("[^A-Za-z0-9]", "");

        if (base.isEmpty()) {
            base = "CT";
        }

        if (base.length() > 7) {
            base = base.substring(0, 7);
        }

        int index = getDetailsByRepairId(maSuaChuaXe).size() + 1;

        while (index < 1000) {
            String id = base + String.format("%03d", index);

            if (chiTietDAO.getById(id) == null) {
                return id;
            }

            index++;
        }

        return (base + System.currentTimeMillis()).substring(0, 10);
    }

    private String normalizeNullable(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private record DetailMoney(double partPrice, double laborPrice, double total) {
    }
}

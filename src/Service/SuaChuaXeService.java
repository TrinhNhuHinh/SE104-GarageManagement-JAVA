package Service;

import DAO.ChiTietSuaChuaXeDAO;
import DAO.SuaChuaXeDAO;
import DAO.TiepNhanXeDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.ChiTietSuaChuaXe;
import MODEL.SuaChuaXe;
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

    public List<SuaChuaXe> getAll() {
        return suaChuaXeDAO.getAll();
    }

    public List<String> getAllIntakeIds() {
        return tiepNhanXeDAO.getAllIds();
    }

    public List<String> getAllPartIds() {
        return vatTuDAO.getAllIds();
    }

    public ChiTietSuaChuaXe getDetailByRepairId(String maSuaChuaXe) {
        List<ChiTietSuaChuaXe> list = chiTietDAO.getByMaSuaChua(maSuaChuaXe);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public String getBienSoXeByIntakeId(String maTiepNhanXe) {
        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (tnx == null) {
            return "";
        }

        return tnx.getBienSoXe();
    }

    public String getNoiDungByRepairId(String maSuaChuaXe) {
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (ct == null) {
            return "";
        }

        return ct.getNoiDung();
    }

    public String getMaVatTuByRepairId(String maSuaChuaXe) {
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (ct == null) {
            return "";
        }

        return ct.getMaVatTuPhuTung();
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
            String maSuaChua = sc.getMaSuaChuaXe() == null ? "" : sc.getMaSuaChuaXe().toLowerCase();
            String maTiepNhan = sc.getMaTiepNhanXe() == null ? "" : sc.getMaTiepNhanXe().toLowerCase();
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
                       String maVatTu, int soLuong, double tienCong, String noiDung) {

        if (!isValid(maSuaChuaXe, maTiepNhanXe, ngaySuaChua, maVatTu, soLuong, tienCong, noiDung)) {
            return false;
        }

        if (suaChuaXeDAO.getById(maSuaChuaXe) != null) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTu);
        TiepNhanXe tnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (vt == null || tnx == null) {
            return false;
        }

        if (vt.getSoLuongVatTuPhuTung() < soLuong) {
            return false;
        }

        double donGia = vt.getDonGiaVatTuPhuTung();
        double thanhTien = donGia * soLuong + tienCong;

        SuaChuaXe sc = new SuaChuaXe(maSuaChuaXe, maTiepNhanXe, ngaySuaChua, thanhTien);

        ChiTietSuaChuaXe ct = new ChiTietSuaChuaXe(
                buildDetailId(maSuaChuaXe),
                maSuaChuaXe,
                noiDung,
                maVatTu,
                soLuong,
                donGia,
                null,
                thanhTien,
                tienCong
        );

        boolean insertSc = suaChuaXeDAO.insert(sc);

        if (!insertSc) {
            return false;
        }

        boolean insertCt = chiTietDAO.insert(ct);

        if (!insertCt) {
            suaChuaXeDAO.delete(maSuaChuaXe);
            return false;
        }

        boolean updateStock = vatTuDAO.updateSoLuong(maVatTu, vt.getSoLuongVatTuPhuTung() - soLuong);
        boolean updateDebt = tiepNhanXeDAO.updateTienNo(maTiepNhanXe, tnx.getTienNo() + thanhTien);

        return updateStock && updateDebt;
    }

    public boolean update(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua,
                          String maVatTu, int soLuong, double tienCong, String noiDung) {

        if (!isValid(maSuaChuaXe, maTiepNhanXe, ngaySuaChua, maVatTu, soLuong, tienCong, noiDung)) {
            return false;
        }

        SuaChuaXe oldSc = suaChuaXeDAO.getById(maSuaChuaXe);
        ChiTietSuaChuaXe oldCt = getDetailByRepairId(maSuaChuaXe);

        if (oldSc == null || oldCt == null) {
            return false;
        }

        VatTuPhuTung oldVt = vatTuDAO.getById(oldCt.getMaVatTuPhuTung());
        VatTuPhuTung newVt = vatTuDAO.getById(maVatTu);
        TiepNhanXe oldTnx = tiepNhanXeDAO.getById(oldSc.getMaTiepNhanXe());
        TiepNhanXe newTnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (oldVt == null || newVt == null || oldTnx == null || newTnx == null) {
            return false;
        }

        int availableStock = newVt.getSoLuongVatTuPhuTung();

        if (oldCt.getMaVatTuPhuTung().trim().equals(maVatTu.trim())) {
            availableStock += oldCt.getSoLuong();
        }

        if (availableStock < soLuong) {
            return false;
        }

        double donGia = newVt.getDonGiaVatTuPhuTung();
        double newThanhTien = donGia * soLuong + tienCong;

        boolean restoreOldStock = vatTuDAO.updateSoLuong(
                oldCt.getMaVatTuPhuTung(),
                oldVt.getSoLuongVatTuPhuTung() + oldCt.getSoLuong()
        );

        boolean restoreOldDebt = tiepNhanXeDAO.updateTienNo(
                oldSc.getMaTiepNhanXe(),
                oldTnx.getTienNo() - oldSc.getThanhTien()
        );

        if (!restoreOldStock || !restoreOldDebt) {
            return false;
        }

        SuaChuaXe newSc = new SuaChuaXe(maSuaChuaXe, maTiepNhanXe, ngaySuaChua, newThanhTien);

        ChiTietSuaChuaXe newCt = new ChiTietSuaChuaXe(
                oldCt.getMaChiTietSuaChuaXe(),
                maSuaChuaXe,
                noiDung,
                maVatTu,
                soLuong,
                donGia,
                null,
                newThanhTien,
                tienCong
        );

        boolean updateSc = suaChuaXeDAO.update(newSc);
        boolean updateCt = chiTietDAO.update(newCt);

        if (!updateSc || !updateCt) {
            return false;
        }

        VatTuPhuTung afterRestoreVt = vatTuDAO.getById(maVatTu);
        TiepNhanXe afterRestoreTnx = tiepNhanXeDAO.getById(maTiepNhanXe);

        if (afterRestoreVt == null || afterRestoreTnx == null) {
            return false;
        }

        boolean reduceNewStock = vatTuDAO.updateSoLuong(
                maVatTu,
                afterRestoreVt.getSoLuongVatTuPhuTung() - soLuong
        );

        boolean addNewDebt = tiepNhanXeDAO.updateTienNo(
                maTiepNhanXe,
                afterRestoreTnx.getTienNo() + newThanhTien
        );

        return reduceNewStock && addNewDebt;
    }

    public boolean delete(String maSuaChuaXe) {
        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) {
            return false;
        }

        SuaChuaXe sc = suaChuaXeDAO.getById(maSuaChuaXe);
        ChiTietSuaChuaXe ct = getDetailByRepairId(maSuaChuaXe);

        if (sc == null || ct == null) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(ct.getMaVatTuPhuTung());
        TiepNhanXe tnx = tiepNhanXeDAO.getById(sc.getMaTiepNhanXe());

        if (vt == null || tnx == null) {
            return false;
        }

        boolean restoreStock = vatTuDAO.updateSoLuong(
                ct.getMaVatTuPhuTung(),
                vt.getSoLuongVatTuPhuTung() + ct.getSoLuong()
        );

        boolean restoreDebt = tiepNhanXeDAO.updateTienNo(
                sc.getMaTiepNhanXe(),
                tnx.getTienNo() - sc.getThanhTien()
        );

        if (!restoreStock || !restoreDebt) {
            return false;
        }

        boolean deleteCt = chiTietDAO.delete(ct.getMaChiTietSuaChuaXe());
        boolean deleteSc = suaChuaXeDAO.delete(maSuaChuaXe);

        return deleteCt && deleteSc;
    }

    private boolean isValid(String maSuaChuaXe, String maTiepNhanXe, Date ngaySuaChua,
                            String maVatTu, int soLuong, double tienCong, String noiDung) {

        if (maSuaChuaXe == null || maSuaChuaXe.trim().isEmpty()) return false;
        if (maTiepNhanXe == null || maTiepNhanXe.trim().isEmpty()) return false;
        if (ngaySuaChua == null) return false;
        if (maVatTu == null || maVatTu.trim().isEmpty()) return false;
        if (soLuong <= 0) return false;
        if (tienCong < 0) return false;
        if (noiDung == null || noiDung.trim().isEmpty()) return false;

        return true;
    }

    private String buildDetailId(String maSuaChuaXe) {
        String id = "CT" + maSuaChuaXe.trim();

        if (id.length() > 10) {
            id = id.substring(0, 10);
        }

        return id;
    }
}
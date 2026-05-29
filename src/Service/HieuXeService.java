package Service;

import DAO.HieuXeDAO;
import DAO.ThongTinGarageDAO;
import MODEL.HieuXe;
import MODEL.ThongTinGarage;
import java.util.ArrayList;
import java.util.List;

public class HieuXeService {

    private final HieuXeDAO hieuXeDAO = new HieuXeDAO();
    private final ThongTinGarageDAO thongTinGarageDAO = new ThongTinGarageDAO();

    public List<HieuXe> getAll() {
        List<HieuXe> result = new ArrayList<>();

        for (Object obj : hieuXeDAO.getAll()) {
            result.add((HieuXe) obj);
        }

        return result;
    }

    public boolean add(HieuXe hx) {
        if (!isValid(hx)) return false;

        if (hieuXeDAO.getById(hx.getMaHieuXe()) != null) {
            return false;
        }

        ThongTinGarage tt = thongTinGarageDAO.get();
        int maxBrand = tt == null ? 10 : tt.getTongSoHieuXe();

        if (getAll().size() >= maxBrand) {
            return false;
        }

        return hieuXeDAO.insert(hx);
    }

    public boolean update(HieuXe hx) {
        if (!isValid(hx)) return false;

        if (hieuXeDAO.getById(hx.getMaHieuXe()) == null) {
            return false;
        }

        return hieuXeDAO.update(hx);
    }

    public boolean delete(String maHieuXe) {
        if (maHieuXe == null || maHieuXe.trim().isEmpty()) {
            return false;
        }

        return hieuXeDAO.delete(maHieuXe.trim());
    }

    private boolean isValid(HieuXe hx) {
        if (hx == null) return false;
        if (hx.getMaHieuXe() == null || hx.getMaHieuXe().trim().isEmpty()) return false;
        if (hx.getTenHieuXe() == null || hx.getTenHieuXe().trim().isEmpty()) return false;

        return true;
    }
}
package Service;

import DAO.ThongTinGarageDAO;
import DAO.TienCongDAO;
import MODEL.ThongTinGarage;
import MODEL.TienCong;
import java.util.List;

public class TienCongService {

    private final TienCongDAO tienCongDAO = new TienCongDAO();
    private final ThongTinGarageDAO thongTinGarageDAO = new ThongTinGarageDAO();

    public List<TienCong> getAll() {
        return tienCongDAO.getAll();
    }

    public List<String> getAllIds() {
        return tienCongDAO.getAllIds();
    }

    public TienCong getById(String maTienCong) {
        if (maTienCong == null || maTienCong.trim().isEmpty()) {
            return null;
        }

        return tienCongDAO.getById(maTienCong.trim());
    }

    public boolean add(TienCong tc) {
        if (!isValid(tc)) {
            return false;
        }

        if (tienCongDAO.getById(tc.getMaTienCong()) != null) {
            return false;
        }

        ThongTinGarage tt = thongTinGarageDAO.get();
        int maxTienCong = tt == null ? 100 : tt.getSoLuongTienCongToiDa();

        if (tienCongDAO.countAll() >= maxTienCong) {
            return false;
        }

        return tienCongDAO.insert(tc);
    }

    public boolean update(TienCong tc) {
        if (!isValid(tc)) {
            return false;
        }

        if (tienCongDAO.getById(tc.getMaTienCong()) == null) {
            return false;
        }

        return tienCongDAO.update(tc);
    }

    public boolean delete(String maTienCong) {
        if (maTienCong == null || maTienCong.trim().isEmpty()) {
            return false;
        }

        return tienCongDAO.delete(maTienCong.trim());
    }

    private boolean isValid(TienCong tc) {
        if (tc == null) return false;
        if (tc.getMaTienCong() == null || tc.getMaTienCong().trim().isEmpty()) return false;
        if (tc.getNoiDungTienCong() == null || tc.getNoiDungTienCong().trim().isEmpty()) return false;
        if (tc.getSoTienCong() < 0) return false;

        return true;
    }
}
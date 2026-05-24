package Service;

import DAO.TiepNhanXeDAO;
import MODEL.TiepNhanXe;
import java.util.List;

public class TiepNhanXeService {

    private final TiepNhanXeDAO tiepNhanXeDAO;

    public TiepNhanXeService() {
        this.tiepNhanXeDAO = new TiepNhanXeDAO();
    }

    public List<TiepNhanXe> getAll() {
        return tiepNhanXeDAO.getALL();
    }

    public boolean add(TiepNhanXe tnx) {
        if (!isValid(tnx)) {
            return false;
        }

        if (tiepNhanXeDAO.getById(tnx.getMaTiepNhanXe()) != null) {
            return false;
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
}
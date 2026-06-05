package Service;

import DAO.NhaCungCapDAO;
import MODEL.NhaCungCap;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapService {

    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    public List<NhaCungCap> getAll() {
        List<NhaCungCap> result = new ArrayList<>();

        for (Object obj : nhaCungCapDAO.getAll()) {
            result.add((NhaCungCap) obj);
        }

        return result;
    }

    public boolean add(NhaCungCap ncc) {
        if (!isValid(ncc)) return false;

        if (nhaCungCapDAO.getById(ncc.getMaNhaCungCap()) != null) {
            return false;
        }

        if (nhaCungCapDAO.getByName(ncc.getTenNhaCungCap().trim()) != null) {
            return false;
        }

        return nhaCungCapDAO.insert(ncc);
    }

    public boolean update(NhaCungCap ncc) {
        if (!isValid(ncc)) return false;

        if (nhaCungCapDAO.getById(ncc.getMaNhaCungCap()) == null) {
            return false;
        }

        NhaCungCap sameName = nhaCungCapDAO.getByName(ncc.getTenNhaCungCap().trim());

        if (sameName != null
                && !sameName.getMaNhaCungCap().trim().equalsIgnoreCase(ncc.getMaNhaCungCap().trim())) {
            return false;
        }

        return nhaCungCapDAO.update(ncc);
    }

    public boolean delete(String maNhaCungCap) {
        if (maNhaCungCap == null || maNhaCungCap.trim().isEmpty()) {
            return false;
        }

        return nhaCungCapDAO.delete(maNhaCungCap.trim());
    }

    private boolean isValid(NhaCungCap ncc) {
        if (ncc == null) return false;
        if (ncc.getMaNhaCungCap() == null || ncc.getMaNhaCungCap().trim().isEmpty()) return false;
        if (ncc.getTenNhaCungCap() == null || ncc.getTenNhaCungCap().trim().isEmpty()) return false;
        if (ncc.getSoDienThoaiNhaCungCap() == null || ncc.getSoDienThoaiNhaCungCap().trim().isEmpty()) return false;
        if (ncc.getEmailNhaCungCap() == null || ncc.getEmailNhaCungCap().trim().isEmpty()) return false;

        return true;
    }
}

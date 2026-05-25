package Service;

import DAO.VatTuPhuTungDAO;
import MODEL.VatTuPhuTung;
import java.util.List;

public class VatTuPhuTungService {

    private final VatTuPhuTungDAO vatTuDAO;

    private static final int LOW_STOCK_LIMIT = 5;

    public VatTuPhuTungService() {
        this.vatTuDAO = new VatTuPhuTungDAO();
    }

    public List<VatTuPhuTung> getAll() {
        return vatTuDAO.getAll();
    }

    public boolean add(VatTuPhuTung vt) {
        if (!isValid(vt)) {
            return false;
        }

        if (vatTuDAO.getById(vt.getMaVatTuPhuTung()) != null) {
            return false;
        }

        return vatTuDAO.insert(vt);
    }

    public boolean update(VatTuPhuTung vt) {
        if (!isValid(vt)) {
            return false;
        }

        if (vatTuDAO.getById(vt.getMaVatTuPhuTung()) == null) {
            return false;
        }

        return vatTuDAO.update(vt);
    }

    public boolean delete(String maVatTuPhuTung) {
        if (maVatTuPhuTung == null || maVatTuPhuTung.trim().isEmpty()) {
            return false;
        }

        return vatTuDAO.delete(maVatTuPhuTung.trim());
    }

    public List<VatTuPhuTung> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return vatTuDAO.search(keyword.trim());
    }

    public boolean importPart(String maVatTuPhuTung, int quantityToAdd) {
        if (maVatTuPhuTung == null || maVatTuPhuTung.trim().isEmpty()) {
            return false;
        }

        if (quantityToAdd <= 0) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTuPhuTung.trim());

        if (vt == null) {
            return false;
        }

        int newQuantity = vt.getSoLuongVatTuPhuTung() + quantityToAdd;

        return vatTuDAO.updateQuantity(vt.getMaVatTuPhuTung(), newQuantity);
    }

    public boolean sellPart(String maVatTuPhuTung, int quantityToSell) {
        if (maVatTuPhuTung == null || maVatTuPhuTung.trim().isEmpty()) {
            return false;
        }

        if (quantityToSell <= 0) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTuPhuTung.trim());

        if (vt == null) {
            return false;
        }

        if (vt.getSoLuongVatTuPhuTung() < quantityToSell) {
            return false;
        }

        int newQuantity = vt.getSoLuongVatTuPhuTung() - quantityToSell;

        return vatTuDAO.updateQuantity(vt.getMaVatTuPhuTung(), newQuantity);
    }

    public int countAll() {
        return vatTuDAO.countAll();
    }

    public int countLowStock() {
        return vatTuDAO.countLowStock(LOW_STOCK_LIMIT);
    }

    public double getInventoryValue() {
        return vatTuDAO.getInventoryValue();
    }

    private boolean isValid(VatTuPhuTung vt) {
        if (vt == null) return false;

        if (vt.getMaVatTuPhuTung() == null || vt.getMaVatTuPhuTung().trim().isEmpty()) return false;
        if (vt.getTenVatTuPhuTung() == null || vt.getTenVatTuPhuTung().trim().isEmpty()) return false;
        if (vt.getDonViTinh() == null || vt.getDonViTinh().trim().isEmpty()) return false;
        if (vt.getDonGiaVatTuPhuTung() < 0) return false;
        if (vt.getSoLuongVatTuPhuTung() < 0) return false;

        return true;
    }
}
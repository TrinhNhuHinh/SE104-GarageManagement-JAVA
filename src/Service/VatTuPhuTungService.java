package Service;

import DAO.BanVatTuDAO;
import DAO.ChiTietBanVatTuDAO;
import DAO.ChiTietNhapVatTuDAO;
import DAO.KhachHangDAO;
import DAO.NhaCungCapDAO;
import DAO.NhapVatTuDAO;
import DAO.VatTuPhuTungDAO;
import MODEL.BanVatTu;
import MODEL.ChiTietBanVatTu;
import MODEL.ChiTietNhapVatTu;
import MODEL.KhachHang;
import MODEL.NhaCungCap;
import MODEL.NhapVatTu;
import MODEL.VatTuPhuTung;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import DAO.ThongTinGarageDAO;
import MODEL.ThongTinGarage;

public class VatTuPhuTungService {

    private final VatTuPhuTungDAO vatTuDAO = new VatTuPhuTungDAO();
    private final NhapVatTuDAO nhapVatTuDAO = new NhapVatTuDAO();
    private final ChiTietNhapVatTuDAO chiTietNhapDAO = new ChiTietNhapVatTuDAO();
    private final BanVatTuDAO banVatTuDAO = new BanVatTuDAO();
    private final ChiTietBanVatTuDAO chiTietBanDAO = new ChiTietBanVatTuDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    private final ThongTinGarageDAO thongTinGarageDAO = new ThongTinGarageDAO();

    private static final int LOW_STOCK_LIMIT = 5;

    public List<VatTuPhuTung> getAll() {
        return vatTuDAO.getAll();
    }

    public List<ChiTietNhapVatTu> getAllImportDetails() {
        return chiTietNhapDAO.getAll();
    }

    public List<ChiTietBanVatTu> getAllSaleDetails() {
        return chiTietBanDAO.getAll();
    }

    public List<String> getAllPartIds() {
        return vatTuDAO.getAllIds();
    }

    public List<String> getAllCustomerIds() {
        List<String> ids = new ArrayList<>();

        for (KhachHang kh : khachHangDAO.getAll()) {
            ids.add(kh.getMaKhachHang().trim());
        }

        return ids;
    }

    public List<String> getAllSupplierIds() {
        List<String> ids = new ArrayList<>();

        for (NhaCungCap ncc : nhaCungCapDAO.getAll()) {
            ids.add(ncc.getMaNhaCungCap().trim());
        }

        return ids;
    }

    public VatTuPhuTung getPartById(String maVatTu) {
        if (maVatTu == null || maVatTu.trim().isEmpty()) {
            return null;
        }

        return vatTuDAO.getById(maVatTu.trim());
    }

    public String getSupplierByImportId(String maNhapVatTu) {
        NhapVatTu nvt = nhapVatTuDAO.getById(maNhapVatTu);

        if (nvt == null) {
            return "";
        }

        return nvt.getMaNhaCungCap();
    }

    public String getImportDateByImportId(String maNhapVatTu) {
        NhapVatTu nvt = nhapVatTuDAO.getById(maNhapVatTu);

        if (nvt == null || nvt.getNgayNhap() == null) {
            return "";
        }

        return nvt.getNgayNhap().toString();
    }

    public String getCustomerBySaleId(String maBanVatTu) {
        BanVatTu bvt = banVatTuDAO.getById(maBanVatTu);

        if (bvt == null) {
            return "";
        }

        return bvt.getMaKhachHang();
    }

    public String getSaleDateBySaleId(String maBanVatTu) {
        BanVatTu bvt = banVatTuDAO.getById(maBanVatTu);

        if (bvt == null || bvt.getNgayBan() == null) {
            return "";
        }

        return bvt.getNgayBan().toString();
    }

    public boolean add(VatTuPhuTung vt) {
        if (!isValidPart(vt)) {
            return false;
        }

        if (vatTuDAO.getById(vt.getMaVatTuPhuTung()) != null) {
            return false;
        }
        
        ThongTinGarage tt = thongTinGarageDAO.get();

        int maxVatTu = 200;

        if (tt != null) {
        maxVatTu = tt.getSoLuongVatTuToiDa();
        }

        if (vatTuDAO.countAll() >= maxVatTu) {
        return false;
        }

        return vatTuDAO.insert(vt);
    }

    public boolean update(VatTuPhuTung vt) {
        if (!isValidPart(vt)) {
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

    public boolean createImportInvoice(String maNhapVatTu, String maNhaCungCap, Date ngayNhap,
                                       String maVatTu, int soLuong, double donGia) {

        if (!isValidImport(maNhapVatTu, maNhaCungCap, ngayNhap, maVatTu, soLuong, donGia)) {
            return false;
        }

        if (nhapVatTuDAO.getById(maNhapVatTu) != null) {
            return false;
        }

        if (nhaCungCapDAO.getById(maNhaCungCap) == null) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTu);

        if (vt == null) {
            return false;
        }

        double thanhTien = soLuong * donGia;

        NhapVatTu nvt = new NhapVatTu(maNhapVatTu, maNhaCungCap, thanhTien, ngayNhap);
        ChiTietNhapVatTu ct = new ChiTietNhapVatTu(maNhapVatTu, maVatTu, soLuong, donGia, thanhTien);

        boolean insertedHeader = nhapVatTuDAO.insert(nvt);

        if (!insertedHeader) {
            return false;
        }

        boolean insertedDetail = chiTietNhapDAO.insert(ct);

        if (!insertedDetail) {
            nhapVatTuDAO.delete(maNhapVatTu);
            return false;
        }

        boolean updatedStock = vatTuDAO.updateSoLuong(
                maVatTu,
                vt.getSoLuongVatTuPhuTung() + soLuong
        );

        if (!updatedStock) {
            chiTietNhapDAO.delete(maNhapVatTu, maVatTu);
            nhapVatTuDAO.delete(maNhapVatTu);
            return false;
        }

        return true;
    }

    public boolean createSaleInvoice(String maBanVatTu, String maKhachHang, Date ngayBan,
                                     String maVatTu, int soLuong, double donGia) {

        if (!isValidSale(maBanVatTu, maKhachHang, ngayBan, maVatTu, soLuong, donGia)) {
            return false;
        }

        if (banVatTuDAO.getById(maBanVatTu) != null) {
            return false;
        }

        if (khachHangDAO.getById(maKhachHang) == null) {
            return false;
        }

        VatTuPhuTung vt = vatTuDAO.getById(maVatTu);

        if (vt == null) {
            return false;
        }

        if (vt.getSoLuongVatTuPhuTung() < soLuong) {
            return false;
        }

        double thanhTien = soLuong * donGia;

        BanVatTu bvt = new BanVatTu(maBanVatTu, maKhachHang, ngayBan, thanhTien);
        ChiTietBanVatTu ct = new ChiTietBanVatTu(maBanVatTu, maVatTu, soLuong, donGia, thanhTien);

        boolean insertedHeader = banVatTuDAO.insert(bvt);

        if (!insertedHeader) {
            return false;
        }

        boolean insertedDetail = chiTietBanDAO.insert(ct);

        if (!insertedDetail) {
            banVatTuDAO.delete(maBanVatTu);
            return false;
        }

        boolean updatedStock = vatTuDAO.updateSoLuong(
                maVatTu,
                vt.getSoLuongVatTuPhuTung() - soLuong
        );

        if (!updatedStock) {
            chiTietBanDAO.delete(maBanVatTu, maVatTu);
            banVatTuDAO.delete(maBanVatTu);
            return false;
        }

        return true;
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

    private boolean isValidPart(VatTuPhuTung vt) {
        if (vt == null) return false;

        if (vt.getMaVatTuPhuTung() == null || vt.getMaVatTuPhuTung().trim().isEmpty()) return false;
        if (vt.getTenVatTuPhuTung() == null || vt.getTenVatTuPhuTung().trim().isEmpty()) return false;
        if (vt.getDonViTinh() == null || vt.getDonViTinh().trim().isEmpty()) return false;
        if (vt.getDonGiaVatTuPhuTung() < 0) return false;
        if (vt.getSoLuongVatTuPhuTung() < 0) return false;

        return true;
    }

    private boolean isValidImport(String maNhapVatTu, String maNhaCungCap, Date ngayNhap,
                                  String maVatTu, int soLuong, double donGia) {

        if (maNhapVatTu == null || maNhapVatTu.trim().isEmpty()) return false;
        if (maNhaCungCap == null || maNhaCungCap.trim().isEmpty()) return false;
        if (ngayNhap == null) return false;
        if (maVatTu == null || maVatTu.trim().isEmpty()) return false;
        if (soLuong <= 0) return false;
        if (donGia < 0) return false;

        return true;
    }

    private boolean isValidSale(String maBanVatTu, String maKhachHang, Date ngayBan,
                                String maVatTu, int soLuong, double donGia) {

        if (maBanVatTu == null || maBanVatTu.trim().isEmpty()) return false;
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) return false;
        if (ngayBan == null) return false;
        if (maVatTu == null || maVatTu.trim().isEmpty()) return false;
        if (soLuong <= 0) return false;
        if (donGia < 0) return false;

        return true;
    }
}
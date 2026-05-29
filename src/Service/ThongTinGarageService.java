package Service;

import DAO.ThongTinGarageDAO;
import MODEL.ThongTinGarage;

public class ThongTinGarageService {

    private final ThongTinGarageDAO thongTinGarageDAO = new ThongTinGarageDAO();

    public ThongTinGarage getSettings() {
        ThongTinGarage tt = thongTinGarageDAO.get();

        if (tt == null) {
            return getDefaultSettings();
        }

        return tt;
    }

    public boolean updateSettings(int soLuongXeToiDa, int tongSoHieuXe,
                                  int soLuongVatTuToiDa, int soLuongTienCongToiDa) {

        if (soLuongXeToiDa <= 0) return false;
        if (tongSoHieuXe <= 0) return false;
        if (soLuongVatTuToiDa <= 0) return false;
        if (soLuongTienCongToiDa <= 0) return false;

        ThongTinGarage tt = new ThongTinGarage(
                "GARAGE",
                soLuongXeToiDa,
                tongSoHieuXe,
                0,
                soLuongVatTuToiDa,
                soLuongTienCongToiDa
        );

        return thongTinGarageDAO.update(tt);
    }

    public boolean resetDefault() {
        ThongTinGarage tt = getDefaultSettings();
        return thongTinGarageDAO.update(tt);
    }

    private ThongTinGarage getDefaultSettings() {
        return new ThongTinGarage(
                "GARAGE",
                30,
                10,
                0,
                200,
                100
        );
    }
}
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

    public boolean updateMaxCarsPerDay(int soLuongXeToiDa) {
        if (soLuongXeToiDa <= 0) {
            return false;
        }

        ThongTinGarage tt = new ThongTinGarage("GARAGE", soLuongXeToiDa);

        if (thongTinGarageDAO.get() == null) {
            return thongTinGarageDAO.insert(tt);
        }

        return thongTinGarageDAO.update(tt);
    }

    public boolean resetDefault() {
        return updateMaxCarsPerDay(30);
    }

    private ThongTinGarage getDefaultSettings() {
        return new ThongTinGarage("GARAGE", 30);
    }
}

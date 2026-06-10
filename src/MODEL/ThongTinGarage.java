package MODEL;

public class ThongTinGarage {

    private String id;
    private int soLuongXeToiDa;

    public ThongTinGarage() {
    }

    public ThongTinGarage(String id, int soLuongXeToiDa) {
        this.id = id;
        this.soLuongXeToiDa = soLuongXeToiDa;
    }

    public String getId() {
        return id;
    }

    public int getSoLuongXeToiDa() {
        return soLuongXeToiDa;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSoLuongXeToiDa(int soLuongXeToiDa) {
        this.soLuongXeToiDa = soLuongXeToiDa;
    }
}

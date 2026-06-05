package DAO;

import Config.DBConnection;
import MODEL.PhieuThuTien;
import MODEL.ThongTinGarage;
import MODEL.TiepNhanXe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuThuTienDAO {

    private final ThongTinGarageDAO thongTinGarageDAO = new ThongTinGarageDAO();

    public boolean insert(PhieuThuTien pt) {
        return insertAndReduceDebt(pt);
    }

    public boolean update(PhieuThuTien pt) {
        return updateAndAdjustDebt(pt);
    }

    public boolean delete(String maPhieuThuTien) {
        return deleteAndRestoreDebt(maPhieuThuTien);
    }

    public List<PhieuThuTien> getAll() {
        List<PhieuThuTien> list = new ArrayList<>();

        String sql = "SELECT * FROM PHIEUTHUTIEN ORDER BY MaPhieuThuTien";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToPhieuThu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    public PhieuThuTien getById(String maPhieuThuTien) {
        String sql = "SELECT * FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPhieuThuTien);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToPhieuThu(rs);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return null;
    }

    public double getTotalPaidByRepairId(String maSuaChuaXe) {
        String sql = """
            SELECT ISNULL(SUM(SoTienThu), 0)
            FROM PHIEUTHUTIEN
            WHERE Ma_SuaChuaXe = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSuaChuaXe);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return 0;
    }

    public List<PhieuThuTien> search(String keyword) {
        List<PhieuThuTien> list = new ArrayList<>();

        String sql = """
            SELECT * FROM PHIEUTHUTIEN
            WHERE MaPhieuThuTien LIKE ?
               OR Ma_TiepNhanXe LIKE ?
               OR Ma_SuaChuaXe LIKE ?
               OR BienSoXe LIKE ?
               OR SoDienThoai LIKE ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";

            ps.setString(1, value);
            ps.setString(2, value);
            ps.setString(3, value);
            ps.setString(4, value);
            ps.setString(5, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToPhieuThu(rs));
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return list;
    }

    private boolean insertAndReduceDebt(PhieuThuTien pt) {
        String repairInfoSql = """
            SELECT sc.MaSuaChuaXe,
                   sc.Ma_TiepNhanXe,
                   sc.ThanhTien,
                   tnx.BienSoXe,
                   tnx.TienNo
            FROM SUACHUAXE sc
            JOIN TIEPNHANXE tnx WITH (UPDLOCK, ROWLOCK)
                ON sc.Ma_TiepNhanXe = tnx.MaTiepNhanXe
            WHERE sc.MaSuaChuaXe = ?
        """;

        String paidSql = """
            SELECT ISNULL(SUM(SoTienThu), 0)
            FROM PHIEUTHUTIEN WITH (UPDLOCK, ROWLOCK)
            WHERE Ma_SuaChuaXe = ?
        """;

        String insertSql = """
            INSERT INTO PHIEUTHUTIEN
            (MaPhieuThuTien, Ma_TiepNhanXe, Ma_SuaChuaXe, NgayThuTien, BienSoXe, Email,
             SoDienThoai, SoTienThu, VatPercent, PriceIncreasePercent)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        String updateDebtSql = """
            UPDATE TIEPNHANXE
            SET TienNo = CASE WHEN TienNo - ? < 0 THEN 0 ELSE TienNo - ? END
            WHERE MaTiepNhanXe = ?
        """;

        String updateStatusSql = """
            UPDATE TIEPNHANXE
            SET TrangThai = ?
            WHERE MaTiepNhanXe = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psRepair = conn.prepareStatement(repairInfoSql);
                 PreparedStatement psPaid = conn.prepareStatement(paidSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertSql);
                 PreparedStatement psDebt = conn.prepareStatement(updateDebtSql);
                 PreparedStatement psStatus = conn.prepareStatement(updateStatusSql)) {

                psRepair.setString(1, pt.getMaSuaChuaXe());
                ResultSet repairRs = psRepair.executeQuery();

                if (!repairRs.next()) {
                    conn.rollback();
                    return false;
                }

                String maTiepNhanXe = repairRs.getString("Ma_TiepNhanXe");
                String bienSoXe = repairRs.getString("BienSoXe");
                double repairTotal = applyVatAndIncrease(repairRs.getDouble("ThanhTien"));
                double currentDebt = repairRs.getDouble("TienNo");

                psPaid.setString(1, pt.getMaSuaChuaXe());
                ResultSet paidRs = psPaid.executeQuery();

                double alreadyPaid = 0;

                if (paidRs.next()) {
                    alreadyPaid = paidRs.getDouble(1);
                }

                double remainingRepairAmount = repairTotal - alreadyPaid;

                if (pt.getSoTienThu() <= 0) {
                    conn.rollback();
                    return false;
                }

                if (pt.getSoTienThu() > remainingRepairAmount) {
                    conn.rollback();
                    return false;
                }

                psInsert.setString(1, pt.getMaPhieuThuTien());
                psInsert.setString(2, maTiepNhanXe);
                psInsert.setString(3, pt.getMaSuaChuaXe());
                psInsert.setDate(4, pt.getNgayThuTien());
                psInsert.setString(5, bienSoXe);
                psInsert.setString(6, pt.getEmail());
                psInsert.setString(7, pt.getSoDienThoai());
                psInsert.setDouble(8, pt.getSoTienThu());
                psInsert.setDouble(9, pt.getVatPercent());
                psInsert.setDouble(10, pt.getPriceIncreasePercent());
                psInsert.executeUpdate();

                double debtReduction = Math.min(pt.getSoTienThu(), currentDebt);
                psDebt.setDouble(1, debtReduction);
                psDebt.setDouble(2, debtReduction);
                psDebt.setString(3, maTiepNhanXe);

                int affectedRows = psDebt.executeUpdate();

                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                if (remainingRepairAmount - pt.getSoTienThu() <= 0.01) {
                    psStatus.setString(1, TiepNhanXe.STATUS_RETURNED);
                    psStatus.setString(2, maTiepNhanXe);
                    psStatus.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                printSqlError(e);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private boolean updateAndAdjustDebt(PhieuThuTien newPt) {
        String oldReceiptSql = "SELECT * FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";

        String repairInfoSql = """
            SELECT sc.ThanhTien,
                   tnx.TienNo
            FROM SUACHUAXE sc
            JOIN TIEPNHANXE tnx WITH (UPDLOCK, ROWLOCK)
                ON sc.Ma_TiepNhanXe = tnx.MaTiepNhanXe
            WHERE sc.MaSuaChuaXe = ?
        """;

        String paidSql = """
            SELECT ISNULL(SUM(SoTienThu), 0)
            FROM PHIEUTHUTIEN WITH (UPDLOCK, ROWLOCK)
            WHERE Ma_SuaChuaXe = ?
        """;

        String updateReceiptSql = """
            UPDATE PHIEUTHUTIEN
            SET NgayThuTien = ?,
                SoTienThu = ?
            WHERE MaPhieuThuTien = ?
        """;

        String updateDebtSql = """
            UPDATE TIEPNHANXE
            SET TienNo = ?
            WHERE MaTiepNhanXe = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psOld = conn.prepareStatement(oldReceiptSql);
                 PreparedStatement psRepair = conn.prepareStatement(repairInfoSql);
                 PreparedStatement psPaid = conn.prepareStatement(paidSql);
                 PreparedStatement psUpdateReceipt = conn.prepareStatement(updateReceiptSql);
                 PreparedStatement psUpdateDebt = conn.prepareStatement(updateDebtSql)) {

                psOld.setString(1, newPt.getMaPhieuThuTien());
                ResultSet oldRs = psOld.executeQuery();

                if (!oldRs.next()) {
                    conn.rollback();
                    return false;
                }

                PhieuThuTien oldPt = mapResultSetToPhieuThu(oldRs);

                psRepair.setString(1, oldPt.getMaSuaChuaXe());
                ResultSet repairRs = psRepair.executeQuery();

                if (!repairRs.next()) {
                    conn.rollback();
                    return false;
                }

                double repairTotal = repairRs.getDouble("ThanhTien");
                double currentDebt = repairRs.getDouble("TienNo");

                psPaid.setString(1, oldPt.getMaSuaChuaXe());
                ResultSet paidRs = psPaid.executeQuery();

                double paidIncludingOld = 0;

                if (paidRs.next()) {
                    paidIncludingOld = paidRs.getDouble(1);
                }

                double paidWithoutOld = paidIncludingOld - oldPt.getSoTienThu();
                double maxCanPayForThisReceipt = repairTotal - paidWithoutOld;
                double debtBeforeOldReceipt = currentDebt + oldPt.getSoTienThu();

                if (newPt.getSoTienThu() <= 0) {
                    conn.rollback();
                    return false;
                }

                if (newPt.getSoTienThu() > maxCanPayForThisReceipt) {
                    conn.rollback();
                    return false;
                }

                if (newPt.getSoTienThu() > debtBeforeOldReceipt) {
                    conn.rollback();
                    return false;
                }

                psUpdateReceipt.setDate(1, newPt.getNgayThuTien());
                psUpdateReceipt.setDouble(2, newPt.getSoTienThu());
                psUpdateReceipt.setString(3, newPt.getMaPhieuThuTien());
                psUpdateReceipt.executeUpdate();

                double newDebt = debtBeforeOldReceipt - newPt.getSoTienThu();

                psUpdateDebt.setDouble(1, newDebt);
                psUpdateDebt.setString(2, oldPt.getMaTiepNhanXe());
                psUpdateDebt.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                printSqlError(e);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private boolean deleteAndRestoreDebt(String maPhieuThuTien) {
        String oldReceiptSql = "SELECT * FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";
        String deleteSql = "DELETE FROM PHIEUTHUTIEN WHERE MaPhieuThuTien = ?";
        String restoreDebtSql = """
            UPDATE TIEPNHANXE
            SET TienNo = TienNo + ?
            WHERE MaTiepNhanXe = ?
        """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psOld = conn.prepareStatement(oldReceiptSql);
                 PreparedStatement psDelete = conn.prepareStatement(deleteSql);
                 PreparedStatement psRestore = conn.prepareStatement(restoreDebtSql)) {

                psOld.setString(1, maPhieuThuTien);
                ResultSet rs = psOld.executeQuery();

                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }

                PhieuThuTien oldPt = mapResultSetToPhieuThu(rs);

                psDelete.setString(1, maPhieuThuTien);
                psDelete.executeUpdate();

                psRestore.setDouble(1, oldPt.getSoTienThu());
                psRestore.setString(2, oldPt.getMaTiepNhanXe());
                psRestore.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                printSqlError(e);
            }

        } catch (SQLException e) {
            printSqlError(e);
        }

        return false;
    }

    private PhieuThuTien mapResultSetToPhieuThu(ResultSet rs) throws SQLException {
        PhieuThuTien pt = new PhieuThuTien();

        pt.setMaPhieuThuTien(rs.getString("MaPhieuThuTien"));
        pt.setMaTiepNhanXe(rs.getString("Ma_TiepNhanXe"));
        pt.setMaSuaChuaXe(rs.getString("Ma_SuaChuaXe"));
        pt.setNgayThuTien(rs.getDate("NgayThuTien"));
        pt.setBienSoXe(rs.getString("BienSoXe"));
        pt.setEmail(rs.getString("Email"));
        pt.setSoDienThoai(rs.getString("SoDienThoai"));
        pt.setSoTienThu(rs.getDouble("SoTienThu"));
        pt.setVatPercent(getDoubleIfPresent(rs, "VatPercent"));
        pt.setPriceIncreasePercent(getDoubleIfPresent(rs, "PriceIncreasePercent"));

        return pt;
    }

    private double applyVatAndIncrease(double baseAmount) {
        ThongTinGarage settings = thongTinGarageDAO.get();

        if (settings == null) {
            return baseAmount;
        }

        double afterIncrease = baseAmount * (1 + settings.getPriceIncreasePercent() / 100.0);
        return afterIncrease * (1 + settings.getVatPercent() / 100.0);
    }

    private double getDoubleIfPresent(ResultSet rs, String columnName) {
        try {
            return rs.getDouble(columnName);
        } catch (SQLException e) {
            return 0;
        }
    }

    private void printSqlError(SQLException e) {
        System.out.println("SQL ERROR CODE: " + e.getErrorCode());
        System.out.println("SQL STATE: " + e.getSQLState());
        System.out.println("SQL MESSAGE: " + e.getMessage());
        e.printStackTrace();
    }
}

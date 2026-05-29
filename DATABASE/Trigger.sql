--Trigger Garage Management--
--Tính thành tiền--
--Tự động tính ThanhTien = (SoLuong * DonGia) + SoTienCong--
CREATE TRIGGER trg_TinhThanhTien_ChiTietSuaChua
ON CHITIETSUACHUAXE
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE CHITIETSUACHUAXE
    SET ThanhTien = (i.SoLuong * i.DonGia) + i.SoTienCong
    FROM CHITIETSUACHUAXE ct
    INNER JOIN inserted i ON ct.MaChiTietSuaChuaXe = i.MaChiTietSuaChuaXe
END
GO

--Khi thêm/sửa/xóa chi tiết → tự động cập nhật tổng tiền phiếu sửa chữa--
CREATE TRIGGER trg_CapNhatTongTien_SuaChua
ON CHITIETSUACHUAXE
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    -- Cập nhật tổng tiền cho các phiếu bị ảnh hưởng
    UPDATE SUACHUAXE
    SET ThanhTien = (
        SELECT ISNULL(SUM(ThanhTien), 0)
        FROM CHITIETSUACHUAXE
        WHERE Ma_SuaChuaXe = SUACHUAXE.MaSuaChuaXe
    )
    WHERE MaSuaChuaXe IN (
        SELECT DISTINCT Ma_SuaChuaXe FROM inserted
        UNION
        SELECT DISTINCT Ma_SuaChuaXe FROM deleted
    )
END
GO

--ThanhTien = SoLuong * DonGia--
-- Nhập vật tư--
CREATE TRIGGER trg_TinhThanhTien_ChiTietNhap
ON CHITIETNHAPVATTU
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE CHITIETNHAPVATTU
    SET ThanhTien = i.SoLuong * i.DonGia
    FROM CHITIETNHAPVATTU ct
    INNER JOIN inserted i ON ct.Ma_NhapVatTu = i.Ma_NhapVatTu 
                         AND ct.Ma_VatTuPhuTung = i.Ma_VatTuPhuTung
END
GO

-- Bán vật tư--
CREATE TRIGGER trg_TinhThanhTien_ChiTietBan
ON CHITIETBANVATTU
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE CHITIETBANVATTU
    SET ThanhTien = i.SoLuong * i.DonGia
    FROM CHITIETBANVATTU ct
    INNER JOIN inserted i ON ct.Ma_BanVatTu = i.Ma_BanVatTu 
                         AND ct.Ma_VatTuPhuTung = i.Ma_VatTuPhuTung
END
GO

-- Phiếu nhập--
CREATE TRIGGER trg_CapNhatTongTien_NhapVatTu
ON CHITIETNHAPVATTU
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE NHAPVATTU
    SET TongTien = (
        SELECT ISNULL(SUM(ThanhTien), 0)
        FROM CHITIETNHAPVATTU
        WHERE Ma_NhapVatTu = NHAPVATTU.MaNhapVatTu
    )
    WHERE MaNhapVatTu IN (
        SELECT DISTINCT Ma_NhapVatTu FROM inserted
        UNION
        SELECT DISTINCT Ma_NhapVatTu FROM deleted
    )
END
GO

-- Phiếu bán--
--Tổng tiền phiếu = tổng chi tiết--
CREATE TRIGGER trg_CapNhatTongTien_BanVatTu
ON CHITIETBANVATTU
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE BANVATTU
    SET TongTien = (
        SELECT ISNULL(SUM(ThanhTien), 0)
        FROM CHITIETBANVATTU
        WHERE Ma_BanVatTu = BANVATTU.MaBanVatTu
    )
    WHERE MaBanVatTu IN (
        SELECT DISTINCT Ma_BanVatTu FROM inserted
        UNION
        SELECT DISTINCT Ma_BanVatTu FROM deleted
    )
END
GO

--Tăng tồn kho khi nhập vật tư--
CREATE TRIGGER trg_TangTonKho_Nhap
ON CHITIETNHAPVATTU
AFTER INSERT
AS
BEGIN
    UPDATE VATTUPHUTUNG
    SET SoLuongVatTuPhuTung = SoLuongVatTuPhuTung + i.SoLuong
    FROM VATTUPHUTUNG vt
    INNER JOIN inserted i ON vt.MaVatTuPhuTung = i.Ma_VatTuPhuTung
END
GO

--Giảm tồn kho khi bán vật tư--
CREATE TRIGGER trg_GiamTonKho_Ban
ON CHITIETBANVATTU
AFTER INSERT
AS
BEGIN
    UPDATE VATTUPHUTUNG
    SET SoLuongVatTuPhuTung = SoLuongVatTuPhuTung - i.SoLuong
    FROM VATTUPHUTUNG vt
    INNER JOIN inserted i ON vt.MaVatTuPhuTung = i.Ma_VatTuPhuTung
    
    -- Kiểm tra tồn kho âm
    IF EXISTS (
        SELECT 1 FROM VATTUPHUTUNG WHERE SoLuongVatTuPhuTung < 0
    )
    BEGIN
        RAISERROR('Ton kho khong du!', 16, 1)
        ROLLBACK TRANSACTION
    END
END
GO

--Giảm tồn kho khi dùng vật tư sửa chữa--
CREATE TRIGGER trg_GiamTonKho_SuaChua
ON CHITIETSUACHUAXE
AFTER INSERT
AS
BEGIN
    UPDATE VATTUPHUTUNG
    SET SoLuongVatTuPhuTung = SoLuongVatTuPhuTung - i.SoLuong
    FROM VATTUPHUTUNG vt
    INNER JOIN inserted i ON vt.MaVatTuPhuTung = i.Ma_VatTuPhuTung
    
    IF EXISTS (
        SELECT 1 FROM VATTUPHUTUNG WHERE SoLuongVatTuPhuTung < 0
    )
    BEGIN
        RAISERROR('Ton kho vat tu khong du!', 16, 1)
        ROLLBACK TRANSACTION
    END
END
GO

--Max 30 xe 1 ngày--
CREATE TRIGGER trg_KiemTraMaxXe_TiepNhan
ON TIEPNHANXE
AFTER INSERT
AS
BEGIN
    DECLARE @NgayTiepNhan DATE
    DECLARE @SoXe INT
    DECLARE @MaxXe INT
    
    SELECT @NgayTiepNhan = NgayTiepNhan FROM inserted
    SELECT @MaxXe = SoLuongXeToiDa FROM THONGTINGARAGE
    
    SELECT @SoXe = COUNT(*)
    FROM TIEPNHANXE
    WHERE NgayTiepNhan = @NgayTiepNhan
    
    IF @SoXe > @MaxXe
    BEGIN
        RAISERROR('Da du 30 xe trong ngay!', 16, 1)
        ROLLBACK TRANSACTION
    END
END
GO

--Kiểm tra biển số xe, 1 xe không thể tiếp nhận 2 lần khi chưa sửa xong--
CREATE TRIGGER trg_KiemTraBienSo_TiepNhan
ON TIEPNHANXE
AFTER INSERT
AS
BEGIN
    IF EXISTS (
        SELECT 1
        FROM TIEPNHANXE tn
        INNER JOIN inserted i ON tn.BienSoXe = i.BienSoXe 
                             AND tn.NgayTiepNhan = i.NgayTiepNhan
                             AND tn.MaTiepNhanXe != i.MaTiepNhanXe
    )
    BEGIN
        RAISERROR('Xe nay da duoc tiep nhan!', 16, 1)
        ROLLBACK TRANSACTION
    END
END
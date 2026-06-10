USE Garage;
GO

-- Xoa du lieu cu theo dung thu tu khoa ngoai de script co the chay lai nhieu lan.
DELETE FROM THONGTINNGUOIDUNG;
DELETE FROM NGUOIDUNG;
DELETE FROM CHITIETCHUCVU;
DELETE FROM QUYENHAN;
DELETE FROM CHUCVU;
DELETE FROM CHITIETBAOCAOTON;
DELETE FROM BAOCAOTON;
DELETE FROM CHITIETBAOCAODOANHSO;
DELETE FROM BAOCAODOANHSO;
DELETE FROM PHIEUTHUTIEN;
DELETE FROM CHITIETSUACHUAXE;
DELETE FROM CHITIETBANVATTU;
DELETE FROM BANVATTU;
DELETE FROM CHITIETNHAPVATTU;
DELETE FROM NHAPVATTU;
DELETE FROM NHACUNGCAP;
DELETE FROM SUACHUAXE;
DELETE FROM TIEPNHANXE;
DELETE FROM VATTUPHUTUNG;
DELETE FROM TIENCONG;
DELETE FROM THONGTINGARAGE;
DELETE FROM KHACHHANG;
DELETE FROM HIEUXE;
GO

-- 1. Hieu xe
INSERT INTO HIEUXE (MaHieuXe, TenHieuXe) VALUES
('HX01', N'Toyota'),
('HX02', N'Honda'),
('HX03', N'Suzuki'),
('HX04', N'Ford'),
('HX05', N'Hyundai'),
('HX06', N'Kia'),
('HX07', N'Mazda'),
('HX08', N'Mitsubishi'),
('HX09', N'Nissan'),
('HX10', N'VinFast');
GO

-- 2. Khach hang demo
INSERT INTO KHACHHANG (MaKhachHang, TenKhachHang, DiaChiKhachHang, SoDienThoaiKhachHang) VALUES
('KH01', N'Nguyen Van An', N'Quan 1, TP. Ho Chi Minh', '0901000001'),
('KH02', N'Tran Thi Bich', N'Quan 3, TP. Ho Chi Minh', '0901000002'),
('KH03', N'Le Minh Cuong', N'Thu Duc, TP. Ho Chi Minh', '0901000003'),
('KH04', N'Pham Gia Huy', N'Quan 7, TP. Ho Chi Minh', '0901000004');
GO

-- 3. Tien cong
INSERT INTO TIENCONG (MaTienCong, SoTienCong, NoiDungTienCong) VALUES
('TC01', 80000, N'Thay dau may'),
('TC02', 150000, N'Kiem tra tong quat'),
('TC03', 250000, N'Thay ma phanh'),
('TC04', 350000, N'Ve sinh khoang may'),
('TC05', 500000, N'Son dam va');
GO

-- 4. Vat tu phu tung
INSERT INTO VATTUPHUTUNG (MaVatTuPhuTung, TenVatTuPhuTung, DonGiaVatTuPhuTung, SoLuongVatTuPhuTung, DonViTinh) VALUES
('VT01', N'Dau nhot 4L', 550000, 24, N'Binh'),
('VT02', N'Loc dau', 120000, 18, N'Cai'),
('VT03', N'Bugi', 90000, 40, N'Cai'),
('VT04', N'Ma phanh truoc', 450000, 4, N'Bo'),
('VT05', N'Gat mua', 180000, 0, N'Cap');
GO

-- 5. Thong tin garage
INSERT INTO THONGTINGARAGE
    (Id, SoLuongXeToiDa)
VALUES
    ('GARAGE', 30);
GO

-- 6. Tiep nhan xe
INSERT INTO TIEPNHANXE (MaTiepNhanXe, Ma_KhachHang, BienSoXe, Ma_HieuXe, NgayTiepNhan, TienNo) VALUES
('TN01', 'KH01', '51A12345', 'HX01', '2026-06-01', 0),
('TN02', 'KH02', '51B23456', 'HX02', '2026-06-01', 0),
('TN03', 'KH03', '51C34567', 'HX04', '2026-06-02', 1150000),
('TN04', 'KH04', '51D45678', 'HX10', '2026-06-03', 0);
GO

-- 7. Phieu sua chua
INSERT INTO SUACHUAXE (MaSuaChuaXe, Ma_TiepNhanXe, NgaySuaChua, ThanhTien) VALUES
('SC01', 'TN01', '2026-06-01', 630000),
('SC02', 'TN02', '2026-06-02', 150000),
('SC03', 'TN03', '2026-06-03', 1150000);
GO

-- 8. Nha cung cap
INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoaiNhaCungCap, EmailNhaCungCap) VALUES
('NCC01', N'Phu Tung Sai Gon', '02811112222', 'phutungsaigon@example.com'),
('NCC02', N'Dau Nhot Minh Tam', '02833334444', 'minhtam@example.com');
GO

-- 9. Nhap vat tu
INSERT INTO NHAPVATTU (MaNhapVatTu, Ma_NhaCungCap, TongTien, NgayNhap) VALUES
('NVT01', 'NCC01', 5000000, '2026-05-28'),
('NVT02', 'NCC02', 1000000, '2026-05-29');
GO

INSERT INTO CHITIETNHAPVATTU (Ma_NhapVatTu, Ma_VatTuPhuTung, SoLuong, DonGia, ThanhTien) VALUES
('NVT01', 'VT01', 10, 500000, 5000000),
('NVT02', 'VT02', 10, 100000, 1000000);
GO

-- 10. Ban vat tu demo
INSERT INTO BANVATTU (MaBanVatTu, Ma_KhachHang, NgayBan, TongTien) VALUES
('BVT01', 'KH01', '2026-06-01', 120000),
('BVT02', 'KH02', '2026-06-02', 180000);
GO

INSERT INTO CHITIETBANVATTU (Ma_BanVatTu, Ma_VatTuPhuTung, SoLuong, DonGia, ThanhTien) VALUES
('BVT01', 'VT02', 1, 120000, 120000),
('BVT02', 'VT03', 2, 90000, 180000);
GO

-- 11. Chi tiet sua chua
INSERT INTO CHITIETSUACHUAXE
    (MaChiTietSuaChuaXe, Ma_SuaChuaXe, NoiDung, Ma_VatTuPhuTung, SoLuong, DonGia, Ma_TienCong, ThanhTien, SoTienCong)
VALUES
('CTSC01', 'SC01', N'Thay dau may va loc dau', 'VT01', 1, 550000, 'TC01', 630000, 80000),
('CTSC02', 'SC02', N'Kiem tra tong quat', NULL, 0, 0, 'TC02', 150000, 150000),
('CTSC03', 'SC03', N'Thay ma phanh truoc', 'VT04', 2, 450000, 'TC03', 1150000, 250000);
GO

-- 12. Phieu thu tien
INSERT INTO PHIEUTHUTIEN
    (MaPhieuThuTien, Ma_TiepNhanXe, Ma_SuaChuaXe, NgayThuTien, BienSoXe, Email, SoDienThoai, SoTienThu)
VALUES
('PTT01', 'TN01', 'SC01', '2026-06-01', '51A12345', 'an@example.com', '0901000001', 630000),
('PTT02', 'TN02', 'SC02', '2026-06-02', '51B23456', 'bich@example.com', '0901000002', 150000);
GO

-- 13. Bao cao demo
INSERT INTO BAOCAODOANHSO (MaBaoCaoDoanhSo, Thang, TongDoanhThu) VALUES
('BCDS01', '2026-06-01', 1930000);
GO

INSERT INTO CHITIETBAOCAODOANHSO (Ma_BaoCaoDoanhSo, Ma_HieuXe, SoLuotSua, ThanhTien, TiLe) VALUES
('BCDS01', 'HX01', 1, 630000, 32.64),
('BCDS01', 'HX02', 1, 150000, 7.77),
('BCDS01', 'HX04', 1, 1150000, 59.59);
GO

INSERT INTO BAOCAOTON (MaBaoCaoTon, Thang) VALUES
('BCT01', '2026-06-01');
GO

INSERT INTO CHITIETBAOCAOTON (Ma_BaoCaoTon, Ma_VatTuPhuTung, TonDau, TonCuoi, PhatSinh) VALUES
('BCT01', 'VT01', 34, 33, -1),
('BCT01', 'VT02', 28, 27, -1),
('BCT01', 'VT03', 40, 38, -2),
('BCT01', 'VT04', 4, 2, -2),
('BCT01', 'VT05', 0, 0, 0);
GO

-- 14. Tai khoan test: chi giu admin va staff.
INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES
('CV01', N'Quan tri vien'),
('CV02', N'Nhan vien');
GO

INSERT INTO QUYENHAN (MaQuyenHan, TenQuyenHan, NoiDungQuyenHan) VALUES
('QH01', N'Toan quyen', N'Quan ly toan bo he thong'),
('QH02', N'Nghiep vu co ban', N'Tiep nhan, sua chua, thu tien va tra cuu');
GO

INSERT INTO CHITIETCHUCVU (MaChiTietChucVu, Ma_ChucVu, Ma_QuyenHan) VALUES
('CTCV01', 'CV01', 'QH01'),
('CTCV02', 'CV02', 'QH02');
GO

INSERT INTO NGUOIDUNG (MaNguoiDung, TenTaiKhoan, MatKhau, Ma_ChucVu) VALUES
('ND01', 'admin', '123456', 'CV01'),
('ND02', 'staff', '123456', 'CV02');
GO

INSERT INTO THONGTINNGUOIDUNG (MaThongTinNguoiDung, Ma_NguoiDung, HoVaTen, Email, SoDienThoai) VALUES
('TT01', 'ND01', N'Quan tri vien', 'admin@garage.local', '0909000001'),
('TT02', 'ND02', N'Nhan vien demo', 'staff@garage.local', '0909000002');
GO

-- Kiem tra nhanh du lieu quan trong.
SELECT * FROM NGUOIDUNG;
SELECT * FROM CHUCVU;
SELECT * FROM TIEPNHANXE;
SELECT * FROM VATTUPHUTUNG;
SELECT * FROM PHIEUTHUTIEN;
GO

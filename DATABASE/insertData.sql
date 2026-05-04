-- Sử dụng database Garage
USE Garage;
GO

-- 1. HIEUXE
INSERT INTO HIEUXE (MaHieuXe, TenHieuXe) VALUES
('MH01', N'Toyota'),
('MH02', N'Honda'),
('MH03', N'Ford'),
('MH04', N'Hyundai'),
('MH05', N'Vinfast');
GO

-- 2. KHACHHANG
INSERT INTO KHACHHANG (MaKhachHang, TenKhachHang, DiaChiKhachHang, SoDienThoaiKhachHang) VALUES
('KH01', N'Nguyễn Văn An', N'Hà Nội', '0912345678'),
('KH02', N'Trần Thị Bích', N'TP. Hồ Chí Minh', '0987654321'),
('KH03', N'Lê Văn Cửu', N'Đà Nẵng', '0905123456'),
('KH04', N'Phạm Thị Dân', N'Hải Phòng', '0934567890'),
('KH05', N'Hoàng Văn Yến', N'Cần Thơ', '0978123456');
GO

-- 3. TIENCONG
INSERT INTO TIENCONG (MaTienCong, SoTienCong, NoiDungTienCong) VALUES
('TC01', 200000, N'Thay dầu'),
('TC02', 500000, N'Sửa phanh'),
('TC03', 1000000, N'Đại tu sửa động cơ'),
('TC04', 300000, N'Cân chỉnh'),
('TC05', 150000, N'Vệ sinh');
GO

-- 4. VATTUPHUTUNG
INSERT INTO VATTUPHUTUNG (MaVatTuPhuTung, TenVatTuPhuTung, DonGiaVatTuPhuTung, SoLuongVatTuPhuTung, DonViTinh) VALUES
('VT01', N'Dầu máy', 150000, 100, N'Lít'),
('VT02', N'Lọc dầu', 50000, 200, N'Cái'),
('VT03', N'Bugi', 80000, 300, N'Cái'),
('VT04', N'Phanh đĩa', 400000, 50, N'Cái'),
('VT05', N'Lốp xe', 1200000, 30, N'Cái');
GO

-- 5. THONGTINGARAGE (tạo 5 dòng giống nhau để đảm bảo trigger không lỗi)
INSERT INTO THONGTINGARAGE (Id, SoLuongXeToiDa, TongSoHieuXe, SoTienThuSoVoiSoTienNo) VALUES
('GARAGE', 30, 10, 0)
GO

-- 6. TIEPNHANXE (các ngày khác nhau để không vượt quá 30 xe/ngày)
INSERT INTO TIEPNHANXE (MaTiepNhanXe, Ma_KhachHang, BienSoXe, Ma_HieuXe, NgayTiepNhan, TienNo) VALUES
('TN01', 'KH01', '30A-12345', 'MH01', '2025-01-01', 0),
('TN02', 'KH02', '29B-67890', 'MH02', '2025-01-02', 500000),
('TN03', 'KH03', '51C-11111', 'MH03', '2025-01-03', 0),
('TN04', 'KH04', '88D-22222', 'MH04', '2025-01-04', 1000000),
('TN05', 'KH05', '77E-33333', 'MH05', '2025-01-05', 200000);
GO

-- 7. SUACHUAXE (ThanhTien sẽ được trigger cập nhật)
INSERT INTO SUACHUAXE (MaSuaChuaXe, Ma_TiepNhanXe, NgaySuaChua, ThanhTien) VALUES
('SC01', 'TN01', '2025-01-02', 0),
('SC02', 'TN02', '2025-01-03', 0),
('SC03', 'TN03', '2025-01-04', 0),
('SC04', 'TN04', '2025-01-05', 0),
('SC05', 'TN05', '2025-01-06', 0);
GO

-- 8. NHACUNGCAP
INSERT INTO NHACUNGCAP (MaNhaCungCap, TenNhaCungCap, SoDienThoaiNhaCungCap, EmailNhaCungCap) VALUES
('NCC01', N'Vật tư A', '0241234567', 'a@supp.com'),
('NCC02', N'Phụ tùng B', '0287654321', 'b@supp.com'),
('NCC03', N'Linh kiện C', '0248888888', 'c@supp.com'),
('NCC04', N'Dầu nhớt D', '0903123456', 'd@supp.com'),
('NCC05', N'Phanh E', '0912345678', 'e@supp.com');
GO

-- 9. NHAPVATTU (TongTien sẽ được trigger cập nhật)
INSERT INTO NHAPVATTU (MaNhapVatTu, Ma_NhaCungCap, TongTien, NgayNhap) VALUES
('NVT01', 'NCC01', 0, '2024-12-01'),
('NVT02', 'NCC02', 0, '2024-12-02'),
('NVT03', 'NCC03', 0, '2024-12-03'),
('NVT04', 'NCC04', 0, '2024-12-04'),
('NVT05', 'NCC05', 0, '2024-12-05');
GO

-- 10. CHITIETNHAPVATTU (tăng tồn kho)
INSERT INTO CHITIETNHAPVATTU (Ma_NhapVatTu, Ma_VatTuPhuTung, SoLuong, DonGia, ThanhTien) VALUES
('NVT01', 'VT01', 20, 140000, 0),   -- giá nhập rẻ hơn giá bán
('NVT01', 'VT02', 30, 45000, 0),
('NVT02', 'VT03', 40, 75000, 0),
('NVT03', 'VT04', 10, 380000, 0),
('NVT04', 'VT05', 8, 1150000, 0),
('NVT05', 'VT01', 15, 145000, 0);
GO

-- 11. BANVATTU (TongTien sẽ được trigger cập nhật)
INSERT INTO BANVATTU (MaBanVatTu, Ma_KhachHang, NgayBan, TongTien) VALUES
('BVT01', 'KH01', '2025-01-10', 0),
('BVT02', 'KH02', '2025-01-11', 0),
('BVT03', 'KH03', '2025-01-12', 0),
('BVT04', 'KH04', '2025-01-13', 0),
('BVT05', 'KH05', '2025-01-14', 0);
GO

-- 12. CHITIETBANVATTU (giảm tồn kho)
INSERT INTO CHITIETBANVATTU (Ma_BanVatTu, Ma_VatTuPhuTung, SoLuong, DonGia, ThanhTien) VALUES
('BVT01', 'VT01', 2, 150000, 0),
('BVT02', 'VT02', 3, 50000, 0),
('BVT03', 'VT03', 4, 80000, 0),
('BVT04', 'VT04', 1, 400000, 0),
('BVT05', 'VT05', 1, 1200000, 0);
GO

-- 13. CHITIETSUACHUAXE (sửa chữa, giảm tồn kho, tự tính thành tiền)
INSERT INTO CHITIETSUACHUAXE (MaChiTietSuaChuaXe, Ma_SuaChuaXe, NoiDung, Ma_VatTuPhuTung, SoLuong, DonGia, Ma_TienCong, ThanhTien, SoTienCong) VALUES
('CTSC01', 'SC01', N'Thay dầu máy', 'VT01', 5, 150000, 'TC01', 0, 200000),
('CTSC02', 'SC01', N'Thay lọc dầu', 'VT02', 5, 50000, NULL, 0, 0),
('CTSC03', 'SC02', N'Thay bugi', 'VT03', 4, 80000, 'TC04', 0, 300000),
('CTSC04', 'SC03', N'Sửa phanh đĩa', 'VT04', 2, 400000, 'TC02', 0, 500000),
('CTSC05', 'SC04', N'Thay lốp', 'VT05', 2, 1200000, 'TC05', 0, 150000),
('CTSC06', 'SC05', N'Bảo dưỡng tổng thể', 'VT01', 3, 150000, 'TC03', 0, 1000000);
GO

-- 14. PHIEUTHUTIEN (giảm tiền nợ)
INSERT INTO PHIEUTHUTIEN (MaPhieuThuTien, Ma_TiepNhanXe, NgayThuTien, BienSoXe, Email, SoDienThoai, SoTienThu) VALUES
('PTT01', 'TN02', '2025-01-15', '29B-67890', 'khach02@email.com', '0987654321', 300000),
('PTT02', 'TN04', '2025-01-16', '88D-22222', 'khach04@email.com', '0934567890', 500000),
('PTT03', 'TN05', '2025-01-17', '77E-33333', 'khach05@email.com', '0978123456', 200000),
('PTT04', 'TN01', '2025-01-18', '30A-12345', 'khach01@email.com', '0912345678', 0),       -- không thu
('PTT05', 'TN03', '2025-01-19', '51C-11111', 'khach03@email.com', '0905123456', 0);
GO

-- 15. BAOCAODOANHSO (báo cáo doanh số tháng 1/2025)
INSERT INTO BAOCAODOANHSO (MaBaoCaoDoanhSo, Thang, TongDoanhThu) VALUES
('BCDS01', '2025-01-01', 0),  -- trigger không tự tính, ta sẽ cập nhật thủ công sau hoặc để 0
('BCDS02', '2025-01-01', 0),
('BCDS03', '2025-01-01', 0),
('BCDS04', '2025-01-01', 0),
('BCDS05', '2025-01-01', 0);
GO

-- 16. CHITIETBAOCAODOANHSO
INSERT INTO CHITIETBAOCAODOANHSO (Ma_BaoCaoDoanhSo, Ma_HieuXe, SoLuotSua, ThanhTien, TiLe) VALUES
('BCDS01', 'MH01', 1, 0, 0),
('BCDS01', 'MH02', 1, 0, 0),
('BCDS02', 'MH03', 1, 0, 0),
('BCDS03', 'MH04', 1, 0, 0),
('BCDS04', 'MH05', 1, 0, 0);
GO

-- 17. BAOCAOTON (báo cáo tồn tháng 1/2025)
INSERT INTO BAOCAOTON (MaBaoCaoTon, Thang) VALUES
('BCT01', '2025-01-01'),
('BCT02', '2025-01-01'),
('BCT03', '2025-01-01'),
('BCT04', '2025-01-01'),
('BCT05', '2025-01-01');
GO

-- 18. CHITIETBAOCAOTON
-- Cần tính tồn đầu = tồn cuối tháng trước + nhập - bán - sửa, nhưng ở đây fake đơn giản
INSERT INTO CHITIETBAOCAOTON (Ma_BaoCaoTon, Ma_VatTuPhuTung, TonDau, TonCuoi, PhatSinh) VALUES
('BCT01', 'VT01', 100, 120, 35),
('BCT01', 'VT02', 200, 225, 40),
('BCT02', 'VT03', 300, 340, 55),
('BCT03', 'VT04', 50, 55, 12),
('BCT04', 'VT05', 30, 35, 10);
GO

-- 19. Thêm chức vụ
INSERT INTO CHUCVU (MaChucVu, TenChucVu) VALUES ('CV01', N'Quản trị viên (Admin)'), ('CV02', N'Nhân viên (Staff)');
GO

-- 20. Thêm quyền hạn 
INSERT INTO QUYENHAN (MaQuyenHan, TenQuyenHan, NoiDungQuyenHan) VALUES 
('QH01', N'Toàn quyền', N'Được làm mọi thứ'),
('QH02', N'Tiếp nhận xe', N'Chỉ được lập phiếu tiếp nhận');
GO

-- 21. Phân quyền cho chức vụ
INSERT INTO CHITIETCHUCVU (MaChiTietChucVu, Ma_ChucVu, Ma_QuyenHan) VALUES 
('CTCV01', 'CV01', 'QH01'),
('CTCV02', 'CV02', 'QH02');
GO

-- 22. Thêm tài khoản test (Pass test = 123456)
INSERT INTO NGUOIDUNG (MaNguoiDung, TenTaiKhoan, MatKhau, Ma_ChucVu) VALUES 
('ND01', 'admin', '123456', 'CV01'),
('ND02', 'staff1', '123456', 'CV02');
GO

-- Thêm thông tin cho tài khoản
INSERT INTO THONGTINNGUOIDUNG (MaThongTinNguoiDung, Ma_NguoiDung, HoVaTen, Email, SoDienThoai) VALUES 
('TT01', 'ND01', N'Sếp Tổng', 'admin@garage.com', '0999999999'),
('TT02', 'ND02', N'Nhân Viên Quèn', 'staff1@garage.com', '0888888888');
GO

-- Kiểm tra dữ liệu sau khi insert
SELECT * FROM HIEUXE;
SELECT * FROM KHACHHANG;
SELECT * FROM TIENCONG;
SELECT * FROM VATTUPHUTUNG;
SELECT * FROM THONGTINGARAGE;
SELECT * FROM TIEPNHANXE;
SELECT * FROM SUACHUAXE;
SELECT * FROM NHACUNGCAP;
SELECT * FROM NHAPVATTU;
SELECT * FROM CHITIETNHAPVATTU;
SELECT * FROM BANVATTU;
SELECT * FROM CHITIETBANVATTU;
SELECT * FROM CHITIETSUACHUAXE;
SELECT * FROM PHIEUTHUTIEN;
SELECT * FROM BAOCAODOANHSO;
SELECT * FROM CHITIETBAOCAODOANHSO;
SELECT * FROM BAOCAOTON;
SELECT * FROM CHITIETBAOCAOTON;
SELECT * FROM NGUOIDUNG;
SELECT * FROM CHUCVU;
SELECT * FROM QUYENHAN;
SElECT * FROM THONGTINNGUOIDUNG;
SELECT * FROM CHITIETCHUCVU;
GO
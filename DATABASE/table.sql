--Tạo database--
CREATE DATABASE Garage
use Garage;

--Lập bảng--
create table HIEUXE(
    MaHieuXe char(10) primary key,
	TenHieuXe varchar(100) unique
);

create table  KHACHHANG(
    MaKhachHang char(10) primary key,
	TenKhachHang varchar(100),
	DiaChiKhachHang varchar(100),
	SoDienThoaiKhachHang varchar(20) unique
); 

create table TIEPNHANXE(
    MaTiepNhanXe char(10) primary key,
	Ma_KhachHang char(10),
	BienSoXe char(10) unique,
	Ma_HieuXe char(10),
	NgayTiepNhan date,
	TienNo money,
	foreign key (Ma_HieuXe) references HIEUXE(MaHieuXe),
	foreign key (Ma_KhachHang) references KHACHHANG(MaKhachHang)
);

create table SUACHUAXE(
    MaSuaChuaXe char(10) primary key,
	Ma_TiepNhanXe char(10),
	NgaySuaChua date,
	ThanhTien money,
	foreign key (Ma_TiepNhanXe) references TIEPNHANXE(MaTiepNhanXe)
);

create table TIENCONG(
    MaTienCong char(10) primary key,
	SoTienCong money,
	NoiDungTienCong varchar(50)
);

create table VATTUPHUTUNG(
    MaVatTuPhuTung char(10) primary key,
	TenVatTuPhuTung varchar(50),
	DonGiaVatTuPhuTung money,
	SoLuongVatTuPhuTung int,
	DonViTinh varchar(20)
);

create table CHITIETSUACHUAXE(
    MaChiTietSuaChuaXe char(10) primary key,
	Ma_SuaChuaXe char(10),
	NoiDung varchar(100),
	Ma_VatTuPhuTung char(10),
	SoLuong int,
	DonGia money,
	Ma_TienCong char(10),
	ThanhTien money,
	SoTienCong money,
	foreign key(Ma_SuaChuaXe) references SUACHUAXE(MaSuaChuaXe),
	foreign key(Ma_VatTuPhuTung) references VATTUPHUTUNG(MaVatTuPhuTung),
	foreign key(Ma_TienCong) references TIENCONG(MaTienCong)
);


create table THONGTINGARAGE(
    Id char(10) primary key,
    SoLuongXeToiDa int,
	TongSoHieuXe int,
	SoTienThuSoVoiSoTienNo money
);

ALTER TABLE THONGTINGARAGE
ADD SoLuongVatTuToiDa INT DEFAULT 200,
    SoLuongTienCongToiDa INT DEFAULT 100;

UPDATE THONGTINGARAGE
SET SoLuongVatTuToiDa = 200,
    SoLuongTienCongToiDa = 100
WHERE Id = 'GARAGE';

IF COL_LENGTH('THONGTINGARAGE', 'SoLuongVatTuToiDa') IS NULL
BEGIN
    ALTER TABLE THONGTINGARAGE
    ADD SoLuongVatTuToiDa INT NULL;
END
GO

IF COL_LENGTH('THONGTINGARAGE', 'SoLuongTienCongToiDa') IS NULL
BEGIN
    ALTER TABLE THONGTINGARAGE
    ADD SoLuongTienCongToiDa INT NULL;
END
GO

UPDATE THONGTINGARAGE
SET SoLuongVatTuToiDa = ISNULL(SoLuongVatTuToiDa, 200),
    SoLuongTienCongToiDa = ISNULL(SoLuongTienCongToiDa, 100)
WHERE Id = 'GARAGE';
GO

SELECT *
FROM THONGTINGARAGE;
GO

create table BAOCAODOANHSO(
    MaBaoCaoDoanhSo char(10) primary key,
	Thang date,
	TongDoanhThu money
);

create table CHITIETBAOCAODOANHSO(
    Ma_BaoCaoDoanhSo char(10),
	Ma_HieuXe char(10),
	SoLuotSua int,
	ThanhTien money,
	TiLe float,
	foreign key(Ma_BaoCaoDoanhSo) references BAOCAODOANHSO(MaBaoCaoDoanhSo),
	foreign key(Ma_HieuXe) references HIEUXE(MaHieuXe),
	Constraint PK_CTBCDS primary key(Ma_BaoCaoDoanhSo, Ma_HieuXe)
);

create table BAOCAOTON(
    MaBaoCaoTon char(10) primary key,
	Thang date
);

create table CHITIETBAOCAOTON(
    Ma_BaoCaoTon char(10),
	Ma_VatTuPhuTung char(10),
	TonDau int,
	TonCuoi int,
	PhatSinh int,
	foreign key(Ma_BaoCaoTon) references BAOCAOTON(MaBaoCaoTon),
	foreign key(Ma_VatTuPhuTung) references VATTUPHUTUNG(MaVatTuPhuTung),
	constraint PK_CTBCT primary key(Ma_BaoCaoTon, Ma_VatTuPhuTung)
);

create table PHIEUTHUTIEN(
    MaPhieuThuTien char(10) primary key,
	Ma_TiepNhanXe char(10),
	NgayThuTien date,
	BienSoXe char(10),
	Email varchar(50),
	SoDienThoai char(20),
	SoTienThu money,
	foreign key(Ma_TiepNhanXe) references TIEPNHANXE(MaTiepNhanXe)
);

IF COL_LENGTH('PHIEUTHUTIEN', 'Ma_SuaChuaXe') IS NULL
BEGIN
    ALTER TABLE PHIEUTHUTIEN
    ADD Ma_SuaChuaXe CHAR(10) NULL;
END
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.foreign_keys
    WHERE name = 'FK_PHIEUTHUTIEN_SUACHUAXE'
)
BEGIN
    ALTER TABLE PHIEUTHUTIEN
    ADD CONSTRAINT FK_PHIEUTHUTIEN_SUACHUAXE
    FOREIGN KEY (Ma_SuaChuaXe)
    REFERENCES SUACHUAXE(MaSuaChuaXe);
END
GO

IF OBJECT_ID('trg_GiamTienNo_ThuTien', 'TR') IS NOT NULL
BEGIN
    DROP TRIGGER trg_GiamTienNo_ThuTien;
END
GO

create table NHACUNGCAP(
    MaNhaCungCap char(10) primary key,
	TenNhaCungCap varchar(100) unique,
	SoDienThoaiNhaCungCap char(20),
	EmailNhaCungCap varchar(50)
);

create table NHAPVATTU(
    MaNhapVatTu char(10) primary key,
	Ma_NhaCungCap char(10),
	TongTien money,
	NgayNhap date,
	foreign key(Ma_NhaCungCap) references NHACUNGCAP(MaNhaCungCap)
);

create table CHITIETNHAPVATTU(
    Ma_NhapVatTu char(10),
	Ma_VatTuPhuTung char(10),
	SoLuong int,
	DonGia money,
	ThanhTien money,
	foreign key(Ma_NhapVatTu) references NHAPVATTU(MaNhapVatTu),
	foreign key(Ma_VatTuPhuTung) references VATTUPHUTUNG(MaVatTuPhuTung),
	constraint PK_CTNVT primary key(Ma_NhapVatTu, Ma_VatTuPhuTung)
);

create table BANVATTU(
    MaBanVatTu char(10) primary key,
	Ma_KhachHang char(10),
	NgayBan date,
	TongTien money,
	foreign key(Ma_KhachHang) references KHACHHANG(MaKhachHang)
);

create table CHITIETBANVATTU(
   Ma_BanVatTu char(10),
   Ma_VatTuPhuTung char(10),
   SoLuong int,
   DonGia money,
   ThanhTien money,
   foreign key(Ma_BanVatTu) references BANVATTU(MaBanVatTu),
   foreign key(Ma_VatTuPhuTung) references VATTUPHUTUNG(MaVatTuPhuTung),
   constraint PK_CTBVT primary key(Ma_BanVatTu, Ma_VatTuPhuTung)
);

CREATE TABLE CHUCVU (
    MaChucVu CHAR(10) PRIMARY KEY,
    TenChucVu NVARCHAR(50) NOT NULL
);

CREATE TABLE QUYENHAN (
    MaQuyenHan CHAR(10) PRIMARY KEY,
    TenQuyenHan NVARCHAR(50) NOT NULL,
    NoiDungQuyenHan NVARCHAR(255)
);

CREATE TABLE CHITIETCHUCVU (
    MaChiTietChucVu CHAR(10) PRIMARY KEY,
    Ma_ChucVu CHAR(10),
    Ma_QuyenHan CHAR(10),
    FOREIGN KEY (Ma_ChucVu) REFERENCES CHUCVU(MaChucVu),
    FOREIGN KEY (Ma_QuyenHan) REFERENCES QUYENHAN(MaQuyenHan)
);

CREATE TABLE NGUOIDUNG (
    MaNguoiDung CHAR(10) PRIMARY KEY,
    TenTaiKhoan VARCHAR(50) UNIQUE NOT NULL,
    MatKhau VARCHAR(100) NOT NULL,
    Ma_ChucVu CHAR(10),
    FOREIGN KEY (Ma_ChucVu) REFERENCES CHUCVU(MaChucVu)
);

CREATE TABLE THONGTINNGUOIDUNG (
    MaThongTinNguoiDung CHAR(10) PRIMARY KEY,
    Ma_NguoiDung CHAR(10) UNIQUE, -- Một người dùng chỉ có 1 bộ thông tin
    HoVaTen NVARCHAR(100),
    Email VARCHAR(50),
    DiaChi NVARCHAR(200),
    Ngaysinh DATE,
    SoDienThoai VARCHAR(20),
    FOREIGN KEY (Ma_NguoiDung) REFERENCES NGUOIDUNG(MaNguoiDung)
);










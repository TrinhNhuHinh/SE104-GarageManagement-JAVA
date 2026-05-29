# Garage Management System - JavaFX

## 1. Giới thiệu

**Garage Management System** là ứng dụng desktop được xây dựng bằng **JavaFX** nhằm hỗ trợ quản lý hoạt động của một gara sửa chữa xe. Hệ thống cho phép quản lý tiếp nhận xe, sửa chữa, vật tư phụ tùng, hóa đơn thu tiền, tra cứu nhanh, báo cáo thống kê và cấu hình các quy định hoạt động của gara.

Dự án được thực hiện cho môn học **SE104 - Nhập môn Công nghệ phần mềm**.

---

## 2. Công nghệ sử dụng

* **Ngôn ngữ:** Java
* **Giao diện:** JavaFX, FXML, CSS
* **Database:** Microsoft SQL Server
* **Kết nối DB:** JDBC
* **IDE khuyến nghị:** NetBeans
* **Quản lý mã nguồn:** GitHub

---

## 3. Chức năng chính

### 3.1. Đăng nhập / Đăng ký

* Đăng nhập vào hệ thống.
* Lưu thông tin người dùng hiện tại.
* Phân biệt người dùng theo tài khoản và chức vụ.

### 3.2. Trang chủ

* Màn hình điều hướng chính.
* Sidebar gồm các chức năng:

  * Dashboard
  * Intake Service
  * Repair Orders
  * Billing & Collections
  * Quick Lookup
  * Statistical Reports
  * Parts Management
  * Profile
  * Regulations

### 3.3. Tiếp nhận xe

* Thêm, sửa, xóa, tìm kiếm phiếu tiếp nhận xe.
* Lưu thông tin:

  * Mã tiếp nhận xe
  * Mã khách hàng
  * Biển số xe
  * Hiệu xe
  * Ngày tiếp nhận
  * Tiền nợ
* Nếu khách hàng chưa tồn tại, hệ thống có thể tạo khách hàng mới trước khi tiếp nhận xe.
* Kiểm tra số lượng xe tiếp nhận trong ngày theo quy định gara.

### 3.4. Quản lý vật tư phụ tùng

* Thêm, sửa, xóa, tìm kiếm vật tư.
* Quản lý:

  * Mã vật tư
  * Tên vật tư
  * Đơn giá
  * Số lượng tồn
  * Đơn vị tính
* Nhập vật tư từ nhà cung cấp bằng phiếu nhập.
* Bán vật tư trực tiếp cho khách hàng bằng hóa đơn bán vật tư.
* Tự động cập nhật tồn kho khi nhập, bán hoặc sử dụng vật tư trong sửa chữa.
* Kiểm tra số loại vật tư tối đa theo quy định gara.

### 3.5. Lập phiếu sửa chữa

* Tạo phiếu sửa chữa cho xe đã tiếp nhận.
* Chọn vật tư phụ tùng sử dụng.
* Chọn loại tiền công từ danh mục tiền công.
* Tự động tính thành tiền theo công thức:

```text
Thành tiền = Số lượng * Đơn giá vật tư + Tiền công
```

* Khi tạo phiếu sửa chữa:

  * Tồn kho vật tư giảm.
  * Tiền nợ của xe tăng theo tổng tiền sửa chữa.

### 3.6. Thu tiền sửa chữa

* Tạo phiếu thu tiền dựa trên phiếu sửa chữa.
* Chọn mã sửa chữa, hệ thống tự động lấy:

  * Mã tiếp nhận xe
  * Biển số xe
  * Tổng tiền sửa chữa
  * Số tiền đã thu
  * Số tiền còn phải thu
* Không cho thu vượt quá:

  * Tiền nợ hiện tại của xe
  * Số tiền còn lại của phiếu sửa chữa
* Khi tạo phiếu thu:

  * Tiền nợ của xe giảm.
* Khi xóa phiếu thu:

  * Tiền nợ của xe được khôi phục.

### 3.7. Tra cứu nhanh

* Tra cứu thông tin xe, khách hàng, vật tư, sửa chữa.
* Hỗ trợ tìm kiếm theo:

  * Mã tiếp nhận
  * Biển số xe
  * Mã khách hàng
  * Mã vật tư
  * Mã sửa chữa

### 3.8. Báo cáo thống kê

* Thống kê doanh thu.
* Thống kê phiếu sửa chữa.
* Thống kê xe tiếp nhận.
* Thống kê vật tư tồn kho.
* Hiển thị biểu đồ báo cáo bằng JavaFX Chart.

### 3.9. Quy định gara

Hệ thống hỗ trợ thay đổi các quy định:

* Số xe tiếp nhận tối đa trong ngày.
* Số lượng hiệu xe tối đa.
* Số loại vật tư phụ tùng tối đa.
* Số loại tiền công tối đa.

Các quy định được lưu trong bảng `THONGTINGARAGE`.

---

## 4. Quy định nghiệp vụ

### QĐ1

Mỗi xe có một hồ sơ sửa chữa riêng, lưu các thông tin:

* Biển số xe
* Tên chủ xe
* Điện thoại
* Địa chỉ
* Hiệu xe

Mỗi ngày gara tiếp nhận tối đa số lượng xe theo quy định trong hệ thống.

### QĐ2

Gara có danh mục vật tư phụ tùng và danh mục tiền công.

Công thức tính tiền sửa chữa:

```text
Thành tiền = Số lượng * Đơn giá + Tiền công
```

### QĐ4

Số tiền thu không được vượt quá số tiền khách hàng đang nợ.

### QĐ6

Người dùng có thể thay đổi các quy định:

* Số lượng hiệu xe.
* Số xe sửa chữa tối đa trong ngày.
* Số loại vật tư phụ tùng.
* Số loại tiền công.

---

## 5. Cấu trúc thư mục

```text
SE104-GarageManagement-JAVA/
│
├── src/
│   ├── Config/
│   │   └── DBConnection.java
│   │
│   ├── Controllers/
│   │   ├── HomeController.java
│   │   ├── IntakeServiceController.java
│   │   ├── PartsManagementController.java
│   │   ├── RepairOrdersController.java
│   │   ├── BillingCollectionsController.java
│   │   ├── QuickLookupController.java
│   │   ├── StatisticalReportsController.java
│   │   ├── ProfileController.java
│   │   └── RegulationsController.java
│   │
│   ├── DAO/
│   │   ├── KhachHangDAO.java
│   │   ├── TiepNhanXeDAO.java
│   │   ├── VatTuPhuTungDAO.java
│   │   ├── SuaChuaXeDAO.java
│   │   ├── ChiTietSuaChuaXeDAO.java
│   │   ├── PhieuThuTienDAO.java
│   │   ├── BanVatTuDAO.java
│   │   ├── ChiTietBanVatTuDAO.java
│   │   ├── NhapVatTuDAO.java
│   │   ├── ChiTietNhapVatTuDAO.java
│   │   ├── TienCongDAO.java
│   │   └── ThongTinGarageDAO.java
│   │
│   ├── MODEL/
│   │   ├── KhachHang.java
│   │   ├── TiepNhanXe.java
│   │   ├── VatTuPhuTung.java
│   │   ├── SuaChuaXe.java
│   │   ├── ChiTietSuaChuaXe.java
│   │   ├── PhieuThuTien.java
│   │   ├── BanVatTu.java
│   │   ├── ChiTietBanVatTu.java
│   │   ├── NhapVatTu.java
│   │   ├── ChiTietNhapVatTu.java
│   │   ├── TienCong.java
│   │   └── ThongTinGarage.java
│   │
│   ├── Service/
│   │   ├── AuthService.java
│   │   ├── TiepNhanXeService.java
│   │   ├── VatTuPhuTungService.java
│   │   ├── SuaChuaXeService.java
│   │   ├── PhieuThuTienService.java
│   │   ├── TienCongService.java
│   │   └── ThongTinGarageService.java
│   │
│   ├── Views/
│   │   ├── LognReg.fxml
│   │   ├── Home.fxml
│   │   ├── Dashboard.fxml
│   │   ├── IntakeService.fxml
│   │   ├── PartsManagement.fxml
│   │   ├── RepairOrders.fxml
│   │   ├── BillingCollections.fxml
│   │   ├── QuickLookup.fxml
│   │   ├── StatisticalReports.fxml
│   │   ├── Profile.fxml
│   │   └── Regulations.fxml
│   │
│   └── Assets/
│       └── css/
│           ├── home.css
│           └── pages.css
│
├── DATABASE/
│   ├── table.sql
│   ├── insertData.sql
│   └── Trigger.sql
│
└── README.md
```

---

## 6. Cài đặt và chạy project

### Bước 1: Clone project

```bash
git clone https://github.com/TrinhNhuHinh/SE104-GarageManagement-JAVA.git
```

### Bước 2: Mở project bằng NetBeans

Mở NetBeans và chọn:

```text
File -> Open Project -> SE104-GarageManagement-JAVA
```

### Bước 3: Tạo database SQL Server

Mở SQL Server Management Studio, chạy lần lượt các file trong thư mục `DATABASE`:

```text
1. table.sql
2. insertData.sql
3. Trigger.sql
```

### Bước 4: Cấu hình kết nối database

Mở file:

```text
src/Config/DBConnection.java
```

Kiểm tra và sửa thông tin kết nối cho phù hợp với máy:

```java
String url = "jdbc:sqlserver://localhost:1433;databaseName=Garage;encrypt=true;trustServerCertificate=true";
String user = "sa";
String password = "your_password";
```

### Bước 5: Cấu hình JavaFX

Trong NetBeans, thêm VM Options:

```bash
--module-path "PATH_TO_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
```

Ví dụ:

```bash
--module-path "C:\Users\YourName\Downloads\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml
```

### Bước 6: Run project

Chạy file main của project để mở ứng dụng.

---

## 7. Luồng demo đề xuất

### Luồng 1: Tiếp nhận xe mới

1. Đăng nhập.
2. Vào `Intake Service`.
3. Nhập thông tin xe.
4. Nếu khách hàng chưa tồn tại, tạo khách hàng mới.
5. Lưu phiếu tiếp nhận.

### Luồng 2: Nhập vật tư

1. Vào `Parts Management`.
2. Chọn tab `Import Invoice`.
3. Chọn nhà cung cấp, vật tư, số lượng, đơn giá.
4. Tạo phiếu nhập.
5. Kiểm tra tồn kho vật tư tăng.

### Luồng 3: Sửa chữa xe

1. Vào `Repair Orders`.
2. Chọn mã tiếp nhận xe.
3. Chọn vật tư.
4. Chọn loại tiền công.
5. Nhập số lượng và nội dung sửa chữa.
6. Tạo phiếu sửa chữa.
7. Kiểm tra tiền nợ xe tăng và tồn kho vật tư giảm.

### Luồng 4: Thu tiền

1. Vào `Billing & Collections`.
2. Chọn mã sửa chữa.
3. Hệ thống tự tính số tiền cần thu.
4. Tạo phiếu thu.
5. Kiểm tra tiền nợ xe giảm.

### Luồng 5: Thay đổi quy định

1. Vào `Regulations`.
2. Thay đổi số xe tiếp nhận tối đa trong ngày.
3. Thử tiếp nhận vượt giới hạn.
4. Hệ thống chặn theo quy định mới.

---

## 8. Một số bảng dữ liệu chính

* `NGUOIDUNG`: quản lý tài khoản người dùng.
* `KHACHHANG`: quản lý khách hàng.
* `HIEUXE`: quản lý hiệu xe.
* `TIEPNHANXE`: quản lý phiếu tiếp nhận xe.
* `VATTUPHUTUNG`: quản lý vật tư phụ tùng.
* `NHAPVATTU`: quản lý phiếu nhập vật tư.
* `CHITIETNHAPVATTU`: chi tiết phiếu nhập vật tư.
* `BANVATTU`: quản lý hóa đơn bán vật tư.
* `CHITIETBANVATTU`: chi tiết hóa đơn bán vật tư.
* `TIENCONG`: quản lý danh mục tiền công.
* `SUACHUAXE`: quản lý phiếu sửa chữa.
* `CHITIETSUACHUAXE`: chi tiết sửa chữa.
* `PHIEUTHUTIEN`: quản lý phiếu thu tiền.
* `THONGTINGARAGE`: quản lý quy định gara.

---

## 9. Thành viên thực hiện

* Trình Như Hinh
* Nguyễn Đức Kiên
* Bùi Minh Khôi
* Nguyễn Ngọc Huy Hoàng
* Nguyễn Ngọc Đăng Khoa

---

## 10. Ghi chú

Project đang trong quá trình hoàn thiện cho mục đích học tập. Một số chức năng có thể tiếp tục được mở rộng như:

* Phân quyền chi tiết theo vai trò.
* Xuất báo cáo ra PDF hoặc Excel.
* Tối ưu giao diện.
* Thêm quản lý danh mục hiệu xe.
* Thêm quản lý danh mục tiền công bằng giao diện riêng.
* Thống kê nâng cao theo tháng, quý, năm.

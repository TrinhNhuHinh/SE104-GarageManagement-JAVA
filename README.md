# Garage Management System - JavaFX

## 1. Giới thiệu

**Garage Management System** là ứng dụng desktop được xây dựng bằng **JavaFX** nhằm hỗ trợ quản lý hoạt động của một gara sửa chữa xe. Hệ thống hỗ trợ các nghiệp vụ chính như tiếp nhận xe, quản lý khách hàng, lập phiếu sửa chữa, quản lý vật tư phụ tùng, nhập vật tư, bán vật tư, thu tiền sửa chữa, tra cứu nhanh, báo cáo thống kê và thay đổi quy định hoạt động của gara.

Dự án được thực hiện cho môn học **SE104 - Nhập môn Công nghệ phần mềm**.

---

## 2. Công nghệ sử dụng

* **Ngôn ngữ:** Java
* **Giao diện:** JavaFX, FXML, CSS
* **Database:** Microsoft SQL Server
* **Kết nối database:** JDBC
* **IDE khuyến nghị:** NetBeans
* **Quản lý mã nguồn:** GitHub

---

## 3. Chức năng chính

### 3.1. Đăng nhập / Đăng ký

Hệ thống hỗ trợ đăng nhập và đăng ký tài khoản người dùng.

Chức năng chính:

* Đăng ký tài khoản mới.
* Đăng nhập vào hệ thống.
* Lưu thông tin người dùng hiện tại sau khi đăng nhập.
* Cho phép đổi tên tài khoản và mật khẩu trong phần Settings.

---

### 3.2. Trang chủ

Sau khi đăng nhập thành công, người dùng được chuyển đến màn hình chính `Home`.

Sidebar gồm các chức năng:

* Dashboard
* Intake Service
* Repair Orders
* Billing & Collections
* Quick Lookup
* Statistical Reports
* Parts Management
* Setting
* Regulations

Khi bấm vào logo của hệ thống, giao diện sẽ quay về Dashboard.

---

### 3.3. Dashboard

Dashboard hiển thị tổng quan dữ liệu của hệ thống.

Các thông tin chính:

* Tổng số khách hàng.
* Tổng số xe đã tiếp nhận.
* Tổng số phiếu sửa chữa.
* Tổng doanh thu từ phiếu thu.
* Tổng số xe đã tiếp nhận.
* Tổng số phiếu sửa chữa.
* Tổng số phiếu thu.
* Số vật tư sắp hết hàng.
* Danh sách hoạt động gần đây:

  * Tiếp nhận xe.
  * Sửa chữa xe.
  * Thu tiền.
  * Cảnh báo tồn kho thấp.

Dashboard hiện dùng dạng **System Summary** thay vì chỉ đếm theo ngày hiện tại để tránh trường hợp dữ liệu mẫu không nằm trong ngày hôm nay dẫn đến toàn bộ số liệu hiển thị là 0.

---

### 3.4. Tiếp nhận xe

Chức năng `Intake Service` dùng để tiếp nhận xe vào gara.

Thông tin tiếp nhận gồm:

* Mã tiếp nhận xe.
* Mã khách hàng.
* Biển số xe.
* Hiệu xe.
* Ngày tiếp nhận.
* Tiền nợ ban đầu.

Chức năng hỗ trợ:

* Thêm phiếu tiếp nhận xe.
* Cập nhật thông tin tiếp nhận.
* Xóa phiếu tiếp nhận.
* Tìm kiếm phiếu tiếp nhận.
* Tự tạo khách hàng mới nếu mã khách hàng chưa tồn tại trong database.

Khi thêm xe mới, hệ thống kiểm tra quy định số xe tiếp nhận tối đa trong ngày theo bảng `THONGTINGARAGE`.

---

### 3.5. Quản lý vật tư phụ tùng

Chức năng `Parts Management` dùng để quản lý vật tư phụ tùng trong gara.

Thông tin vật tư gồm:

* Mã vật tư.
* Tên vật tư.
* Đơn vị tính.
* Số lượng tồn.
* Đơn giá niêm yết.

Chức năng chính:

* Thêm vật tư.
* Cập nhật vật tư.
* Xóa vật tư.
* Tìm kiếm vật tư.
* Kiểm tra tồn kho.
* Cảnh báo vật tư sắp hết hàng.

Hệ thống kiểm tra số loại vật tư tối đa theo quy định trong bảng `THONGTINGARAGE`.

---

### 3.6. Nhập vật tư

Trong `Parts Management`, tab `Import Invoice` dùng để tạo phiếu nhập vật tư từ nhà cung cấp.

Thông tin phiếu nhập gồm:

* Mã phiếu nhập.
* Mã nhà cung cấp.
* Ngày nhập.
* Mã vật tư.
* Số lượng nhập.
* Đơn giá nhập.
* Thành tiền.

Khi tạo phiếu nhập:

* Hệ thống thêm dữ liệu vào bảng `NHAPVATTU`.
* Hệ thống thêm dữ liệu vào bảng `CHITIETNHAPVATTU`.
* Số lượng tồn của vật tư được tăng lên.

---

### 3.7. Bán vật tư

Trong `Parts Management`, tab `Sale Invoice` dùng để bán vật tư trực tiếp cho khách hàng.

Thông tin hóa đơn bán vật tư gồm:

* Mã hóa đơn bán vật tư.
* Mã khách hàng.
* Ngày bán.
* Mã vật tư.
* Số lượng bán.
* Đơn giá bán.
* Thành tiền.

Khi tạo hóa đơn bán vật tư:

* Hệ thống thêm dữ liệu vào bảng `BANVATTU`.
* Hệ thống thêm dữ liệu vào bảng `CHITIETBANVATTU`.
* Số lượng tồn của vật tư được giảm xuống.
* Không cần tiếp nhận xe nếu khách hàng chỉ mua vật tư.

---

### 3.8. Lập phiếu sửa chữa

Chức năng `Repair Orders` dùng để lập phiếu sửa chữa cho xe đã được tiếp nhận.

Thông tin phiếu sửa chữa gồm:

* Mã sửa chữa.
* Mã tiếp nhận xe.
* Ngày sửa chữa.
* Mã vật tư sử dụng.
* Số lượng vật tư.
* Mã tiền công.
* Nội dung sửa chữa.
* Thành tiền.

Tiền công không nhập tay mà được chọn từ danh mục `TIENCONG`.

Công thức tính thành tiền:

```text
Thành tiền = Số lượng * Đơn giá vật tư + Tiền công
```

Khi tạo phiếu sửa chữa:

* Hệ thống thêm dữ liệu vào bảng `SUACHUAXE`.
* Hệ thống thêm dữ liệu vào bảng `CHITIETSUACHUAXE`.
* Số lượng tồn của vật tư được giảm xuống.
* Tiền nợ của xe trong bảng `TIEPNHANXE` được cộng thêm theo tổng tiền sửa chữa.

---

### 3.9. Thu tiền sửa chữa

Chức năng `Billing & Collections` dùng để tạo phiếu thu tiền sửa chữa.

Flow hiện tại:

```text
Repair Orders
→ tạo phiếu sửa chữa
→ phát sinh tổng tiền sửa chữa
→ cộng tiền nợ cho xe
→ Billing chọn mã sửa chữa
→ hệ thống tự tính số tiền cần thu
→ tạo phiếu thu
→ giảm tiền nợ của xe
```

Thông tin phiếu thu gồm:

* Mã phiếu thu.
* Mã sửa chữa.
* Mã tiếp nhận xe.
* Biển số xe.
* Ngày thu tiền.
* Số điện thoại khách hàng.
* Số tiền thu.

Khi chọn mã sửa chữa, hệ thống tự động lấy:

* Mã tiếp nhận xe.
* Tổng tiền sửa chữa.
* Số tiền đã thu cho phiếu sửa chữa đó.
* Số tiền còn phải thu.
* Tiền nợ hiện tại của xe.

Hệ thống không cho thu tiền tùy ý. Số tiền thu phải thỏa:

```text
Số tiền thu <= Số tiền còn lại của phiếu sửa chữa
Số tiền thu <= Tiền nợ hiện tại của xe
```

Khi xóa phiếu thu:

* Tiền nợ của xe được khôi phục lại.

---

### 3.10. Tra cứu nhanh

Chức năng `Quick Lookup` dùng để tra cứu nhanh các dữ liệu quan trọng trong hệ thống.

Có thể tra cứu:

* Xe đã tiếp nhận.
* Khách hàng.
* Vật tư phụ tùng.
* Phiếu sửa chữa.

Hỗ trợ tìm kiếm theo:

* Mã tiếp nhận xe.
* Biển số xe.
* Mã khách hàng.
* Tên khách hàng.
* Số điện thoại.
* Mã vật tư.
* Mã sửa chữa.

---

### 3.11. Báo cáo thống kê

Chức năng `Statistical Reports` dùng để xem báo cáo hoạt động của gara.

Các loại báo cáo:

* Báo cáo doanh thu.
* Báo cáo sửa chữa.
* Báo cáo tồn kho.
* Báo cáo hiệu xe.

Hệ thống hiển thị:

* Các thẻ tổng quan.
* Bảng dữ liệu chi tiết.
* Biểu đồ bằng JavaFX Chart.

Doanh thu có thể bao gồm:

* Doanh thu từ phiếu thu sửa chữa.
* Doanh thu từ bán vật tư trực tiếp.

---

### 3.12. Setting

Chức năng `Setting` dùng để quản lý tài khoản hiện tại và các danh mục nền của hệ thống.

Setting được chia thành các tab:

#### Profile

* Xem thông tin người dùng hiện tại.
* Cập nhật tên tài khoản.
* Đổi mật khẩu.
* Đăng xuất.

#### Customers

Quản lý danh mục khách hàng.

Thông tin gồm:

* Mã khách hàng.
* Tên khách hàng.
* Số điện thoại.
* Địa chỉ.

Chức năng:

* Thêm khách hàng.
* Cập nhật khách hàng.
* Xóa khách hàng.
* Hiển thị danh sách khách hàng.

#### Labor Fee

Quản lý danh mục tiền công.

Thông tin gồm:

* Mã tiền công.
* Nội dung tiền công.
* Số tiền công.

Chức năng:

* Thêm tiền công.
* Cập nhật tiền công.
* Xóa tiền công.
* Hiển thị danh sách tiền công.

Hệ thống kiểm tra số lượng loại tiền công tối đa theo quy định trong bảng `THONGTINGARAGE`.

#### Suppliers

Quản lý danh mục nhà cung cấp.

Thông tin gồm:

* Mã nhà cung cấp.
* Tên nhà cung cấp.
* Số điện thoại.
* Email.

Chức năng:

* Thêm nhà cung cấp.
* Cập nhật nhà cung cấp.
* Xóa nhà cung cấp.
* Hiển thị danh sách nhà cung cấp.

#### Car Brands

Quản lý danh mục hiệu xe.

Thông tin gồm:

* Mã hiệu xe.
* Tên hiệu xe.

Chức năng:

* Thêm hiệu xe.
* Cập nhật hiệu xe.
* Xóa hiệu xe.
* Hiển thị danh sách hiệu xe.

Hệ thống kiểm tra số lượng hiệu xe tối đa theo quy định trong bảng `THONGTINGARAGE`.

---

### 3.13. Regulations

Chức năng `Regulations` dùng để thay đổi các quy định hoạt động của gara.

Các quy định có thể thay đổi:

* Số xe tiếp nhận tối đa trong ngày.
* Số lượng hiệu xe tối đa.
* Số loại vật tư phụ tùng tối đa.
* Số loại tiền công tối đa.

Dữ liệu quy định được lưu trong bảng `THONGTINGARAGE`.

---

## 4. Quy định nghiệp vụ

### QĐ1

Mỗi xe có một hồ sơ sửa chữa riêng, lưu đầy đủ thông tin:

* Biển số xe.
* Tên chủ xe.
* Điện thoại.
* Địa chỉ.
* Hiệu xe.

Gara có danh mục hiệu xe. Số lượng hiệu xe tối đa có thể thay đổi trong phần `Regulations`.

Mỗi ngày gara chỉ tiếp nhận tối đa số lượng xe theo quy định.

Ví dụ mặc định:

```text
Số xe tiếp nhận tối đa trong ngày = 30
Số lượng hiệu xe tối đa = 10
```

---

### QĐ2

Gara có danh mục vật tư phụ tùng và danh mục tiền công.

Ví dụ mặc định:

```text
Số loại vật tư phụ tùng tối đa = 200
Số loại tiền công tối đa = 100
```

Công thức tính tiền sửa chữa:

```text
Thành tiền = Số lượng * Đơn giá vật tư + Tiền công
```

Tiền công được chọn từ bảng `TIENCONG`, không nhập tự do trong phiếu sửa chữa.

---

### QĐ4

Số tiền thu không được vượt quá số tiền khách hàng đang nợ.

Trong hệ thống, phiếu thu được tạo dựa trên phiếu sửa chữa. Khi thu tiền, hệ thống kiểm tra:

```text
Số tiền thu <= Tiền nợ hiện tại của xe
Số tiền thu <= Số tiền còn lại của phiếu sửa chữa
```

---

### QĐ6

Người dùng có thể thay đổi các quy định:

* Thay đổi số lượng hiệu xe.
* Thay đổi số xe sửa chữa tối đa trong ngày.
* Thay đổi số loại vật tư phụ tùng.
* Thay đổi số loại tiền công.

Các quy định được quản lý trong chức năng `Regulations`.

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
│   │   ├── DashboardController.java
│   │   ├── IntakeServiceController.java
│   │   ├── PartsManagementController.java
│   │   ├── RepairOrdersController.java
│   │   ├── BillingCollectionsController.java
│   │   ├── QuickLookupController.java
│   │   ├── StatisticalReportsController.java
│   │   ├── SettingsController.java
│   │   └── RegulationsController.java
│   │
│   ├── DAO/
│   │   ├── KhachHangDAO.java
│   │   ├── HieuXeDAO.java
│   │   ├── NhaCungCapDAO.java
│   │   ├── TienCongDAO.java
│   │   ├── ThongTinGarageDAO.java
│   │   ├── TiepNhanXeDAO.java
│   │   ├── VatTuPhuTungDAO.java
│   │   ├── SuaChuaXeDAO.java
│   │   ├── ChiTietSuaChuaXeDAO.java
│   │   ├── PhieuThuTienDAO.java
│   │   ├── NhapVatTuDAO.java
│   │   ├── ChiTietNhapVatTuDAO.java
│   │   ├── BanVatTuDAO.java
│   │   └── ChiTietBanVatTuDAO.java
│   │
│   ├── MODEL/
│   │   ├── NguoiDung.java
│   │   ├── KhachHang.java
│   │   ├── HieuXe.java
│   │   ├── NhaCungCap.java
│   │   ├── TienCong.java
│   │   ├── ThongTinGarage.java
│   │   ├── TiepNhanXe.java
│   │   ├── VatTuPhuTung.java
│   │   ├── SuaChuaXe.java
│   │   ├── ChiTietSuaChuaXe.java
│   │   ├── PhieuThuTien.java
│   │   ├── NhapVatTu.java
│   │   ├── ChiTietNhapVatTu.java
│   │   ├── BanVatTu.java
│   │   └── ChiTietBanVatTu.java
│   │
│   ├── Service/
│   │   ├── AuthService.java
│   │   ├── TiepNhanXeService.java
│   │   ├── VatTuPhuTungService.java
│   │   ├── SuaChuaXeService.java
│   │   ├── PhieuThuTienService.java
│   │   ├── TienCongService.java
│   │   ├── HieuXeService.java
│   │   ├── NhaCungCapService.java
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
│   │   ├── Setting.fxml
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

## 6. Database chính

Một số bảng quan trọng:

| Bảng               | Chức năng                       |
| ------------------ | ------------------------------- |
| `NGUOIDUNG`        | Quản lý tài khoản người dùng    |
| `KHACHHANG`        | Quản lý khách hàng              |
| `HIEUXE`           | Quản lý hiệu xe                 |
| `NHACUNGCAP`       | Quản lý nhà cung cấp            |
| `TIENCONG`         | Quản lý danh mục tiền công      |
| `THONGTINGARAGE`   | Quản lý quy định gara           |
| `TIEPNHANXE`       | Quản lý tiếp nhận xe            |
| `VATTUPHUTUNG`     | Quản lý vật tư phụ tùng         |
| `NHAPVATTU`        | Quản lý phiếu nhập vật tư       |
| `CHITIETNHAPVATTU` | Chi tiết phiếu nhập vật tư      |
| `BANVATTU`         | Quản lý hóa đơn bán vật tư      |
| `CHITIETBANVATTU`  | Chi tiết hóa đơn bán vật tư     |
| `SUACHUAXE`        | Quản lý phiếu sửa chữa          |
| `CHITIETSUACHUAXE` | Chi tiết sửa chữa               |
| `PHIEUTHUTIEN`     | Quản lý phiếu thu tiền sửa chữa |

---

## 7. Cài đặt và chạy project

### Bước 1: Clone project

```bash
git clone https://github.com/TrinhNhuHinh/SE104-GarageManagement-JAVA.git
```

---

### Bước 2: Mở project bằng NetBeans

Mở NetBeans và chọn:

```text
File -> Open Project -> SE104-GarageManagement-JAVA
```

---

### Bước 3: Tạo database SQL Server

Mở SQL Server Management Studio và chạy lần lượt các file trong thư mục `DATABASE`:

```text
1. table.sql
2. insertData.sql
3. Trigger.sql
```

Nếu database đã tồn tại và có thay đổi mới, cần kiểm tra các cột mới trong bảng `THONGTINGARAGE`:

```sql
SELECT *
FROM THONGTINGARAGE;
```

Các cột cần có:

```text
SoLuongXeToiDa
TongSoHieuXe
SoTienThuSoVoiSoTienNo
SoLuongVatTuToiDa
SoLuongTienCongToiDa
```

Nếu chưa có, có thể thêm bằng SQL:

```sql
ALTER TABLE THONGTINGARAGE
ADD SoLuongVatTuToiDa INT NULL,
    SoLuongTienCongToiDa INT NULL;

UPDATE THONGTINGARAGE
SET SoLuongVatTuToiDa = 200,
    SoLuongTienCongToiDa = 100
WHERE Id = 'GARAGE';
```

---

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

---

### Bước 5: Cấu hình JavaFX

Trong NetBeans, thêm VM Options:

```bash
--module-path "PATH_TO_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
```

Ví dụ:

```bash
--module-path "C:\Users\YourName\Downloads\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml
```

---

### Bước 6: Run project

Chạy file main của project để mở ứng dụng.

---

## 8. Luồng demo đề xuất

### Luồng 1: Tiếp nhận xe mới

1. Đăng nhập.
2. Vào `Intake Service`.
3. Nhập thông tin tiếp nhận xe.
4. Nếu khách hàng chưa tồn tại, hệ thống hỏi tạo khách hàng mới.
5. Lưu phiếu tiếp nhận.
6. Kiểm tra danh sách tiếp nhận xe.

---

### Luồng 2: Nhập vật tư

1. Vào `Parts Management`.
2. Chọn tab `Import Invoice`.
3. Chọn nhà cung cấp.
4. Chọn vật tư.
5. Nhập số lượng và đơn giá.
6. Tạo phiếu nhập.
7. Kiểm tra số lượng tồn tăng.

---

### Luồng 3: Bán vật tư trực tiếp

1. Vào `Parts Management`.
2. Chọn tab `Sale Invoice`.
3. Chọn khách hàng.
4. Chọn vật tư.
5. Nhập số lượng bán.
6. Tạo hóa đơn bán vật tư.
7. Kiểm tra số lượng tồn giảm.

---

### Luồng 4: Lập phiếu sửa chữa

1. Vào `Repair Orders`.
2. Chọn mã tiếp nhận xe.
3. Chọn vật tư phụ tùng.
4. Chọn loại tiền công.
5. Nhập số lượng vật tư.
6. Nhập nội dung sửa chữa.
7. Tạo phiếu sửa chữa.
8. Kiểm tra:

   * Tồn kho vật tư giảm.
   * Tiền nợ của xe tăng.
   * Thành tiền được tính theo vật tư và tiền công.

---

### Luồng 5: Thu tiền sửa chữa

1. Vào `Billing & Collections`.
2. Chọn mã sửa chữa.
3. Hệ thống tự lấy tổng tiền sửa chữa.
4. Hệ thống tự tính số tiền còn phải thu.
5. Tạo phiếu thu.
6. Kiểm tra tiền nợ của xe giảm.
7. Thử tạo phiếu thu lần nữa cho cùng phiếu sửa chữa để kiểm tra hệ thống không cho thu vượt.

---

### Luồng 6: Quản lý danh mục trong Setting

1. Vào `Setting`.
2. Kiểm tra các tab:

   * Profile.
   * Customers.
   * Labor Fee.
   * Suppliers.
   * Car Brands.
3. Thêm, sửa, xóa dữ liệu danh mục.
4. Kiểm tra dữ liệu được cập nhật ở các màn liên quan.

---

### Luồng 7: Thay đổi quy định

1. Vào `Regulations`.
2. Thay đổi số xe tiếp nhận tối đa trong ngày.
3. Thử tiếp nhận vượt số lượng đó.
4. Hệ thống chặn theo quy định mới.
5. Thay đổi số loại vật tư hoặc số loại tiền công tối đa.
6. Thử thêm vượt giới hạn để kiểm tra.

---

## 9. Ghi chú kỹ thuật

### 9.1. Billing không nhập tiền tự do

Phiếu thu hiện được tạo dựa trên phiếu sửa chữa.

Điều này giúp tiền thu liên kết trực tiếp với:

* Vật tư sử dụng.
* Số lượng vật tư.
* Đơn giá vật tư.
* Tiền công.
* Tổng tiền sửa chữa.
* Tiền nợ của xe.

---

### 9.2. Tiền công lấy từ danh mục

Trong màn sửa chữa, tiền công được chọn từ bảng `TIENCONG`, không nhập trực tiếp.

Điều này giúp hệ thống đúng với quy định QĐ2.

---

### 9.3. Quy định được lưu trong database

Các quy định hoạt động được lưu trong bảng `THONGTINGARAGE`, không hard-code cố định trong chương trình.

---

### 9.4. Không nên xóa dữ liệu đang được tham chiếu

Một số dữ liệu danh mục không thể xóa nếu đã được dùng ở bảng khác.

Ví dụ:

* Không nên xóa khách hàng đã có phiếu tiếp nhận.
* Không nên xóa hiệu xe đã được dùng trong tiếp nhận xe.
* Không nên xóa nhà cung cấp đã có phiếu nhập.
* Không nên xóa tiền công đã được dùng trong phiếu sửa chữa.
* Không nên xóa vật tư đã được dùng trong nhập, bán hoặc sửa chữa.

---

## 10. Thành viên thực hiện

* Trình Như Hinh
* Nguyễn Ngọc Huy Hoàng
* Nguyễn Đức Kiên
* Bùi Minh Khôi
* Nguyễn Ngọc Đăng Khoa

---

## 11. Hướng phát triển thêm

Một số chức năng có thể phát triển thêm:

* Phân quyền chi tiết theo chức vụ.
* Xuất báo cáo ra Excel hoặc PDF.
* In phiếu sửa chữa.
* In hóa đơn thu tiền.
* Thống kê nâng cao theo tháng, quý, năm.
* Tìm kiếm nâng cao.
* Giao diện responsive hơn.
* Kiểm tra dữ liệu nhập chặt chẽ hơn.
* Mã hóa mật khẩu người dùng.

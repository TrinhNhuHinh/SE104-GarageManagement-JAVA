# Garage Management System

## Giới thiệu

**Garage Management System** là ứng dụng desktop JavaFX hỗ trợ quản lý hoạt động của một gara ô tô. Ứng dụng tập trung vào các nghiệp vụ chính như tiếp nhận xe, lập phiếu sửa chữa, quản lý vật tư phụ tùng, thu tiền, tra cứu và lập báo cáo.

Dự án được thực hiện cho môn **SE104 - Nhập môn Công nghệ phần mềm**.

## Công nghệ sử dụng

* Java
* JavaFX, FXML, CSS
* Microsoft SQL Server
* JDBC SQL Server
* NetBeans/Ant project

## Chức năng đã hoàn thành

### Đăng nhập và phân quyền

Ứng dụng hỗ trợ đăng nhập bằng tài khoản người dùng và phân quyền theo vai trò:

* **Quản trị viên:** được sử dụng đầy đủ chức năng.
* **Nhân viên:** được sử dụng các nghiệp vụ cơ bản như tiếp nhận xe, sửa chữa, thu tiền, tra cứu và cập nhật hồ sơ cá nhân.

### Tổng quan

Màn hình tổng quan hiển thị các số liệu chính của gara:

* Tổng khách hàng.
* Tổng xe đã tiếp nhận.
* Tổng phiếu sửa chữa.
* Doanh thu.
* Công nợ còn lại.
* Trạng thái vật tư tồn kho.
* Các hoạt động gần đây.

### Tiếp nhận xe

Quản lý hồ sơ tiếp nhận xe của khách hàng:

* Thêm, cập nhật, xóa phiếu tiếp nhận.
* Tìm kiếm phiếu tiếp nhận.
* Lưu thông tin khách hàng, biển số, hiệu xe, ngày tiếp nhận và tiền nợ.

### Phiếu sửa chữa

Quản lý thông tin sửa chữa xe:

* Lập phiếu sửa chữa theo xe đã tiếp nhận.
* Chọn vật tư phụ tùng và tiền công.
* Tính thành tiền sửa chữa.
* Cập nhật số lượng tồn kho khi sử dụng vật tư.

### Thu tiền

Quản lý phiếu thu tiền sửa chữa:

* Tạo phiếu thu dựa trên phiếu sửa chữa.
* Theo dõi tổng tiền, số tiền đã thu và số tiền còn lại.
* Cập nhật công nợ sau khi thu tiền.

### Tra cứu nhanh

Tra cứu thông tin trong hệ thống:

* Xe đã tiếp nhận.
* Khách hàng.
* Vật tư phụ tùng.
* Phiếu sửa chữa.

### Quản lý vật tư

Quản lý vật tư phụ tùng trong gara:

* Thêm, sửa, xóa vật tư.
* Nhập vật tư từ nhà cung cấp.
* Bán vật tư trực tiếp.
* Theo dõi tồn kho.

### Báo cáo thống kê

Hệ thống hỗ trợ 2 loại báo cáo:

* Báo cáo doanh thu theo tháng và năm.
* Báo cáo vật tư tồn.

### Cài đặt và quy định

Ứng dụng hỗ trợ:

* Cập nhật hồ sơ người dùng.
* Đổi mật khẩu.
* Quản lý tiền công.
* Quản lý nhà cung cấp.
* Quản lý hiệu xe.
* Thay đổi một số quy định hoạt động của gara.

## Tài khoản demo

| Tài khoản | Mật khẩu | Vai trò |
| --- | --- | --- |
| `admin` | `123456` | Quản trị viên |
| `staff` | `123456` | Nhân viên |

## Cấu trúc thư mục

```text
GarageManagement/
├── DATABASE/
│   ├── table.sql
│   ├── Trigger.sql
│   └── insertData.sql
├── src/
│   ├── Assets/
│   ├── Config/
│   ├── Controllers/
│   ├── DAO/
│   ├── MODEL/
│   ├── Service/
│   ├── Views/
│   └── garagemanagement/
├── dist/
├── nbproject/
├── build.xml
└── README.md
```

## Hướng dẫn cài đặt database

### Bước 1: Tạo database và bảng

Mở SQL Server Management Studio và chạy file:

```text
DATABASE/table.sql
```

### Bước 2: Tạo trigger

Chạy file:

```text
DATABASE/Trigger.sql
```

### Bước 3: Thêm dữ liệu mẫu

Chạy file:

```text
DATABASE/insertData.sql
```

File dữ liệu mẫu tạo sẵn các danh mục, phiếu nghiệp vụ và tài khoản demo để kiểm thử ứng dụng.

## Cấu hình kết nối database

Mặc định ứng dụng kết nối SQL Server với thông tin:

```text
Server: localhost
Port: 1433
Database: Garage
User: sa
Password: 123456
```

Cấu hình nằm trong:

```text
src/Config/DBConnection.java
```

Nếu máy sử dụng tài khoản hoặc mật khẩu SQL Server khác, hãy chỉnh lại cấu hình kết nối trước khi chạy ứng dụng.

## Hướng dẫn chạy ứng dụng

### Chạy bằng NetBeans

1. Mở project bằng NetBeans.
2. Kiểm tra JavaFX SDK và JDBC driver đã được cấu hình trong project.
3. Chạy file main:

```text
garagemanagement.AppLauncher
```

### VM Options cho JavaFX

Nếu IDE yêu cầu cấu hình JavaFX, thêm VM Options:

```bash
--module-path "PATH_TO_JAVAFX_SDK/lib" --add-modules javafx.controls,javafx.fxml
```

Ví dụ:

```bash
--module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
```

## Luồng demo đề xuất

1. Đăng nhập bằng tài khoản `admin`.
2. Xem màn hình tổng quan.
3. Thêm phiếu tiếp nhận xe.
4. Lập phiếu sửa chữa cho xe đã tiếp nhận.
5. Tạo phiếu thu.
6. Kiểm tra tồn kho vật tư.
7. Xem báo cáo doanh thu và báo cáo vật tư tồn.
8. Đăng xuất và đăng nhập bằng tài khoản `staff` để kiểm tra phân quyền.

## Ghi chú

* Database sử dụng SQL Server.
* Mật khẩu demo được lưu dạng đơn giản để phục vụ đồ án.
* Khi chuyển sang máy khác, cần kiểm tra lại đường dẫn JavaFX SDK, JDBC driver và thông tin kết nối SQL Server.

## Thành viên thực hiện

* Trình Như Hinh
* Nguyễn Ngọc Huy Hoàng
* Nguyễn Đức Kiên
* Bùi Minh Khôi
* Nguyễn Ngọc Đăng Khoa

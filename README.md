# Garage Management System

Ứng dụng desktop JavaFX hỗ trợ quản lý hoạt động của gara ô tô: tiếp nhận xe, lập phiếu sửa chữa, thu tiền, quản lý vật tư, tra cứu nhanh, báo cáo thống kê, cài đặt quy định và phân quyền người dùng.

Dự án được thực hiện cho môn **SE104 - Nhập môn Công nghệ phần mềm**.

## Công nghệ sử dụng

- Java 21
- JavaFX 21.0.11
- FXML, CSS
- Microsoft SQL Server
- JDBC SQL Server
- NetBeans/Ant project

## Chức năng chính

- Đăng nhập và phân quyền theo vai trò quản trị viên/nhân viên.
- Tổng quan hoạt động gara, doanh thu, công nợ và tình trạng tồn kho.
- Quản lý tiếp nhận xe.
- Quản lý phiếu sửa chữa.
- Quản lý phiếu thu tiền.
- Quản lý vật tư, phiếu nhập vật tư và hóa đơn bán vật tư.
- Tra cứu nhanh khách hàng, xe, vật tư và phiếu sửa chữa.
- Báo cáo doanh thu theo tháng/năm và báo cáo vật tư tồn.
- Cài đặt tài khoản, tiền công, nhà cung cấp, hiệu xe và quy định gara.

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
├── lib/
│   ├── mssql-jdbc-13.4.0.jre11.jar
│   └── javafx-sdk-21.0.11/
├── nbproject/
├── src/
├── build.xml
├── manifest.mf
└── README.md
```

## Yêu cầu môi trường

- Windows.
- JDK 21.
- SQL Server và SQL Server Management Studio.
- NetBeans hoặc IntelliJ IDEA.

Thư viện JavaFX và JDBC đã được đặt sẵn trong thư mục `lib/`, nên khi clone project về không cần chỉnh đường dẫn JavaFX theo máy cá nhân.

Nếu chạy trên macOS/Linux, cần thay thư mục `lib/javafx-sdk-21.0.11` bằng JavaFX SDK đúng hệ điều hành.

## Cài đặt database

Mở SQL Server Management Studio và chạy lần lượt:

```text
DATABASE/table.sql
DATABASE/Trigger.sql
DATABASE/insertData.sql
```

Database mặc định:

```text
Database: Garage
User: sa
Password: 123456
```

Cấu hình kết nối nằm tại:

```text
src/Config/DBConnection.java
```

Nếu máy dùng tài khoản SQL Server khác, chỉnh lại `user` và `password` trong file này trước khi chạy app.

## Chạy bằng NetBeans

1. Mở NetBeans.
2. Chọn `File > Open Project`.
3. Chọn thư mục `GarageManagement`.
4. Kiểm tra project đang dùng JDK 21.
5. Bấm `Run Project`.

Main class:

```text
garagemanagement.AppLauncher
```

## Chạy bằng IntelliJ IDEA

1. Mở IntelliJ IDEA.
2. Chọn `Open` và trỏ tới thư mục `GarageManagement`.
3. Chọn Project SDK là JDK 21.
4. Tạo Run Configuration dạng Application:

```text
Main class: garagemanagement.AppLauncher
Working directory: <đường dẫn tới GarageManagement>
```

VM options:

```text
-Djava.library.path="$PROJECT_DIR$/lib/javafx-sdk-21.0.11/bin" --module-path "$PROJECT_DIR$/lib/javafx-sdk-21.0.11/lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=ALL-UNNAMED
```

Classpath/module dependencies cần có:

```text
lib/mssql-jdbc-13.4.0.jre11.jar
lib/javafx-sdk-21.0.11/lib/javafx.base.jar
lib/javafx-sdk-21.0.11/lib/javafx.controls.jar
lib/javafx-sdk-21.0.11/lib/javafx.fxml.jar
lib/javafx-sdk-21.0.11/lib/javafx.graphics.jar
```

## Chạy bằng CMD/PowerShell

Sau khi build project bằng NetBeans:

```powershell
cd C:\path\to\GarageManagement
java -Djava.library.path=".\lib\javafx-sdk-21.0.11\bin" --module-path ".\lib\javafx-sdk-21.0.11\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=ALL-UNNAMED -cp ".\dist\GarageManagement.jar;.\lib\mssql-jdbc-13.4.0.jre11.jar" garagemanagement.AppLauncher
```

## Ghi chú

- Nên dùng JDK 21 để tương thích với JavaFX 21.
- Không commit thư mục `build/`, `dist/`, `.idea/`, `out/` hoặc file `.iml`.
- Dữ liệu mẫu chỉ phục vụ kiểm thử và demo đồ án.

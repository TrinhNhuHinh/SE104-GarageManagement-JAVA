package MODEL;

public class SessionManager {
    private static NguoiDung currentUser;

    public static void setCurrentUser(NguoiDung user) {
        currentUser = user;
    }

    public static NguoiDung getCurrentUser() {
        return currentUser;
    }
}
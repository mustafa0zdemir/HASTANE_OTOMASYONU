package kullaniciBilgileriHasta;

public class SessionManager {
    public  static KullaniciInfo currentUser;

    // Kullanıcıyı oturuma ekleme
    public static void setCurrentUser(KullaniciInfo user) {
        currentUser = user;
    }

    // Oturumdaki kullanıcıyı alma
    public static KullaniciInfo getCurrentUser() {
        return currentUser;
    }

    // Oturumu temizleme
    public static void clearSession() {
        currentUser = null;
    }
}
package kullaniciBilgileriDoktor;

public class SessionManagerD {
    private static String currentDoctorTC;

    public static void setCurrentDoctorTC(String tc) {
        currentDoctorTC = tc;
    }

    public static String getCurrentDoctorTC() {
        return currentDoctorTC;
    }
}

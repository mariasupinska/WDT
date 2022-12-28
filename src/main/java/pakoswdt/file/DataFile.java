package pakoswdt.file;

import pakoswdt.MainApp;

import java.util.prefs.Preferences;

public class DataFile {
    public static final String PREFS_KEY = "dataFilePath";

    public static String getDataFile() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get(PREFS_KEY, "");
        return filePath;
    }

    public static void setDataFilePath(String filePath) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        prefs.put(PREFS_KEY, filePath);
    }
}

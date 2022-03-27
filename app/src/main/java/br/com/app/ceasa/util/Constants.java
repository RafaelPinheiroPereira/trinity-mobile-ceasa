package br.com.app.ceasa.util;

import android.Manifest;
import android.os.Environment;

public class Constants {

    public static final String EXTRA_DATE_PAYMENT = "dateSale";
    public static final String SPINNER_KEY_POSITION = "spinnerKeyPostion";
    public static final int TARGET_HOME_FRAGMENT_REQUEST_CODE = 1;
    public static final int TARGET_HISTORIC_FRAGMENT_REQUEST_CODE = 2;
    public static String PREF_DEVICE_ADDRESS = "device_address";

    public static final String PREFS_NAME = "Preferences";

    public static int REQUEST_STORAGE = 112;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,   Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED,
    };

    public static String APP_FOLDER_NAME = "/CEASA";

    public static final String APP_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(APP_FOLDER_NAME).getPath();

    public static String[] INPUT_FILES =
            new String[]{
                    "FILEINPUT.txt"
            };

    public static String OUTPUT_FILE = "/FILEOUT.txt";

    public static String LOG_FILE = "/Log.txt";
}

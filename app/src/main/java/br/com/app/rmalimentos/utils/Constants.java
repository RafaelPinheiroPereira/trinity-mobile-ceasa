package br.com.app.rmalimentos.utils;

import android.Manifest;
import android.os.Environment;

public class Constants {

    public static final String EXTRA_DATE_SALE = "dateSale";
    public static final String SPINNER_KEY_POSITION = "spinnerKeyPostion";
    public static final int TARGET_FRAGMENT_REQUEST_CODE = 1;

    public static final String PREFS_NAME = "Preferences";

    public static int REQUEST_STORAGE = 112;

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String APP_FOLDER_NAME = "/TRINITY_MOBILE";

    public static final String APP_DIRECTORY =
            Environment.getExternalStoragePublicDirectory(APP_FOLDER_NAME).getPath();

    public static String[] INPUT_FILES =
            new String[]{
                    "PdaVendedor.txt",
                    "PdaProdutos.txt",
                    "PdaClientes.txt",
                    "PdaPrecos.txt",
                    "PdaUnidades.txt",
                    "PdaLocalidades.txt",
                    "PdaContas.txt",
                    "PdaForPag.txt"
            };

    public static String OUTPUT_FILE = "/Pedidos.txt";

    public static String LOG_FILE = "/Log.txt";
}

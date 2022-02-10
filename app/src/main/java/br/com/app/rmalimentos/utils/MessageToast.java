package br.com.app.rmalimentos.utils;

import android.content.Context;
import android.widget.Toast;

public abstract class MessageToast {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}

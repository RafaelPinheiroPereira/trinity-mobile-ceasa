package br.com.app.ceasa.listener;

import android.view.View;

public  interface RecyclerViewOnClickListenerHack {
    void onClickListener(View view, int position);
    void onLongPressClickListener(View view, int position);
}

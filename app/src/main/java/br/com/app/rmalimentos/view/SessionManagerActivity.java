package br.com.app.rmalimentos.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import br.com.app.rmalimentos.utils.Singleton;
import br.com.app.rmalimentos.viewmodel.SessionManagerViewModel;

public class SessionManagerActivity extends AppCompatActivity {

    SessionManagerViewModel sessionManagerViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManagerViewModel = new ViewModelProvider(this).get(SessionManagerViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (this.sessionManagerViewModel.checkedLogin()) {
            startActivity(
                    new Intent(SessionManagerActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(SessionManagerActivity.this, LoginActivity.class));
        }


    }
}

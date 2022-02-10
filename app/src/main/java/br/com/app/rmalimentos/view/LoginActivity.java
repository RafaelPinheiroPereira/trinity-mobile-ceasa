package br.com.app.rmalimentos.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.Singleton;
import br.com.app.rmalimentos.viewmodel.LoginViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog.Builder;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.edt_user)
    EditText edtUser;

    @BindView(R.id.img_logo)
    ImageView imgLogo;

    @BindView(R.id.lnl_login_box)
    LinearLayout lnLoginBox;

    LoginViewModel loginViewModel;

    AbstractActivity abstractActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        this.loadAnimation();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        try {
            abstractActivity = Singleton.getInstance(AbstractActivity.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        this.checkPermissions();
    }

    @OnClick(R.id.btn_login)
    public void setBtnLoginClicked(View view) {

        if (loginViewModel.employeeFileExists()) {

            try {
                loginViewModel.readEmployeeFile();

                if (edtUser.getText().toString().equals(String.valueOf(loginViewModel.getEmployee().getId()))) {
//                        &&
//                        edtPassword.getText().toString().equals(loginViewModel.getEmployee().getPassword())) {

                    this.loginViewModel.getEmployee().setAtived(1);

                    if (this.loginViewModel.isExistEmployee()) {
                        loginViewModel.updateEmployee();
                    } else {
                        loginViewModel.saveEmployee();
                    }

                    AbstractActivity.navigateToActivity(this, new Intent(this, HomeActivity.class));

                } else {
                    // Todo exibir alerta de senha/user errado
                    BottomSheetMaterialDialog mBottomSheetDialog =
                            new BottomSheetMaterialDialog.Builder(this)
                                    .setTitle("Dados Inválidos?")
                                    .setMessage("Usuário/Senha foram digitados corretamente?")
                                    .setAnimation("error.json")
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "OK",
                                            (dialogInterface, which)->{
                                                dialogInterface.dismiss();
                                            })
                                    .build();

                    // Show Dialog
                    mBottomSheetDialog.show();
                }

            } catch (IOException | InstantiationException | IllegalAccessException e) {

                abstractActivity.showErrorMessage(LoginActivity.this, e.getMessage());
            }

        } else {

            BottomSheetMaterialDialog mBottomSheetDialog =
                    new Builder(this)
                            .setTitle("Atenção")
                            .setMessage(getString(R.string.employee_file_not_found_message_alert))
                            .setCancelable(false)
                            .setPositiveButton(
                                    "OK",
                                    (dialogInterface, which)->{
                                        abstractActivity
                                                .showMessage(this, "Por favor, realize a inclusão do arquivo");

                                        dialogInterface.dismiss();
                                    })
                            .build();

            // Show Dialog
            mBottomSheetDialog.show();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(
                            LoginActivity.this,
                            "As permissões são necessárias para as operações de importação e exportação.",
                            Toast.LENGTH_LONG)
                            .show();
                    ActivityCompat.requestPermissions(
                            LoginActivity.this, Constants.PERMISSIONS_STORAGE, Constants.REQUEST_STORAGE);

                } else {
                    // Solicita a permissao
                    ActivityCompat.requestPermissions(
                            LoginActivity.this, Constants.PERMISSIONS_STORAGE, Constants.REQUEST_STORAGE);
                }
            }
        }
    }

    private void loadAnimation() {
        Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.translate);
        animTranslate.setAnimationListener(
                new Animation.AnimationListener() {

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        lnLoginBox.setVisibility(View.VISIBLE);

                        Animation animFade = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade);
                        lnLoginBox.startAnimation(animFade);
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }
                });
        imgLogo.startAnimation(animTranslate);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permissões concedidas.", Toast.LENGTH_LONG).show();
                try {
                    loginViewModel.setContext(this);
                    loginViewModel.createAppDirectory();
                } catch (IOException e) {

                    abstractActivity.showErrorMessage(this, e.getMessage());
                }

            } else {
                Toast.makeText(
                        LoginActivity.this,
                        "As permissões não foram concedidas.\nO Login não poderá ser realizado.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

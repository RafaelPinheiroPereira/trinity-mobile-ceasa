package br.com.app.ceasa.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import br.com.app.ceasa.R;
import br.com.app.ceasa.util.Constants;
import br.com.app.ceasa.util.Singleton;
import br.com.app.ceasa.ui.fragment.EmptyFragment;
import br.com.app.ceasa.ui.fragment.HomeFragment;
import br.com.app.ceasa.viewmodel.HomeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog.Builder;

import java.io.IOException;

public class HomeActivity extends AbstractActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.bottom_nav)
  BottomNavigationView bottomNavigationView;

  HomeViewModel homeViewModel;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    initViews();
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    homeViewModel.setContext(this);

  }

  @Override
  protected void onStart() {
    super.onStart();

    this.checkPermissions();

    homeViewModel
        .getClientsAll()
        .observe(
            this,
            clients -> {
              if (clients.size() > 0) {
                HomeFragment homeFragment = new HomeFragment();
                this.loadFragment(homeFragment);

              } else {

                EmptyFragment emptyFragment = new EmptyFragment();
                this.loadFragment(emptyFragment);
              }
            });

    bottomNavigationView.setOnNavigationItemSelectedListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.page_1:
              if (homeViewModel.containsAllFiles()) {

                try {
                  homeViewModel.importData();
                } catch (IllegalAccessException e) {
                  showErrorMessage(getApplicationContext(), e.getMessage());
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                  showErrorMessage(getApplicationContext(), e.getMessage());
                } catch (InstantiationException e) {
                  e.printStackTrace();
                  showErrorMessage(getApplicationContext(), e.getMessage());
                }

              } else {

                StringBuilder message = homeViewModel.searchInexistsFilesNames();

                BottomSheetMaterialDialog mBottomSheetDialog =
                    new Builder(HomeActivity.this)
                        .setTitle("Atenção, não foram localizados os arquivos abaixo:")
                        .setMessage(message.toString())
                        .setCancelable(false)
                        .setPositiveButton(
                            "OK",
                            (dialogInterface, which) -> {
                              showMessage(
                                  getApplicationContext(),
                                  "Por favor, realize a inclusão dos arquivos");

                              dialogInterface.dismiss();
                            })
                        .build();

                mBottomSheetDialog.show();
              }
              break;
            case R.id.page_2:
              startActivity(new Intent(HomeActivity.this, ConfigurationDataActivity.class));
              break;
            case R.id.page_3:
              startActivity(new Intent(HomeActivity.this, HistoricActivity.class));
              break;

            case R.id.page_4:
              startActivity(new Intent(HomeActivity.this, ExportActivity.class));

              break;
          }
          return true;
        });
  }


  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Home");
    setSupportActionBar(toolbar);
  }

  private void checkPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED
          || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
              != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
            HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          Toast.makeText(
                  HomeActivity.this,
                  "As permissões são necessárias para as operações de importação e exportação.",
                  Toast.LENGTH_LONG)
              .show();
          ActivityCompat.requestPermissions(
              HomeActivity.this, Constants.PERMISSIONS_STORAGE, Constants.REQUEST_STORAGE);

        } else {
          // Solicita a permissao
          ActivityCompat.requestPermissions(
              HomeActivity.this, Constants.PERMISSIONS_STORAGE, Constants.REQUEST_STORAGE);
        }
      }
    }
  }

  @Override
  public void onBackPressed() {
    this.finish();
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {

    if (requestCode == Constants.REQUEST_STORAGE) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        Toast.makeText(this, "Permissões concedidas.", Toast.LENGTH_LONG).show();
        try {
          homeViewModel.setContext(this);
          homeViewModel.createAppDirectory();
        } catch (IOException e) {

          showErrorMessage(this, e.getMessage());
        }

      } else {
        Toast.makeText(
                HomeActivity.this,
                "As permissões não foram concedidas.\nO App não funcionará corretamente.",
                Toast.LENGTH_LONG)
            .show();
      }
    } else {

      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }


}

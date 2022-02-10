package br.com.app.rmalimentos.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.utils.Singleton;
import br.com.app.rmalimentos.view.fragment.EmptyFragment;
import br.com.app.rmalimentos.view.fragment.HomeFragment;
import br.com.app.rmalimentos.viewmodel.HomeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog.Builder;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;



    HomeViewModel homeViewModel;

    AbstractActivity abstractActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initViews();
        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.setContext(this);
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

        try {
            homeViewModel
                    .getAllRoutes()
                    .observe(
                            this,
                            routes->{
                                if (routes.size() > 0) {
                                    HomeFragment homeFragment = new HomeFragment();
                                    this.loadFragment(homeFragment);

                                } else {

                                    EmptyFragment emptyFragment = new EmptyFragment();
                                    this.loadFragment(emptyFragment);
                                    // exibir spty state
                                }
                            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bottomNavigationView.setSelectedItemId(R.id.page_1);

        bottomNavigationView.setOnNavigationItemSelectedListener(item->{

            switch (  item.getItemId()) {
                case R.id.page_1:
                    // Todo recarregar tela inicial sei lá

                    break;
                case R.id.page_3:
                    if (homeViewModel.containsAllFiles()) {

                        //

                        try {
                            homeViewModel.importData();
                        } catch (IllegalAccessException e) {
                            abstractActivity.showErrorMessage(getApplicationContext(),e.getMessage());
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            abstractActivity.showErrorMessage(getApplicationContext(),e.getMessage());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                            abstractActivity.showErrorMessage(getApplicationContext(),e.getMessage());
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
                                                (dialogInterface, which)->{
                                                    abstractActivity.showMessage(getApplicationContext(),
                                                            "Por favor, realize a inclusão dos arquivos");

                                                    dialogInterface.dismiss();
                                                })
                                        .build();

                        mBottomSheetDialog.show();
                    }
                    break;

                case R.id.page_4:

                    startActivity(new Intent(HomeActivity.this, ExportActivity.class));

                    break;

                case R.id.page_5:

                    BottomSheetMaterialDialog mBottomSheetDialog =
                            new Builder(HomeActivity.this)
                                    .setTitle("Atenção")
                                    .setMessage("Você deseja realmente sair da sessão?")
                                    .setNegativeButton("NÃO", (dialogInterface, which)->{

                                        dialogInterface.dismiss();
                                    })
                                    .setPositiveButton(
                                            "OK",
                                            (dialogInterface, which)->{
                                                this.homeViewModel.logout();
                                                dialogInterface.dismiss();
                                                this.finish();
                                            })
                                    .build();

                    mBottomSheetDialog.show();
                    break;
            }
            return true;
        });

    }

    @Override
    public void onBackPressed() {
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViews() {
        toolbar.setTitle("Trinity Mobile - R&M Alimentos");
        setSupportActionBar(toolbar);
    }
}

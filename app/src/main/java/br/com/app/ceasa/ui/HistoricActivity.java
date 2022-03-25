package br.com.app.ceasa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import java.text.ParseException;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.ui.fragment.EmptyFragment;
import br.com.app.ceasa.ui.fragment.HistoricFragment;
import br.com.app.ceasa.ui.fragment.HomeFragment;
import br.com.app.ceasa.viewmodel.HistoricViewModel;
import br.com.app.ceasa.viewmodel.PaymentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  HistoricViewModel historicViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_historic);
    ButterKnife.bind(this);
    initViews();
    historicViewModel = new ViewModelProvider(this).get(HistoricViewModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    try {
      historicViewModel
          .getHistoricByDatePayment(this.historicViewModel.getConfigurationDataSalved().getBaseDate())
          .observe(
              this,
              historic -> {
                if (historic.size() > 0) {
                  HistoricFragment historicFragment = new HistoricFragment();
                  this.loadFragment(historicFragment);

                } else {

                  EmptyFragment emptyFragment = new EmptyFragment();
                  this.loadFragment(emptyFragment);
                }
              });
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Histórico");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
  private void loadFragment(Fragment fragment) {
    // load fragment
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
}

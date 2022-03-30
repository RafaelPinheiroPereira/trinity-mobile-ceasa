package br.com.app.ceasa.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import br.com.app.ceasa.R;
import br.com.app.ceasa.ui.fragment.HistoricFragment;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.PrinterDatecsUtil;
import br.com.app.ceasa.viewmodel.HistoricViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoricActivity extends AbstractActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  HistoricViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_historic);
    ButterKnife.bind(this);
    initViews();
    viewModel = new ViewModelProvider(this).get(HistoricViewModel.class);
    this.viewModel.setPrinterDatecsUtil(new PrinterDatecsUtil(this));
  }

  @Override
  protected void onStart() {
    super.onStart();

    try {
      Date dateToday =
              DateFormat.getDateInstance(DateFormat.SHORT)
                      .parse(
                              DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
                                      new Date(System.currentTimeMillis())));
      viewModel
          .getHistoricByDatePayment(dateToday)
          .observe(
              this,
              historic -> {

                  HistoricFragment historicFragment = new HistoricFragment();
                  this.loadFragment(historicFragment);

              });
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (this.viewModel.isAtivedPrinter()) {
      try {
        this.viewModel.waitForConnection();
      } catch (Throwable throwable) {
        showErrorMessage(this, "Erro ao encontrar impressora ativa!");
      }
    } else {
      showMessage(this, "Não há impressora ativa, por favor configure!");
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

  @OnClick(R.id.fb_print_historic)
  public void printHistoric(View view) {
    if (!this.viewModel.getHistorics().isEmpty()) this.viewModel.printHistoric();
    else showMessage(this,"Não existem recebimentos a serem impressos!");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.viewModel.closeConnection();
  }

  @Override
  public void onBackPressed() {}
}

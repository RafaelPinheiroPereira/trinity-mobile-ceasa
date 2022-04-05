package br.com.app.ceasa.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import br.com.app.ceasa.R;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.viewmodel.ClearDataBaseViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClearDatabaseActivity extends AbstractActivity {

  @BindView(R.id.cv_initial_date)
  CalendarView cvInitialDate;

  @BindView(R.id.cv_final_date)
  CalendarView cvFinalDate;

  @BindView(R.id.btn_clear)
  Button btnClear;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  ClearDataBaseViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_clear_database);
    ButterKnife.bind(this);
    viewModel = new ViewModelProvider(this).get(ClearDataBaseViewModel.class);
    initViews();
  }

  @Override
  protected void onStart() {
    super.onStart();

    DateFormat format = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    try {
      viewModel.setInitialDate(format.parse(format.format(cvInitialDate.getDate())));
      viewModel.setFinalDate(format.parse(format.format(cvFinalDate.getDate())));
    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }

    cvInitialDate.setOnDateChangeListener(
        (view, year, month, dayOfMonth) -> {
          // display the selected date by using a toast

          Calendar c = Calendar.getInstance();
          c.set(Calendar.YEAR, year);
          c.set(Calendar.MONTH, month);
          c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
          String formatedInitialDate = f.format(c.getTime());
          try {

            Date initialDate = f.parse(formatedInitialDate);
            viewModel.setInitialDate(initialDate);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });
    cvFinalDate.setOnDateChangeListener(
        (view, year, month, dayOfMonth) -> {
          // display the selected date by using a toast
          Calendar c = Calendar.getInstance();
          c.set(Calendar.YEAR, year);
          c.set(Calendar.MONTH, month);
          c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
          String formatedFinalDate = f.format(c.getTime());
          try {
            Date finalDate = f.parse(formatedFinalDate);

            viewModel.setFinalDate(finalDate);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });
  }

  private void initViews() {
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.btn_clear)
  public void clearDatabase() {
    if (DateUtils.isValidPeriod(this.viewModel.getInitialDate(), this.viewModel.getFinalDate())) {
        this.viewModel.clearDatabase();
        showMessage(this,"Dados deletados com sucesso!");
    } else {
       showMessage(this,"Data Inicial dever ser menor ou igual a Data Final!");
    }
  }
}

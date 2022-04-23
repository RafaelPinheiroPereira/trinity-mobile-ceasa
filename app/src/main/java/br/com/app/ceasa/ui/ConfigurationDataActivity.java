package br.com.app.ceasa.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import br.com.app.ceasa.R;
import br.com.app.ceasa.tasks.InsertConfigurationDataTask;
import br.com.app.ceasa.tasks.UpdateConfigurationDataTask;
import br.com.app.ceasa.util.CurrencyEditText;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.MonetaryFormatting;
import br.com.app.ceasa.viewmodel.ConfigurationDataViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationDataActivity extends AbstractActivity {
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.cet_base_value)
  CurrencyEditText cetPrice;

  @BindView(R.id.btn_save_configuration_data)
  Button btnSaveConfigurationData;



  @BindView(R.id.cv_base_date)
  CalendarView cvBaseDate;

  ConfigurationDataViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_configuration_data);
    ButterKnife.bind(this);
    initViews();
    viewModel = new ViewModelProvider(this).get(ConfigurationDataViewModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    this.viewModel.setContext(this);

    if (this.viewModel.existConfigurationData()) {
      this.viewModel.setConfigurationData(this.viewModel.getConfigurationDataSalved());
      cetPrice.setText(
          MonetaryFormatting.convertToDolar(
              this.viewModel.getConfigurationData().getBaseValue()));
      cvBaseDate.setDate(this.viewModel.getConfigurationData().getBaseDate().getTime());
      this.viewModel.setInitialDateBase(this.viewModel.getConfigurationData().getBaseDate());
    } else {
      cetPrice.setText("0.00");
      Date dateToday =
              null;
      try {
        dateToday = DateFormat.getDateInstance(DateFormat.SHORT)
                .parse(
                        DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
                                new Date(System.currentTimeMillis())));
      } catch (ParseException e) {
        showMessage(this,e.getMessage());
      }
      this.viewModel.setInitialDateBase(dateToday);
    }

    cvBaseDate.setOnDateChangeListener(
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
            this.viewModel.setInitialDateBase(initialDate);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Configurações");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private boolean isConfigurationDataValid() {
    if (TextUtils.isEmpty(cetPrice.getText().toString())) {
      cetPrice.setError("Valor Base Obrigatório!");
      cetPrice.requestFocus();
      return false;
    }

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.btn_save_configuration_data)
  public void saveConfigurationData() {

    if (isConfigurationDataValid()) {
      this.viewModel.setValueBase(cetPrice.getCurrencyDouble());

      if (!this.viewModel.existConfigurationData()) {
        this.viewModel.setConfigurationData(
            this.viewModel.getConfigurationDataToInsert());
        new InsertConfigurationDataTask(this.viewModel).execute();
      } else {
        this.viewModel.setConfigurationData(
            this.viewModel.getConfigurationDataSalved());
        this.viewModel
            .getConfigurationData()
            .setBaseValue(this.viewModel.getValueBase());

        if (DateUtils.isTodayAfterDateBase(
            this.viewModel.getInitialDateBase(),
            this.viewModel.getConfigurationDataSalved().getBaseDate())) {
          this.viewModel
              .getConfigurationData()
              .setBaseDate(this.viewModel.getInitialDateBase());
          new UpdateConfigurationDataTask(this.viewModel).execute();
        }else{
          showMessage(this,"A nova data base não pode ser menor do que a cadastrada!");

        }
      }
    }
  }


}

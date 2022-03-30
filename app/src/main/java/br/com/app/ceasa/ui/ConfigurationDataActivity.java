package br.com.app.ceasa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import br.com.app.ceasa.util.MonetaryFormatting;
import br.com.app.ceasa.viewmodel.ConfigurationDataViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigurationDataActivity extends AppCompatActivity {
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.cet_base_value)
  CurrencyEditText cetPrice;

  @BindView(R.id.btn_save_configuration_data)
  Button btnSaveConfigurationData;

  @BindView(R.id.btn_save_printer)
  Button btnSavePrinter;

  @BindView(R.id.cv_base_date)
  CalendarView cvBaseDate;

  ConfigurationDataViewModel configurationDataViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_configuration_data);
    ButterKnife.bind(this);
    initViews();
    configurationDataViewModel = new ViewModelProvider(this).get(ConfigurationDataViewModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    this.configurationDataViewModel.setContext(this);

    if (this.configurationDataViewModel.existConfigurationData()) {
      // Todo setar os campos de data e valor com os dados do banco
      cetPrice.setText(
          MonetaryFormatting.convertToDolar(
              this.configurationDataViewModel.getConfigurationDataSalved().getBaseValue()));
    } else {
      cetPrice.setText("0.00");
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
            this.configurationDataViewModel.setInitialDateBase(initialDate);
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
      this.configurationDataViewModel.setValueBase(cetPrice.getCurrencyDouble());
      if (!this.configurationDataViewModel.existConfigurationData()) {
        this.configurationDataViewModel.setConfigurationData(
            this.configurationDataViewModel.getConfigurationDataToInsert());
        new InsertConfigurationDataTask(this.configurationDataViewModel).execute();
      } else {
        this.configurationDataViewModel.setConfigurationData(
            this.configurationDataViewModel.getConfigurationDataSalved());
        this.configurationDataViewModel
            .getConfigurationData()
            .setBaseValue(this.configurationDataViewModel.getValueBase());
        this.configurationDataViewModel
            .getConfigurationData()
            .setBaseDate(this.configurationDataViewModel.getInitialDateBase());
        new UpdateConfigurationDataTask(this.configurationDataViewModel).execute();
      }
    }
  }

  @OnClick(R.id.btn_save_printer)
  public void savePrinter() {
         startActivity(new Intent(this,PrinterActivity.class));
  }
}

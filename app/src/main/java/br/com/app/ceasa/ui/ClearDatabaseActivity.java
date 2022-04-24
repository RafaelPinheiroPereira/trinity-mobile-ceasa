package br.com.app.ceasa.ui;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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

public class ClearDatabaseActivity extends AbstractActivity  {

  @BindView(R.id.edt_date_initial)
  EditText edtInitialDate;

  @BindView(R.id.edt_date_final)
  EditText edtFinalDate;

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
      viewModel.setInitialDate(format.parse(format.format(new Date(System.currentTimeMillis()))));
      viewModel.setFinalDate(format.parse(format.format(new Date(System.currentTimeMillis()))));
      edtInitialDate.setText(
                DateUtils.convertDateToStringInFormat_dd_mm_yyyy(new Date(viewModel.getInitialDate().getTime())));
      edtFinalDate.setText(
                DateUtils.convertDateToStringInFormat_dd_mm_yyyy(new Date(viewModel.getFinalDate().getTime())));
    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }

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

  @OnClick(R.id.edt_date_initial)
  public  void setInitialDate(){
      final Calendar c = Calendar.getInstance();
      int  mYear = c.get(Calendar.YEAR);
      int  mMonth = c.get(Calendar.MONTH);
      int  mDay = c.get(Calendar.DAY_OF_MONTH);
      DatePickerDialog datePickerDialog = new DatePickerDialog(this,
              (view, year, monthOfYear, dayOfMonth) -> {

                  String strMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                  String strDay = (dayOfMonth) < 10 ? "0" + (dayOfMonth) : String.valueOf(dayOfMonth);
                  edtInitialDate.setText(strDay + "/" + strMonth  + "/" + year);
                  try {
                      viewModel.setInitialDate(DateFormat.getDateInstance(DateFormat.DATE_FIELD).parse(edtInitialDate.getText().toString()));
                  } catch (ParseException e) {
                      showErrorMessage(ClearDatabaseActivity.this,e.getMessage());
                  }

              }, mYear, mMonth, mDay);
      datePickerDialog.show();
  }
    @OnClick(R.id.edt_date_final)
    public  void setFinalDate(){

        final Calendar c = Calendar.getInstance();
        int  mYear = c.get(Calendar.YEAR);
        int  mMonth = c.get(Calendar.MONTH);
        int  mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String strMonth = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                    String strDay = (dayOfMonth) < 10 ? "0" + (dayOfMonth) : String.valueOf(dayOfMonth);
                    edtFinalDate.setText(strDay + "/" + strMonth  + "/" + year);

                    try {
                        viewModel.setInitialDate(DateFormat.getDateInstance(DateFormat.DATE_FIELD).parse(edtFinalDate.getText().toString()));
                    } catch (ParseException e) {
                        showErrorMessage(ClearDatabaseActivity.this,e.getMessage());
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

}

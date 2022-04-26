package br.com.app.ceasa.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.viewmodel.ExportViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog.Builder;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExportActivity extends AbstractActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.edt_date_initial)
  EditText edtInitialDate;

  @BindView(R.id.edt_date_final)
  EditText edtFinalDate;

  @BindView(R.id.btn_export)
  Button btnExport;

  ExportViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_export);
    ButterKnife.bind(this);
    initViews();
    viewModel = new ViewModelProvider(this).get(ExportViewModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();

    DateFormat format = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    try {
      viewModel.setInitialDate(format.parse(format.format(new Date(System.currentTimeMillis()))));
      viewModel.setFinalDate(format.parse(format.format(new Date(System.currentTimeMillis()))));
      edtInitialDate.setText(
          DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
              new Date(viewModel.getInitialDate().getTime())));
      edtFinalDate.setText(
          DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
              new Date(viewModel.getFinalDate().getTime())));
    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }

    this.viewModel.setContext(this);
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

  private void initViews() {
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.btn_export)
  public void exportData() {
    if (DateUtils.isValidPeriod(this.viewModel.getInitialDate(), this.viewModel.getFinalDate())) {
      LiveData<List<Payment>> paymentsListLiveData = this.viewModel.searchDataToExportByDate();
      paymentsListLiveData.observe(
          this,
          payments -> {
            if (payments.size() > 0) {
              viewModel.setPayments(payments);
              viewModel.exportData();
            } else {
              BottomSheetMaterialDialog mBottomSheetDialog =
                  new Builder(this)
                      .setTitle("Atenção!")
                      .setMessage("Não existem dados para serem exportados no período informado!")
                      .setCancelable(false)
                      .setPositiveButton(
                          "OK",
                          (dialogInterface, which) -> {
                            Toast.makeText(
                                    getApplication().getApplicationContext(),
                                    "Por favor, verifique o período de recebimentos!",
                                    Toast.LENGTH_LONG)
                                .show();

                            dialogInterface.dismiss();
                          })
                      .build();

              mBottomSheetDialog.show();
            }
          });

    } else {
      showMessage(this, "Data Inicial dever ser menor ou igual a Data Final!");
    }
  }

  @Override
  public void onBackPressed() {}

  @OnClick(R.id.edt_date_initial)
  public void setInitialDate() {
    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog =
        new DatePickerDialog(
            this,
            (view, year, monthOfYear, dayOfMonth) -> {
              String strMonth =
                  (monthOfYear + 1) < 10
                      ? "0" + (monthOfYear + 1)
                      : String.valueOf(monthOfYear + 1);
              String strDay = (dayOfMonth) < 10 ? "0" + (dayOfMonth) : String.valueOf(dayOfMonth);
              edtInitialDate.setText(strDay + "/" + strMonth + "/" + year);
              try {
                viewModel.setInitialDate(
                    DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                        .parse(edtInitialDate.getText().toString()));
              } catch (ParseException e) {
                showErrorMessage(ExportActivity.this, e.getMessage());
              }
            },
            mYear,
            mMonth,
            mDay);
    datePickerDialog.show();
  }

  @OnClick(R.id.edt_date_final)
  public void setFinalDate() {

    final Calendar c = Calendar.getInstance();
    int mYear = c.get(Calendar.YEAR);
    int mMonth = c.get(Calendar.MONTH);
    int mDay = c.get(Calendar.DAY_OF_MONTH);
    DatePickerDialog datePickerDialog =
        new DatePickerDialog(
            this,
            (view, year, monthOfYear, dayOfMonth) -> {
              String strMonth =
                  (monthOfYear + 1) < 10
                      ? "0" + (monthOfYear + 1)
                      : String.valueOf(monthOfYear + 1);
              String strDay = (dayOfMonth) < 10 ? "0" + (dayOfMonth) : String.valueOf(dayOfMonth);
              edtFinalDate.setText(strDay + "/" + strMonth + "/" + year);

              try {
                viewModel.setInitialDate(
                    DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                        .parse(edtFinalDate.getText().toString()));
              } catch (ParseException e) {
                showErrorMessage(ExportActivity.this, e.getMessage());
              }
            },
            mYear,
            mMonth,
            mDay);
    datePickerDialog.show();
  }
}

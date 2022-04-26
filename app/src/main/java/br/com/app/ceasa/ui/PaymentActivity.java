package br.com.app.ceasa.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.tasks.InsertPaymentTask;
import br.com.app.ceasa.util.CurrencyEditText;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.MonetaryFormatting;
import br.com.app.ceasa.util.PrinterDatecsUtil;
import br.com.app.ceasa.viewmodel.PaymentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class PaymentActivity extends AbstractActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.txt_cliente_dados)
  TextView dataClient;

  @BindView(R.id.cet_price)
  CurrencyEditText cetPrice;

  @BindView(R.id.edt_description)
  EditText edtDescription;

  @BindView(R.id.edt_date_base)
  EditText edtBaseDate;

  @BindView(R.id.fb_save_sale)
  FloatingActionButton floatingActionButton;

  PaymentViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);
    initViews();
    viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
    this.viewModel.setPrinterDatecsUtil(new PrinterDatecsUtil(this));
  }

  @Override
  protected void onStart() {
    super.onStart();
    try {
      this.viewModel.setContext(this);
      this.getParentActivityData();

      if (this.viewModel.existConfigurationData()) {
        cetPrice.setText(
            MonetaryFormatting.convertToDolar(
                this.viewModel.getConfigurationDataSalved().getBaseValue()));

        edtBaseDate.setText(
            DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
                new Date(this.viewModel.getConfigurationDataSalved().getBaseDate().getTime())));
      }

    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }

    this.setClientData();
    this.checkInitialConfigure();

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

  /*Configura os componentes para a criacao da venda*/
  private void configureCreate() {
    DateFormat format = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    try {
      this.viewModel.setPaymentDate(
          format.parse(format.format(new Date(System.currentTimeMillis()))));
    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }
  }

  /*Configura os componentes para a atualizacao da venda*/

  private void checkInitialConfigure() {
    Optional<Payment> optionalPayment =
        Optional.ofNullable(this.viewModel.getPaymentByDateAndClient());

    if (optionalPayment.isPresent()) {
      this.viewModel.setPayment(optionalPayment.get());
      disableForm();
      setDataPaymet();
      floatingActionButton.setImageDrawable(getDrawable(R.mipmap.ic_print));

    } else {
      this.configureCreate();
    }
  }

  private void setDataPaymet() {
    edtDescription.setText(this.viewModel.getPayment().getDescription());
    cetPrice.setText(MonetaryFormatting.convertToDolar(this.viewModel.getPayment().getValue()));
    edtBaseDate.setText(
        DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
            new Date(this.viewModel.getPayment().getDate().getTime())));
  }

  private void disableForm() {
    edtBaseDate.setEnabled(false);
    edtDescription.setEnabled(false);
    cetPrice.setEnabled(false);
  }

  /*Preeche os text view com os dados do cliente*/
  private void setClientData() {
    this.dataClient.setText(
        "CLIENTE: "
            + this.viewModel.getClient().getId()
            + " - "
            + this.viewModel.getClient().getName());
  }

  /*Obtem os dados da HomeActivity*/
  private void getParentActivityData() throws ParseException {

    Bundle args = getIntent().getExtras();

    if (args != null) {
      this.viewModel.setClient((Client) args.getSerializable("keyClient"));
      this.viewModel.setPaymentDate(
          DateFormat.getDateInstance(DateFormat.SHORT).parse(args.getString("keyDateSale")));
    }
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Recebimentos");

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    cetPrice.setText("0.00");
    cetPrice.requestFocus();
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

  @OnClick(R.id.fb_save_sale)
  public void savePayment(View view) {

    Optional<Payment> optionalPayment = Optional.ofNullable(this.viewModel.getPayment());
    if (!optionalPayment.isPresent()) {

      if (isBaseValueValid()) {
        ConfigurationData configurationData = this.viewModel.getConfigurationDataSalved();
        if (DateUtils.isValidPeriod(
            this.viewModel.getPaymentDate(), configurationData.getBaseDate())) {
          MaterialDialog mDialog =
              new MaterialDialog.Builder(this)
                  .setTitle("Salvar Recebimento?")
                  .setMessage("Você deseja realmente confirmar o recebimento?")
                  .setCancelable(true)
                  .setNegativeButton(
                      "Não",
                      R.mipmap.ic_clear_black_48dp,
                      (dialogInterface, which) -> dialogInterface.dismiss())
                  .setPositiveButton(
                      "Salvar",
                      R.mipmap.ic_save_white_48dp,
                      (dialogInterface, which) -> {
                        this.viewModel.setDescription(edtDescription.getText().toString());
                        this.viewModel.setPaymentValue(cetPrice.getCurrencyDouble());
                        this.viewModel.setPayment(this.viewModel.getPaymentToInsert());
                        new InsertPaymentTask(this.viewModel).execute();

                        this.viewModel.printPayment();

                        dialogInterface.dismiss();
                      })
                  .build();

          mDialog.show();

        } else {
          showMessage(this, "Data Base inferior a Data de Recebimento!");
        }
      } else {
        showMessage(this, "Por favor,digite um valor base válido!");
      }
    } else {

      this.viewModel.printPayment();
    }
  }

  @OnClick(R.id.edt_date_base)
  public void setOnClickBaseDate() {
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
              edtBaseDate.setText(strDay + "/" + strMonth + "/" + year);
              try {
                viewModel.setPaymentDate(
                    DateFormat.getDateInstance(DateFormat.DATE_FIELD)
                        .parse(edtBaseDate.getText().toString()));
              } catch (ParseException e) {
                showErrorMessage(PaymentActivity.this, e.getMessage());
              }
            },
            mYear,
            mMonth,
            mDay);
    datePickerDialog.show();
  }

  /** Realiza a validacao dos itens antes da insercao */
  private boolean isBaseValueValid() {
    if (TextUtils.isEmpty(cetPrice.getText().toString())) {
      cetPrice.setError("Valor Base Obrigatório!");
      cetPrice.requestFocus();
      return false;
    }

    return true;
  }

  @Override
  public void onBackPressed() {
    NavUtils.navigateUpFromSameTask(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.viewModel.closeConnection();
  }
}

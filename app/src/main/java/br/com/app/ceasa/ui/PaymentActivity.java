package br.com.app.ceasa.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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
import br.com.app.ceasa.util.Singleton;
import br.com.app.ceasa.viewmodel.PaymentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

  @BindView(R.id.cv_base_date)
  CalendarView cvDate;

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
        this.viewModel.setPaymentDate(this.viewModel.getConfigurationDataSalved().getBaseDate());
        cvDate.setDate( this.viewModel.getConfigurationDataSalved().getBaseDate().getTime());
      }

    } catch (ParseException e) {
      e.printStackTrace();
    }

    this.setClientData();
    this.checkInitialConfigure();

    cvDate.setOnDateChangeListener(
        (view, year, month, dayOfMonth) -> {
          // display the selected date by using a toast

          Calendar c = Calendar.getInstance();
          c.set(Calendar.YEAR, year);
          c.set(Calendar.MONTH, month);
          c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          DateFormat f = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
          String formatedPaymentDate = f.format(c.getTime());
          try {

            Date datePayment = f.parse(formatedPaymentDate);
            this.viewModel.setPaymentDate(datePayment);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });

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
      this.viewModel.setPaymentDate(format.parse(format.format(cvDate.getDate())));
    } catch (ParseException e) {
      showErrorMessage(this, e.getMessage());
    }
  }

  /*Configura os componentes para a atualizacao da venda*/

  private void checkInitialConfigure() {
    Optional<Payment> optionalPayment =
        Optional.ofNullable(this.viewModel.getPaymentByDateAndClient());
    if (optionalPayment.isPresent()) {
      // deixa tudo desabilitado
    } else {
      this.configureCreate();
    }
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

    if (isBaseValueValid()) {
      ConfigurationData configurationData=  this.viewModel.getConfigurationDataSalved();
      if (DateUtils.isValidPeriod(
          this.viewModel.getPaymentDate(),
              configurationData.getBaseDate())) {
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
                      new InsertPaymentTask(this.viewModel, this).execute();

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
  public void onBackPressed() {}

  @Override
  protected void onDestroy() {
    super.onDestroy();
    this.viewModel.closeConnection();
  }
}

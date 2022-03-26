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
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.tasks.InsertPaymentTask;
import br.com.app.ceasa.utils.CurrencyEditText;
import br.com.app.ceasa.utils.DateUtils;
import br.com.app.ceasa.utils.MonetaryFormatting;
import br.com.app.ceasa.utils.Singleton;
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

public class PaymentActivity extends AppCompatActivity {

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

  PaymentViewModel paymentViewModel;

  AbstractActivity abstractActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);
    initViews();
    paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
  }

  @Override
  protected void onStart() {
    super.onStart();
    try {
      abstractActivity = Singleton.getInstance(AbstractActivity.class);
      this.paymentViewModel.setContext(this);
      this.getParentActivityData();

      if (this.paymentViewModel.existConfigurationData()) {
        cetPrice.setText(
            MonetaryFormatting.convertToDolar(
                this.paymentViewModel.getConfigurationDataSalved().getBaseValue()));
      }

    } catch (InstantiationException | IllegalAccessException | ParseException e) {
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
            this.paymentViewModel.setPaymentDate(datePayment);
          } catch (ParseException e) {
            e.printStackTrace();
          }
        });
  }

  /*Configura os componentes para a criacao da venda*/
  private void configureCreate() {
    DateFormat format = DateFormat.getDateInstance(DateFormat.DATE_FIELD);
    try {
      this.paymentViewModel.setPaymentDate( format.parse(format.format(cvDate.getDate())));
    } catch (ParseException e) {
      abstractActivity.showErrorMessage(this, e.getMessage());
    }

  }

  /*Configura os componentes para a atualizacao da venda*/

  private void checkInitialConfigure() {
    Optional<Payment> optionalPayment =
        Optional.ofNullable(this.paymentViewModel.getPaymentByDateAndClient());
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
            + this.paymentViewModel.getClient().getId()
            + " - "
            + this.paymentViewModel.getClient().getName());
  }

  /*Obtem os dados da HomeActivity*/
  private void getParentActivityData() throws ParseException {

    Bundle args = getIntent().getExtras();

    if (args != null) {
      this.paymentViewModel.setClient((Client) args.getSerializable("keyClient"));
      this.paymentViewModel.setPaymentDate(
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


      if (DateUtils.isValidPeriod(
          this.paymentViewModel.getPaymentDate(),
          this.paymentViewModel.getConfigurationDataSalved().getBaseDate())) {
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
                      this.paymentViewModel.setDescription(edtDescription.getText().toString());
                      this.paymentViewModel.setPaymentValue(cetPrice.getCurrencyDouble());
                      this.paymentViewModel.setPayment(this.paymentViewModel.getPaymentToInsert());
                      new InsertPaymentTask(this.paymentViewModel, this).execute();

                      dialogInterface.dismiss();
                    })
                .build();

        mDialog.show();

      } else {
        abstractActivity.showMessage(this, "Data Base inferior a Data de Recebimento!");
      }
    } else {
      abstractActivity.showMessage(this, "Por favor,digite um valor base válido!");
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


}

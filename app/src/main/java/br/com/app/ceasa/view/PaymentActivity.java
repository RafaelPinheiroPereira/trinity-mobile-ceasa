package br.com.app.ceasa.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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
import br.com.app.ceasa.utils.Singleton;
import br.com.app.ceasa.viewmodel.PaymentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Optional;

public class PaymentActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.txt_cliente_dados)
  TextView dataClient;

  @BindView(R.id.cet_price)
  CurrencyEditText cetPrice;

  @BindView(R.id.edt_date_base)
  EditText baseDate;

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

    } catch (InstantiationException | IllegalAccessException | ParseException e) {
      e.printStackTrace();
    }

    this.setClientData();
    this.checkInitialConfigure();
  }

  /*Configura os componentes para a criacao da venda*/
  private void configureCreate() {}

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
    this.dataClient.setText("CLIENTE: "+this.paymentViewModel.getClient().getId()+" - " + this.paymentViewModel.getClient().getName());
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
    toolbar.setTitle("Trinity Mobile - Ceasa");
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
                    this.paymentViewModel.setPayment(this.paymentViewModel.getPaymentToInsert());
                    new InsertPaymentTask(this.paymentViewModel, this).execute();

                    dialogInterface.dismiss();
                  })
              .build();

      mDialog.show();

    } else {
      abstractActivity.showMessage(this, "Por favor,digite um valor base válido!");
    }
  }

  private boolean isPaymentValid() {
    return false;
  }

  /** Realiza a validacao dos itens antes da insercao */
  private boolean isBaseValueValid() {
    if (TextUtils.isEmpty(cetPrice.getText().toString())) {
      cetPrice.setError("Valor Base Obrigatório!");
      cetPrice.requestFocus();
      return false;
    }
    if (TextUtils.isEmpty(baseDate.getText().toString())) {
      baseDate.setError("Data Base Obrigatória!");
      baseDate.requestFocus();
      return false;
    }

    return true;
  }

  @Override
  public void onBackPressed() {}
}

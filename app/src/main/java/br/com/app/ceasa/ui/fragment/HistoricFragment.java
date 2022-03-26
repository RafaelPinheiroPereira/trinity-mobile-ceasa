package br.com.app.ceasa.ui.fragment;

import static br.com.app.ceasa.utils.Constants.EXTRA_DATE_PAYMENT;
import static br.com.app.ceasa.utils.Constants.TARGET_HISTORIC_FRAGMENT_REQUEST_CODE;
import static br.com.app.ceasa.utils.Constants.TARGET_HOME_FRAGMENT_REQUEST_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.ui.AbstractActivity;
import br.com.app.ceasa.ui.adapter.HistoricAdapter;
import br.com.app.ceasa.ui.dialog.DateSalePickerDialog;
import br.com.app.ceasa.utils.DateUtils;
import br.com.app.ceasa.utils.MonetaryFormatting;
import br.com.app.ceasa.utils.Singleton;
import br.com.app.ceasa.viewmodel.HistoricViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricFragment extends Fragment implements View.OnClickListener {

  @BindView(R.id.edt_date_sale)
  EditText edtDatePayment;

  @BindView(R.id.txt_total_value_historic)
  TextView txtValueTotal;

  @BindView(R.id.rcv_historic)
  RecyclerView rcvHistoric;

  AbstractActivity abstractActivity;
  private HistoricViewModel historicViewModel;
  HistoricAdapter historicAdapter;
  DialogFragment dialogDatePaymentFragment = new DateSalePickerDialog();

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.historic_fragment, container, false);
    ButterKnife.bind(this, view);
    try {
      abstractActivity = Singleton.getInstance(AbstractActivity.class);
    } catch (java.lang.InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return view;
  }

  @Override
  public void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    historicViewModel = new ViewModelProvider(requireActivity()).get(HistoricViewModel.class);
  }

  @Override
  public void onStart() {
    super.onStart();
    this.setAdapter();
    this.txtValueTotal.setText("Total :R$ 00,00");
    this.edtDatePayment.setOnClickListener(this);
    this.setDatePaymentToday();

    this.configInitialRecycle();
    try {
      loadHistoricByDate();
    } catch (ExecutionException e) {
      abstractActivity.showErrorMessage(getActivity(), e.getMessage());
    } catch (InterruptedException e) {
      abstractActivity.showErrorMessage(getActivity(), e.getMessage());
    } catch (ParseException e) {
      abstractActivity.showErrorMessage(getActivity(), e.getMessage());
    }
  }

  private void setAdapter() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    rcvHistoric.setLayoutManager(linearLayoutManager);
    historicAdapter = new HistoricAdapter(getActivity(), new ArrayList<>());
    rcvHistoric.setAdapter(historicAdapter);
  }

  private void configInitialRecycle() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    rcvHistoric.setLayoutManager(layoutManager);
  }

  private void loadHistoricByDate()
      throws ExecutionException, InterruptedException, ParseException {
    this.historicViewModel
        .getHistoricByDatePayment(this.historicViewModel.getConfigurationDataSalved().getBaseDate())
        .observe(
            this,
            historics -> {
              Collections.sort(historics, Comparator.comparing(Historic::getIdClient));
              historicAdapter = new HistoricAdapter(getActivity(), historics);
              rcvHistoric.setAdapter(historicAdapter);
              this.updateTxtTotalValue(historics);
            });
  }

  private void updateTxtTotalValue(List<Historic> historics) {
    txtValueTotal.setText("Total: "+MonetaryFormatting.convertToReal(historics.stream().mapToDouble(Historic::getValue).sum()));

  }

  @Override
  public void onClick(final View v) {
    switch (v.getId()) {
      case R.id.edt_date_sale:
        this.showDatePickerDialog();
        break;

      default:
        break;
    }
  }
  /*Exibe o picker para selecionar a data de venda*/
  private void showDatePickerDialog() {
    /*Se ainda nao foi instanciado*/
    if (!dialogDatePaymentFragment.isAdded()) {
      dialogDatePaymentFragment.setTargetFragment(
          HistoricFragment.this, TARGET_HISTORIC_FRAGMENT_REQUEST_CODE);
      dialogDatePaymentFragment.show(getParentFragmentManager(), "datePicker");
    }
    /*Caso ele já tenha sido instanciado eu removo, isto ocorre devida a baixa performace
     * do equipamento, uma vez que eh solicitado a exibicao do dialog o mesmo demora e o edit
     * text possibilita um segundo clique como primeiro ja instaciado */
    else {
      getParentFragmentManager().beginTransaction().remove(dialogDatePaymentFragment).commit();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == TARGET_HISTORIC_FRAGMENT_REQUEST_CODE) {
      this.edtDatePayment.setText(data.getStringExtra(EXTRA_DATE_PAYMENT));
      this.historicViewModel.setDatePayment(edtDatePayment.getText().toString());
      try {
        this.historicViewModel
            .getHistoricByDatePayment(
                DateFormat.getDateInstance(DateFormat.SHORT)
                    .parse(this.historicViewModel.getDatePayment()))
            .observe(
                this,
                historics -> {
                  Collections.sort(historics, Comparator.comparing(Historic::getIdClient));
                  historicAdapter = new HistoricAdapter(getActivity(), historics);
                  rcvHistoric.setAdapter(historicAdapter);
                });

      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  private void setDatePaymentToday() {
    edtDatePayment.setText(
        DateUtils.convertDateToStringInFormat_dd_mm_yyyy(new Date(System.currentTimeMillis())));
    this.historicViewModel.setDatePayment(edtDatePayment.getText().toString());
  }
}

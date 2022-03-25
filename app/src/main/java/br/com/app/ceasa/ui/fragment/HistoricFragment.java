package br.com.app.ceasa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.ui.AbstractActivity;
import br.com.app.ceasa.ui.adapter.HistoricAdapter;
import br.com.app.ceasa.utils.Singleton;
import br.com.app.ceasa.viewmodel.HistoricViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricFragment extends Fragment {

  @BindView(R.id.edt_date_sale)
  EditText edtDatePayment;

  @BindView(R.id.rcv_historic)
  RecyclerView rcvHistoric;

  AbstractActivity abstractActivity;
  private HistoricViewModel historicViewModel;
  HistoricAdapter historicAdapter;

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
    //this.edtDatePayment.setOnClickListener(this);
    //this.setDatePaymentToday();

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
  private void loadHistoricByDate() throws ExecutionException, InterruptedException, ParseException {
    this.historicViewModel
            .getHistoricByDatePayment(this.historicViewModel.getConfigurationDataSalved().getBaseDate())
            .observe(
                    this,
                    historics -> {
                      Collections.sort(historics, Comparator.comparing(Historic::getIdClient));
                      historicAdapter = new HistoricAdapter(getActivity(), historics);
                      rcvHistoric.setAdapter(historicAdapter);
                    });
  }
}

package br.com.app.rmalimentos.view.fragment;

import static br.com.app.rmalimentos.utils.Constants.EXTRA_DATE_SALE;
import static br.com.app.rmalimentos.utils.Constants.TARGET_FRAGMENT_REQUEST_CODE;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.listener.RecyclerViewOnClickListenerHack;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.model.entity.Route;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.DateUtils;
import br.com.app.rmalimentos.utils.Singleton;
import br.com.app.rmalimentos.view.AbstractActivity;
import br.com.app.rmalimentos.view.SaleActivity;
import br.com.app.rmalimentos.view.adapter.HomeAdapter;
import br.com.app.rmalimentos.view.dialog.ClientDataAlertDialog;
import br.com.app.rmalimentos.view.dialog.DateSalePickerDialog;
import br.com.app.rmalimentos.viewmodel.HomeViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment
    implements OnClickListener, RecyclerViewOnClickListenerHack {

  private HomeViewModel mViewModel;

  AbstractActivity abstractActivity;

  @BindView(R.id.spn_route)
  Spinner routeSpinner;

  @BindView(R.id.edt_date_sale)
  EditText edtDateSale;

  @BindView(R.id.rd_group_status)
  RadioGroup rdGroupStatus;

  @BindView(R.id.rd_all)
  RadioButton rdAll;

  @BindView(R.id.rd_positives)
  RadioButton rdPositives;

  @BindView(R.id.rd_not_positives)
  RadioButton rdNotPositives;

  @BindView(R.id.rcv_home)
  RecyclerView rcvHome;

  DialogFragment dialogDateSaleFragment = new DateSalePickerDialog();

  ArrayAdapter routesAdapter;

  HomeAdapter homeAdapter;

  private SearchView searchView = null;

  private SearchView.OnQueryTextListener queryTextListener;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.home_fragment, container, false);
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

    mViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
  }

  @Override
  public void onStart() {
    super.onStart();
    loadAllRoutes();
    this.setAdapter();
    this.edtDateSale.setOnClickListener(this);
    this.setDateSaleToday();
    rdAll.setChecked(true);
    this.configInitialRecycle();
    try {
      loadClientsAll();
    } catch (ExecutionException e) {
      abstractActivity.showErrorMessage(getActivity(), e.getMessage());
    } catch (InterruptedException e) {
      abstractActivity.showErrorMessage(getActivity(), e.getMessage());
    }

    rdAll.setOnCheckedChangeListener(
            (buttonView, isChecked)->{
              if (isChecked) {

                getAllClientsChecked();
              }
            });

    rdPositives.setOnCheckedChangeListener(
            (buttonView, isChecked)->{
              if (isChecked) {

                  try {
                      getAllPositived();
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
            });

    rdNotPositives.setOnCheckedChangeListener(
            (buttonView, isChecked)->{
              if (isChecked) {

                  try {
                      getAllNotPositived();
                  } catch (ParseException e) {
                      e.printStackTrace();
                  }
              }
            });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.home_menu, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchManager searchManager =
            (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

    if (searchItem != null) {
      searchView = (SearchView) searchItem.getActionView();
    }
    if (searchView != null) {
      searchView.setSearchableInfo(
              searchManager.getSearchableInfo(getActivity().getComponentName()));

      queryTextListener =
              new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String query) {

                  homeAdapter.getFilter().filter(query);
                  return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                  Log.i("onQueryTextSubmit", query);
                  homeAdapter.getFilter().filter(query);
                  return true;
                }
              };
      searchView.setOnQueryTextListener(queryTextListener);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_search:
        // Not implemented here
        return false;
      default:
        break;
    }
    searchView.setOnQueryTextListener(queryTextListener);
    return super.onOptionsItemSelected(item);
  }

    private void getAllNotPositived() throws ParseException {
    int position = this.getRouteSpinnerPosition();
    Route route = (Route) routesAdapter.getItem(position);
    LiveData<List<Client>> clientListLiveData =
            mViewModel.getNotPositived(edtDateSale.getText().toString(), route);
    clientListLiveData.observe(
            this,
            clients->{
              Collections.sort(clients, Comparator.comparing(Client::getRouteOrder));
              homeAdapter = new HomeAdapter(getActivity(), clients);

              rcvHome.setAdapter(homeAdapter);
              homeAdapter.setRecyclerViewOnClickListenerHack(this);
            });
  }

  private void getAllClientsChecked() {
    if (this.getRouteSpinnerPosition() != 0) {
      int position = this.getRouteSpinnerPosition();
        Route route = (Route) routeSpinner.getItemAtPosition(position);
      loadAllClientByRoute(route);
    } else {
      try {
        this.loadClientsAll();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

    private void getAllPositived() throws ParseException {
    int position = this.getRouteSpinnerPosition();
    Route route = (Route) routesAdapter.getItem(position);
    LiveData<List<Client>> clientListLiveData =
            mViewModel.getPositivedClients(edtDateSale.getText().toString(), route);
    clientListLiveData.observe(
            this,
            clients->{
              Collections.sort(clients, Comparator.comparing(Client::getRouteOrder));
              homeAdapter = new HomeAdapter(getActivity(), clients);

              rcvHome.setAdapter(homeAdapter);
              homeAdapter.setRecyclerViewOnClickListenerHack(this);
            });
  }

  private void setAdapter() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    rcvHome.setLayoutManager(linearLayoutManager);
    routesAdapter =
            new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, new ArrayList());
    homeAdapter = new HomeAdapter(getActivity(), new ArrayList<>());
    rcvHome.setAdapter(homeAdapter);
  }

  private void loadClientsAll() throws ExecutionException, InterruptedException {
    this.mViewModel
        .getClientsAll()
        .observe(
            this,
            clients -> {
              Collections.sort(clients, Comparator.comparing(Client::getRouteOrder));
              homeAdapter = new HomeAdapter(getActivity(), clients);
              rcvHome.setAdapter(homeAdapter);
              homeAdapter.setRecyclerViewOnClickListenerHack(this);
            });
  }

  private void configInitialRecycle() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    rcvHome.setLayoutManager(layoutManager);
  }

  private void setDateSaleToday() {
    edtDateSale.setText(
        DateUtils.convertDateToStringInFormat_dd_mm_yyyy(new Date(System.currentTimeMillis())));
      this.mViewModel.setDateSale(edtDateSale.getText().toString());
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
  /*Exibir detalhes dos clientes*/
  private void showClientDetails(final Client client) {

    DialogFragment dialog = new ClientDataAlertDialog(client, getActivity());

    /*Se ainda nao foi instanciado*/
    if (!dialog.isAdded()) {
      dialog.show(getParentFragmentManager(), "datePicker");
    }
    /*Caso ele já tenha sido instanciado eu removo, isto ocorre devida a baixa performace
     * do equipamento, uma vez que eh solicitado a exibicao do dialog o mesmo demora e o edit
     * text possibilita um segundo clique como primeiro ja instaciado */
    else {
      getParentFragmentManager().beginTransaction().remove(dialog).commit();
    }
  }

  /*Exibe o picker para selecionar a data de venda*/
  private void showDatePickerDialog() {
    /*Se ainda nao foi instanciado*/
    if (!dialogDateSaleFragment.isAdded()) {
      dialogDateSaleFragment.setTargetFragment(HomeFragment.this, TARGET_FRAGMENT_REQUEST_CODE);
      dialogDateSaleFragment.show(getParentFragmentManager(), "datePicker");
    }
    /*Caso ele já tenha sido instanciado eu removo, isto ocorre devida a baixa performace
     * do equipamento, uma vez que eh solicitado a exibicao do dialog o mesmo demora e o edit
     * text possibilita um segundo clique como primeiro ja instaciado */
    else {
      getParentFragmentManager().beginTransaction().remove(dialogDateSaleFragment).commit();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == TARGET_FRAGMENT_REQUEST_CODE) {
      this.edtDateSale.setText(data.getStringExtra(EXTRA_DATE_SALE));
        this.mViewModel.setDateSale(edtDateSale.getText().toString());
        loadClientsByGroupChecked();
    }
  }

  private void loadAllRoutes() {

    try {

      this.mViewModel
          .getAllRoutes()
          .observe(
              this,
              routes -> {
                routesAdapter =
                    new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, routes);
                routes.add(new Route("Selecionar Rota...", 0L));
                Collections.sort(routes, Comparator.comparing(Route::getId));
                routeSpinner.setAdapter(routesAdapter);
                routeSpinner.setSelection(this.getRouteSpinnerPosition());
              });
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClickListener(final View view, final int position) {

    switch (view.getId()) {
      case R.id.img_info:
        this.showClientDetails(homeAdapter.getItem(position));
        break;
      case R.id.btn_sale:
        navigateToSaleActivity(position);
        break;
      default:
        break;
    }
  }

  private void navigateToSaleActivity(final int position) {
    Intent intent = new Intent(getActivity(), SaleActivity.class);
    Bundle params = new Bundle();
    params.putSerializable("keyClient", homeAdapter.getItem(position));
    params.putString("keyDateSale", edtDateSale.getText().toString());
    intent.putExtras(params);
    startActivity(intent);
  }

  @Override
  public void onLongPressClickListener(final View view, final int position) {}

  @OnItemSelected(R.id.spn_route)
  void onRouteItemSelected(int position) {
      setRouteSpinnerPosition(position);
      loadClientsByGroupChecked();
  }

    private void loadClientsByGroupChecked() {
        if (rdAll.isChecked()) {
            this.getAllClientsChecked();
        } else if (rdPositives.isChecked()) {
            try {
                this.getAllPositived();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.getAllNotPositived();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

  private void loadAllClientByRoute(final Route route) {
    LiveData<List<Client>> listLiveData = this.mViewModel.getlAllClientByRoute(route);
    listLiveData.observe(
        this,
        clients -> {
          Collections.sort(clients, Comparator.comparing(Client::getRouteOrder));
          homeAdapter = new HomeAdapter(getActivity(), clients);

          rcvHome.setAdapter(homeAdapter);
          homeAdapter.setRecyclerViewOnClickListenerHack(this);
        });
  }

  private void setRouteSpinnerPosition(final int position) {
    SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putInt(Constants.SPINNER_KEY_POSITION, position);
    editor.commit();
  }

  private int getRouteSpinnerPosition() {
    SharedPreferences settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
    return settings.getInt(Constants.SPINNER_KEY_POSITION, 0);
  }
}

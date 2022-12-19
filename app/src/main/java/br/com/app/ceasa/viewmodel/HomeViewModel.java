package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Home;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.repository.ClientRepository;
import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.FileManagerRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.tasks.ImportDataTask;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.Singleton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

  private String TAG = this.getClass().getSimpleName();

  private ClientRepository clientRepository;

  private PaymentRepository paymentRepository;

  private ConfigurationDataRepository configurationDataRepository;

  private String datePayment;

  private Date today;

  FileManagerRepository fileManagerRepository;

  Context context;

  ProgressDialog progressDialog;

  List<Home> homes = new ArrayList<>();

  public HomeViewModel(@NonNull final Application application)
      throws IllegalAccessException, InstantiationException {
    super(application);

    clientRepository = new ClientRepository(application);
    paymentRepository = new PaymentRepository(application);
    fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
    configurationDataRepository = new ConfigurationDataRepository(application);

    if (today == null) {
      this.today = new Date(System.currentTimeMillis());
    }
  }

  public LiveData<List<Client>> getNotPositived(final String datePayment) throws ParseException {

    LiveData<List<Client>> clients =
        this.clientRepository.findNotPositived(
            DateFormat.getDateInstance(DateFormat.SHORT).parse(datePayment));

    return clients;
  }

  public void setPayments(List<Client> clients) {

    clients.stream()
        .forEach(
            client -> {
              List<Payment> payments = new ArrayList<>();
              this.paymentRepository.findPaymentClient(client.getId());
              client.setPayments(payments);
            });
  }

  public LiveData<List<Client>> getPositivedClients(final String datePayment)
      throws ParseException {

    LiveData<List<Client>> clients =
        this.clientRepository.findPositivedClient(
            DateFormat.getDateInstance(DateFormat.SHORT).parse(datePayment));
    return clients;
  }

  public void importData() throws IllegalAccessException, IOException, InstantiationException {
    new ImportDataTask(this).execute();


  }

  public void saveData() {
    saveClients();
  }

  public void createAppDirectory() throws IOException {

    fileManagerRepository.createAppDirectory(this.getContext());
  }

  private void saveClients() {
    this.clientRepository.saveAll(this.fileManagerRepository.getClients());

  }

  public void deleteClients() {
    this.clientRepository.deleteAll();
  }

  public FileManagerRepository getFileManagerRepository() {
    return fileManagerRepository;
  }



  public boolean containsInputFile() {
    return fileManagerRepository.containsInputFile(this.context);
  }

  public ProgressDialog getProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this.getContext());
    }

    return progressDialog;
  }

  public String getDatePayment() {
    return datePayment;
  }

  public void setDatePayment(final String datePayment) {
    this.datePayment = datePayment;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(final Context context) {
    this.context = context;
  }

  public List<Home> getHomes() {
    return homes;
  }

  public void setHomes(List<Home> homes) {
    this.homes = homes;
  }

  public ConfigurationData getConfigurationData() {
    return this.configurationDataRepository.findConfigurationData();
  }

  public Date getToday() throws ParseException {
    return (getParseToday(DateUtils.convertDateToStringInFormat_dd_mm_yyyy(this.today)));
  }

  public boolean isExistConfigurationData() {
    return this.getConfigurationData() != null ? true : false;
  }

  public void createConfigurationDataDefault() throws ParseException {
    ConfigurationData configurationData = new ConfigurationData();

    configurationData.setBaseValue(0.0);
    configurationData.setBaseDate(
        getParseToday(DateUtils.convertDateToStringInFormat_dd_mm_yyyy(this.getToday())));

    this.configurationDataRepository.insertConfigurationData(configurationData);
  }

  private Date getParseToday(String s) throws ParseException {
    return DateFormat.getDateInstance(DateFormat.SHORT).parse(s);
  }

  public void updateConfigurationData() throws ParseException {
    ConfigurationData configurationData = this.getConfigurationData();
    configurationData.setBaseDate(
        getParseToday(DateUtils.convertDateToStringInFormat_dd_mm_yyyy(this.getToday())));
    this.configurationDataRepository.updateConfigurationData(configurationData);
  }

  public List<Client> getClientsAll() {
   return  this.clientRepository.getAll();
  }


  public LiveData<List<Client>> getAllClientsLiveData() {
     return this.clientRepository.getAllClientsLiveData();
  }
}

package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.repository.ClientRepository;
import br.com.app.ceasa.repository.FileManagerRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.tasks.ImportDataTask;
import br.com.app.ceasa.utils.Singleton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

  private String TAG = this.getClass().getSimpleName();

  private ClientRepository clientRepository;

  private PaymentRepository paymentRepository;

  private String datePayment;

  FileManagerRepository fileManagerRepository;

  Context context;

  ProgressDialog progressDialog;

  public HomeViewModel(@NonNull final Application application)
      throws IllegalAccessException, InstantiationException {
    super(application);

    clientRepository = new ClientRepository(application);
    paymentRepository = new PaymentRepository(application);
    fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
  }

  public LiveData<List<Client>> getNotPositived(final String dateSale) throws ParseException {
    return this.clientRepository.findNotPositived(
        DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale));
  }

  public LiveData<List<Client>> getPositivedClients(final String dateSale) throws ParseException {
    return this.clientRepository.findPositivedClient(
        DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale));
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

  public FileManagerRepository getFileManagerRepository() {
    return fileManagerRepository;
  }

  public LiveData<List<Client>> getClientsAll() {
    return clientRepository.getAll();
  }

  public boolean containsAllFiles() {
    return fileManagerRepository.containsAllFiles();
  }

  public StringBuilder searchInexistsFilesNames() {
    return fileManagerRepository.searchInexistsFilesNames();
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
}

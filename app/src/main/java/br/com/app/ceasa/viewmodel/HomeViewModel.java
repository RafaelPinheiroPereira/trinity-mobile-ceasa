package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.repository.ClientRepository;
import br.com.app.ceasa.repository.EmployeeRepository;
import br.com.app.ceasa.repository.FileManagerRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.repository.SaleRepository;
import br.com.app.ceasa.tasks.ImportDataTask;
import br.com.app.ceasa.utils.Singleton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private ClientRepository clientRepository;

    private PaymentRepository paymentRepository;

    private SaleRepository saleRepository;

    private EmployeeRepository employeeRepository;

    private String dateSale;

    FileManagerRepository fileManagerRepository;

    Context context;

    ProgressDialog progressDialog;

    public HomeViewModel(@NonNull final Application application)
            throws IllegalAccessException, InstantiationException {
        super(application);

        clientRepository = new ClientRepository(application);
        paymentRepository = new PaymentRepository(application);
        fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
        saleRepository = new SaleRepository(application);
        employeeRepository = new EmployeeRepository(application);

    }

    public LiveData<List<Client>> getNotPositived(final String dateSale) throws ParseException {
        return this.clientRepository
                .findNotPositived(DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale));
    }

    public LiveData<List<Client>> getPositivedClients(final String dateSale) throws ParseException {
        return this.clientRepository.findPositivedClient(
                DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale));

    }

    public void importData() throws IllegalAccessException, IOException, InstantiationException {
        new ImportDataTask(this).execute();
    }

    public void logout() {
        this.employeeRepository.removeUserSession();
    }

    public void saveData() {

        savePayments();
        saveClients();
    }

    private void savePayments() {
        this.paymentRepository.saveAll(this.fileManagerRepository.getPayments());
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

    public String getDateSale() {
        return dateSale;
    }

    public void setDateSale(final String dateSale) {
        this.dateSale = dateSale;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }
}

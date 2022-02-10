package br.com.app.rmalimentos.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.model.entity.Route;
import br.com.app.rmalimentos.repository.ClientRepository;
import br.com.app.rmalimentos.repository.EmployeeRepository;
import br.com.app.rmalimentos.repository.FileManagerRepository;
import br.com.app.rmalimentos.repository.PaymentRepository;
import br.com.app.rmalimentos.repository.PriceRepository;
import br.com.app.rmalimentos.repository.ProductRepository;
import br.com.app.rmalimentos.repository.RouteRepository;
import br.com.app.rmalimentos.repository.SaleRepository;
import br.com.app.rmalimentos.repository.UnityRepository;
import br.com.app.rmalimentos.tasks.ImportDataTask;
import br.com.app.rmalimentos.utils.Singleton;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeViewModel extends AndroidViewModel {

  private String TAG = this.getClass().getSimpleName();

  private RouteRepository routeRepository;
  private ClientRepository clientRepository;
  private ProductRepository productRepository;
  private UnityRepository unityRepository;
  private PaymentRepository paymentRepository;
  private PriceRepository priceRepository;

    private SaleRepository saleRepository;

    private EmployeeRepository employeeRepository;

    private String dateSale;

  FileManagerRepository fileManagerRepository;
  Context context;

  ProgressDialog progressDialog;

  public HomeViewModel(@NonNull final Application application)
      throws IllegalAccessException, InstantiationException {
    super(application);
    routeRepository = new RouteRepository(application);
    clientRepository = new ClientRepository(application);
    productRepository= new ProductRepository(application);
    unityRepository= new UnityRepository(application);
    paymentRepository= new PaymentRepository(application);
    priceRepository= new PriceRepository(application);
    fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
      saleRepository = new SaleRepository(application);
      employeeRepository = new EmployeeRepository(application);

  }

    public LiveData<List<Client>> getNotPositived(final String dateSale, final Route route) throws ParseException {
        return this.clientRepository
                .findNotPositived(DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale), route.getId());
  }

  public LiveData<List<Client>> getPositivedClients(final String dateSale,
          final Route route) throws ParseException {
      return this.clientRepository.findPositivedClient(
              DateFormat.getDateInstance(DateFormat.SHORT).parse(dateSale), route.getId());

    }

    public LiveData<List<Client>> getlAllClientByRoute(final Route route) {
         return this.clientRepository.getAllClientByRoute(route.getId());
    }

    public void importData() throws IllegalAccessException, IOException, InstantiationException {
    new ImportDataTask(this).execute();
  }

    public void logout() {
        this.employeeRepository.removeUserSession();
    }

  public void saveData() {
    saveRoutes();
    saveProducts();
    saveUnities();
    savePayments();
    savePrices();
    saveClients();
  }



  private void savePrices() {
    this.priceRepository.saveAll(this.fileManagerRepository.getPrices());
  }

  private void savePayments() {
    this.paymentRepository.saveAll(this.fileManagerRepository.getPayments());
  }

  private void saveUnities() {
    this.unityRepository.saveAll(this.fileManagerRepository.getUnities());
  }

  private void saveProducts() {
      this.productRepository.saveAll(fileManagerRepository.getProducts());
    }

    private void saveClients() {
    this.clientRepository.saveAll(this.fileManagerRepository.getClients());
  }

  private void saveRoutes() {
    this.routeRepository.saveAll(fileManagerRepository.getRoutes());
  }

  public FileManagerRepository getFileManagerRepository() {
    return fileManagerRepository;
  }

  public LiveData<List<Route>> getAllRoutes() throws ExecutionException, InterruptedException {
    return routeRepository.getAll();
  }

  public LiveData<List<Client>> getClientsAll() throws ExecutionException, InterruptedException{
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

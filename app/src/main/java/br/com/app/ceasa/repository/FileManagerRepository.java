package br.com.app.ceasa.repository;

import android.content.Context;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.utils.FileManager;
import br.com.app.ceasa.utils.Singleton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class FileManagerRepository {

    ProductFileRepository productFileRepository;
    UnityFileRepository unityFileRepository;
    ClientFileRepository clientFileRepository;
    PaymentFileRepository paymentFileRepository;
    PriceFileRepository priceFileRepository;
    RouteFileRepository routeFileRepository;
    ClientFileRepository clientFileRepository;

    SaleFileRepository saleFileRepository;
    Employee employee;

    FileManager fileManager;

    public FileManagerRepository() throws IllegalAccessException, InstantiationException {

        fileManager = Singleton.getInstance(FileManager.class);
    }

    public boolean containsAllFiles() {

        return fileManager.containsAllFiles();
    }

    public void createAppDirectory(final Context context) throws FileNotFoundException {
        this.fileManager.createAppDirectory(context);
    }

    public void downloadFiles() throws IOException, IllegalAccessException, InstantiationException {

        productFileRepository = Singleton.getInstance(ProductFileRepository.class);
        unityFileRepository = Singleton.getInstance(UnityFileRepository.class);
        clientFileRepository = Singleton.getInstance(ClientFileRepository.class);
        paymentFileRepository = Singleton.getInstance(PaymentFileRepository.class);
        priceFileRepository = Singleton.getInstance(PriceFileRepository.class);
        routeFileRepository = Singleton.getInstance(RouteFileRepository.class);
        unityFileRepository = Singleton.getInstance(UnityFileRepository.class);

        readFiles();
    }

    public void uploadFile(Employee employee, List<Sale> sales, Context context)
            throws IllegalAccessException, InstantiationException, FileNotFoundException {
        saleFileRepository = Singleton.getInstance(SaleFileRepository.class);
        saleFileRepository.writeFile(employee, sales, context);

    }

    private void readFiles() throws IOException {

        clientFileRepository.readFile();

    }


    public boolean fileExists(final String inputFile) {
        return fileManager.fileExists(inputFile);
    }

    public void readEmployeeFile()
            throws IllegalAccessException, InstantiationException, IOException {
        clientFileRepository = Singleton.getInstance(ClientFileRepository.class);
        clientFileRepository.readFile();
        this.setEmployee(clientFileRepository.getEmployee());
    }

    public StringBuilder searchInexistsFilesNames() {
        return fileManager.searchInexistsFilesNames();
    }

    public Employee getEmployee() {
        return employee;
    }

    private void setEmployee(final Employee employee) {
        this.employee = employee;
    }


    public List<Client> getClients() {
        return clientFileRepository.getClients();
    }


    public List<Payment> getPayments() {
        return this.paymentFileRepository.getPayments();
    }

}

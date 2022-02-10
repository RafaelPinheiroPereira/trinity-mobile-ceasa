package br.com.app.rmalimentos.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.model.entity.Employee;
import br.com.app.rmalimentos.model.entity.Sale;
import br.com.app.rmalimentos.repository.EmployeeRepository;
import br.com.app.rmalimentos.repository.FileManagerRepository;
import br.com.app.rmalimentos.repository.SaleItemRepository;
import br.com.app.rmalimentos.repository.SaleRepository;
import br.com.app.rmalimentos.tasks.ExportDataTask;
import br.com.app.rmalimentos.utils.Singleton;
import io.reactivex.annotations.NonNull;
import java.util.Date;
import java.util.List;

public class ExportViewModel extends AndroidViewModel {

    Date initialDate;

    Date finalDate;

    SaleRepository saleRepository;

    SaleItemRepository saleItemRepository;

    EmployeeRepository employeeRepository;

    List<Sale> sales;

    Context context;

    ProgressDialog progressDialog;

    FileManagerRepository fileManagerRepository;

    Employee employee;

    public ExportViewModel(@NonNull final Application application)
            throws IllegalAccessException, InstantiationException {
        super(application);
        saleRepository = new SaleRepository(application);
        saleItemRepository = new SaleItemRepository(application);
        fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
        employeeRepository = new EmployeeRepository(application);
    }

    public void exportData() {
        new ExportDataTask(this).execute();
    }

    public void loadAllSaleItem() {
        this.getSales()
                .forEach(
                        sale->{
                            sale.setSaleItemList(this.saleItemRepository.findItensToExport(sale.getId()));
                        });
    }

    public LiveData<List<Sale>> searchDataToExportByDate() {
        return this.saleRepository.findDataToExportByDate(this.getInitialDate(), this.getFinalDate());
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(final Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(final Date finalDate) {
        this.finalDate = finalDate;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(final List<Sale> sales) {
        this.sales = sales;
    }

    public ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.getContext());
        }

        return progressDialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public FileManagerRepository getFileManagerRepository() {
        return fileManagerRepository;
    }

    public void setFileManagerRepository(final FileManagerRepository fileManagerRepository) {
        this.fileManagerRepository = fileManagerRepository;
    }

    public Employee findSessionEmployee() {
        return this.employeeRepository.findSessionEmployee();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }
}

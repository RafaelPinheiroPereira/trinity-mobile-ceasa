package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.SaleDAO;
import br.com.app.rmalimentos.model.entity.Sale;
import java.util.Date;
import java.util.List;

public class SaleRepository  {
    private SaleDAO saleDAO;
    private AppDataBase appDataBase;

    public SaleRepository(Application application) {
        this.appDataBase = AppDataBase.getDatabase(application);
        saleDAO=appDataBase.saleDAO();
    }

    public void createSale(Sale sale) {
       this.saleDAO.insert(sale);
    }

    public LiveData<List<Sale>> findDataToExportByDate(Date initialDate, Date finalDate) {
        return this.saleDAO.findDataToExportByDate(initialDate, finalDate);
    }

    public Long findLastId() {
       return  this.saleDAO.findLastId();
    }

    public LiveData<Sale> findSaleByDate(Date dateSale) {

        return this.saleDAO.findSaleByDate(dateSale);
    }

    public Sale findSaleByDateAndClient(final Date dateSale, final Long clientId) {
        return this.saleDAO.findSaleByDateAndClient(dateSale,clientId);
    }

    public void updateSale(final Sale sale) {
        this.saleDAO.update(sale);
    }
}

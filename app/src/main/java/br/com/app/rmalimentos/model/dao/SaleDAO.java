package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Sale;
import java.util.Date;
import java.util.List;

@Dao
public abstract class SaleDAO extends  GenericDAO<Sale>    {
    @Query(value = "select * from sale  ORDER BY id")
    public abstract LiveData<List<Sale>> getAll();
    @Query(value="SELECT * FROM sale WHERE date_sale = :dateSale ")
    public abstract LiveData<Sale> findSaleByDate(Date dateSale);

    @Query(value = "SELECT * FROM sale WHERE date_sale>= :initialDate and date_sale<= :finalDate ")
    public abstract LiveData<List<Sale>> findDataToExportByDate(Date initialDate, Date finalDate);

    @Query(value="SELECT MAX(id) FROM sale ")
    public abstract Long findLastId();

    @Query(value = "SELECT * FROM sale WHERE date_sale= :dateSale and client_id = :clientId ")
    public abstract Sale findSaleByDateAndClient(Date dateSale, Long clientId);

    private class OperationsAsyncTask extends AsyncTask<Sale, Void, Void> {

        SaleDAO mAsyncTaskDao;

        OperationsAsyncTask(SaleDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Sale... sales) {
            return null;
        }



    }
    private class InsertAsyncTask extends SaleDAO.OperationsAsyncTask {

        InsertAsyncTask(SaleDAO saleDAO) {
            super(saleDAO);
        }

        @Override
        protected Void doInBackground(Sale... sales) {
            mAsyncTaskDao.insert(sales[0]);


            return null;
        }


    }



    @Override
    public void save(final Sale obj) {
       new SaleDAO.InsertAsyncTask(this).execute(obj);


    }
}

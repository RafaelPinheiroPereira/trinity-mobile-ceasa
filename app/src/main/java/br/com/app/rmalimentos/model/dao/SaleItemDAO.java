package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.SaleItem;
import java.util.List;

@Dao
public abstract class SaleItemDAO extends  GenericDAO<SaleItem> {

    @Query("select * from saleitem where  sale_id = :saleId")
    public abstract List<SaleItem> findItensBySale(Long saleId);

    @Query("select * from saleitem where  sale_id = :saleId")
    public abstract List<SaleItem> findItensToExport(Long saleId);

    private class OperationsAsyncTask extends AsyncTask<SaleItem, Void, Void> {

        SaleItemDAO mAsyncTaskDao;


        OperationsAsyncTask(SaleItemDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(SaleItem... saleItems) {
            return null;
        }

    }
    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(SaleItemDAO saleItemDAO) {
            super(saleItemDAO);
        }

        @Override
        protected Void doInBackground(SaleItem... saleItems) {
            mAsyncTaskDao.insert(saleItems[0]);
            return null;
        }


    }
    @Override
    public void save(final SaleItem obj) {

        new SaleItemDAO.InsertAsyncTask(this).execute(obj);

    }

}

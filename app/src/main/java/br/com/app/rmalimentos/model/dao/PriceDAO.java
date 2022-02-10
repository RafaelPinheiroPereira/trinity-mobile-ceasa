package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Price;
import java.util.List;

@Dao
public abstract class PriceDAO  extends GenericDAO<Price> {



    @Query("select * from price order by id")
    public abstract LiveData<List<Price>> getAll();
    @Query("select * from price where product_id = :productId and unity_code like :unityCode  order by id")
    public abstract Price findPriceByUnitAndProduct(final Long productId, final String unityCode);
    private class OperationsAsyncTask extends AsyncTask<Price, Void, Void> {

        PriceDAO mAsyncTaskDao;

        OperationsAsyncTask(PriceDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Price... prices) {
            return null;
        }
    }
    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(PriceDAO priceDAO) {
            super(priceDAO);
        }

        @Override
        protected Void doInBackground(Price... prices) {
            mAsyncTaskDao.insert(prices[0]);
            return null;
        }
    }
    @Override
    public void save(final Price obj) {

        new PriceDAO.InsertAsyncTask(this).execute(obj);

    }
}

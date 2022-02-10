package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Unity;
import java.util.List;

@Dao
public abstract class UnityDAO extends  GenericDAO<Unity> {



    @Query("select * from unity order by id")
    public abstract LiveData<List<Unity>> getAll();
    @Query("select * from unity  where product_id=:productId   order by standard desc")
    public abstract List<Unity> findUnitiesByProduct(final Long productId);

    private class OperationsAsyncTask extends AsyncTask<Unity, Void, Void> {

        UnityDAO mAsyncTaskDao;

        OperationsAsyncTask(UnityDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Unity... unities) {
            return null;
        }
    }
    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(UnityDAO routeDAO) {
            super(routeDAO);
        }

        @Override
        protected Void doInBackground(Unity... unities) {
            mAsyncTaskDao.insert(unities[0]);
            return null;
        }
    }
    @Override
    public void save(final Unity obj) {

        new UnityDAO.InsertAsyncTask(this).execute(obj);

    }

}

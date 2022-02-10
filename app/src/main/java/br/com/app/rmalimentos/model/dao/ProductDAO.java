package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Product;
import java.util.List;

@Dao
public abstract class ProductDAO extends GenericDAO<Product> {
    @Query("select * from product  where id = :productId order by id")
    public abstract Product findProductById(final long productId);

    @Query("select * from product order by id")
    public abstract List<Product> getAll();
    private class OperationsAsyncTask extends AsyncTask<Product, Void, Void> {

        ProductDAO mAsyncTaskDao;

        OperationsAsyncTask(ProductDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            return null;
        }
    }
    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(ProductDAO productDAO) {
            super(productDAO);
        }

        @Override
        protected Void doInBackground(Product... products) {
            mAsyncTaskDao.insert(products[0]);
            return null;
        }
    }
    @Override
    public void save(final Product obj) {

        new ProductDAO.InsertAsyncTask(this).execute(obj);

    }
}

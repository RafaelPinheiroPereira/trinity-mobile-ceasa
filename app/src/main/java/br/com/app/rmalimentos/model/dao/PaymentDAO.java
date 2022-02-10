package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Payment;
import java.util.List;

@Dao
public abstract  class PaymentDAO  extends GenericDAO<Payment> {


    @Query("select * from payment order by id")
    public abstract List<Payment> getAll();
    private class OperationsAsyncTask extends AsyncTask<Payment, Void, Void> {

        PaymentDAO mAsyncTaskDao;

        OperationsAsyncTask(PaymentDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Payment... payments) {
            return null;
        }
    }
    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(PaymentDAO paymentDAO) {
            super(paymentDAO);
        }

        @Override
        protected Void doInBackground(Payment... payments) {
            mAsyncTaskDao.insert(payments[0]);
            return null;
        }
    }
    @Override
    public void save(final Payment obj) {

        new PaymentDAO.InsertAsyncTask(this).execute(obj);

    }
}

package br.com.app.ceasa.model.dao;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.ceasa.model.entity.Payment;

import java.util.Date;
import java.util.List;

@Dao
public abstract  class PaymentDAO  extends GenericDAO<Payment> {


    @Query("select * from payment order by id")
    public abstract List<Payment> getAll();

    @Query(value="SELECT MAX(id) FROM payment ")
    public abstract Long findLastId();

    @Query(value = "SELECT * FROM payment WHERE date_sale= :datePayment and id_client = :clientId ")
    public abstract Payment findPaymentByDateAndClient(Date datePayment, Long clientId);

    @Query(value = "SELECT * FROM payment WHERE date_sale>= :initialDate and date_sale<= :finalDate ")
    public abstract LiveData<List<Payment>> findDataToExportByDate(Date initialDate, Date finalDate);

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

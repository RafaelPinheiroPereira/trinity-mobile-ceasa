package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.PaymentDAO;
import br.com.app.rmalimentos.model.entity.Payment;
import java.util.List;

public class PaymentRepository {
    private PaymentDAO paymentDAO;

    private LiveData<List<Payment>> listLiveData;

    private AppDataBase appDataBase;

    public PaymentRepository(Application application) {
        appDataBase = AppDataBase.getDatabase(application);
        paymentDAO = appDataBase.paymentDAO();
    }

    public List<Payment> getAll() {

        return paymentDAO.getAll();
    }

    public void saveAll(final List<Payment> payments) {

            this.paymentDAO.save(payments.toArray(new Payment[payments.size()]));

    }
}

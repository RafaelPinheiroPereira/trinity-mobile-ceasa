package br.com.app.ceasa.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.PaymentDAO;
import br.com.app.ceasa.model.entity.Payment;

import java.util.Date;
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

  public Payment findPaymentByDateAndClient(final Date datePayment, final Long clientId) {
    return this.paymentDAO.findPaymentByDateAndClient(datePayment, clientId);
  }

  public LiveData<List<Payment>> findDataToExportByDate(Date initialDate, Date finalDate) {
    return this.paymentDAO.findDataToExportByDate(initialDate, finalDate);
  }

  public void saveAll(final List<Payment> payments) {

    this.paymentDAO.save(payments.toArray(new Payment[payments.size()]));
  }

  public void insertPayment(Payment payment) {
    this.paymentDAO.insert(payment);
  }

  public Long findLastId() {
    return this.paymentDAO.findLastId();
  }
}

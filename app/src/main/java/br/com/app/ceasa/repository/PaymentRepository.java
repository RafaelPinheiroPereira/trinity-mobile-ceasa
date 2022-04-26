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

  private AppDataBase appDataBase;

  public PaymentRepository(Application application) {
    appDataBase = AppDataBase.getDatabase(application);
    paymentDAO = appDataBase.paymentDAO();
  }

  public Payment findPaymentByDateAndClient(final Date datePayment, final Long clientId) {
    return this.paymentDAO.findPaymentByDateAndClient(datePayment, clientId);
  }

  public List<Payment> findPaymentClient(final Long clientId) {
    return this.paymentDAO.findPaymentClient(clientId);
  }

  public LiveData<List<Payment>> findDataToExportByDate(Date initialDate, Date finalDate) {
    return this.paymentDAO.findDataToExportByDate(initialDate, finalDate);
  }

  public void insertPayment(Payment payment) {
    this.paymentDAO.insert(payment);
  }

  public void clearDatabaseByDates(Date initialDate, Date finalDate) {
    this.paymentDAO.deletePaymentByDates(initialDate, finalDate);
  }
}

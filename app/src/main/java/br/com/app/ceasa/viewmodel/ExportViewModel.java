package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.repository.FileManagerRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.tasks.ExportDataTask;
import br.com.app.ceasa.util.Singleton;
import io.reactivex.annotations.NonNull;
import java.util.Date;
import java.util.List;

public class ExportViewModel extends AndroidViewModel {

  Date initialDate;

  Date finalDate;

  PaymentRepository paymentRepository;

  List<Payment> payments;

  Context context;

  ProgressDialog progressDialog;

  FileManagerRepository fileManagerRepository;


  public ExportViewModel(@NonNull final Application application)
      throws IllegalAccessException, InstantiationException {
    super(application);

    fileManagerRepository = Singleton.getInstance(FileManagerRepository.class);
    paymentRepository = new PaymentRepository(application);
  }

  public void exportData() {
    new ExportDataTask(this).execute();
  }


  public LiveData<List<Payment>> searchDataToExportByDate() {
    return this.paymentRepository.findDataToExportByDate(this.getInitialDate(), this.getFinalDate());
  }

  public Date getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(final Date initialDate) {
    this.initialDate = initialDate;
  }

  public Date getFinalDate() {
    return finalDate;
  }

  public void setFinalDate(final Date finalDate) {
    this.finalDate = finalDate;
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(final List<Payment> payments) {
    this.payments = payments;
  }

  public ProgressDialog getProgressDialog() {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this.getContext());
    }

    return progressDialog;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(final Context context) {
    this.context = context;
  }

  public FileManagerRepository getFileManagerRepository() {
    return fileManagerRepository;
  }

  public void setFileManagerRepository(final FileManagerRepository fileManagerRepository) {
    this.fileManagerRepository = fileManagerRepository;
  }

}

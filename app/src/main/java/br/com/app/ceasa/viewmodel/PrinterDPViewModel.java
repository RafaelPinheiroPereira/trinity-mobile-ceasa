package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.repository.PrinterDPRepository;

public class PrinterDPViewModel extends AndroidViewModel {
  PrinterDPRepository printerDPRepository;


  Context context;

  public PrinterDPViewModel(@NonNull final Application application) {
    super(application);
    printerDPRepository = new PrinterDPRepository(application);
  }

  public boolean isActivedPrinter(){
    return printerDPRepository.isActivedPrinter();
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }
}

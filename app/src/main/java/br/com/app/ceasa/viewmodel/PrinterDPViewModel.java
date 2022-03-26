package br.com.app.ceasa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.repository.PrinterDPRepository;

public class PrinterDPViewModel extends AndroidViewModel {
  PrinterDPRepository printerDPRepository;

  public PrinterDPViewModel(@NonNull final Application application) {
    super(application);
    printerDPRepository = new PrinterDPRepository(application);
  }
}

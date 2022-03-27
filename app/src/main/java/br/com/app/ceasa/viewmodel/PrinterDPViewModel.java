package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.com.app.ceasa.model.entity.PrinterDP;
import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.repository.PrinterDPRepository;

public class PrinterDPViewModel extends AndroidViewModel {
  PrinterDPRepository printerDPRepository;
  Context context;

  PrinterDP printerDP;

  public PrinterDPViewModel(@NonNull final Application application) {
    super(application);
    printerDPRepository = new PrinterDPRepository(application);
  }

  public boolean isActivedPrinter() {
    return printerDPRepository.isActivedPrinter();
  }

  public PrinterDP searchActivedPrinter() throws Throwable {
    return this.printerDPRepository.findActivedPrinter();
  }

  public PrinterDP searchPrinterByMac(String mac) throws Throwable {
    return this.printerDPRepository.searchPrinterByMac(mac);
  }

  public void updatePrinter(PrinterDP printerDP) {
    this.printerDPRepository.updatePrinter(printerDP);
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public PrinterDP getPrinterDP() {
    return printerDP;
  }

  public void setPrinterDP(PrinterDP printerDP) {
    this.printerDP = printerDP;
  }

  public Long getLastId() {
    return this.printerDPRepository.findLastId();
  }

  public void insertPrinter() {
    Long lastId = this.getLastId();
    if (lastId != null) {
      this.getPrinterDP().setId(lastId + 1);
    } else {
      this.getPrinterDP().setId(1L);
    }

    this.printerDPRepository.insertPrintDP(this.getPrinterDP());
  }
}

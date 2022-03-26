package br.com.app.ceasa.repository;

import android.app.Application;

import java.util.Optional;

import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.PrinterDPDAO;
import br.com.app.ceasa.model.entity.PrinterDP;

public class PrinterDPRepository {
  private PrinterDPDAO printerDPDAO;
  private AppDataBase appDataBase;

  public PrinterDPRepository(Application application) {
    appDataBase = AppDataBase.getDatabase(application);
    printerDPDAO = appDataBase.printerDPDAO();
  }

  public PrinterDP findActivedPrinter() throws Throwable {
    return Optional.ofNullable(printerDPDAO.findActivedPrinter()).orElseThrow(Exception::new);
  }

  public boolean isActivedPrinter() {
    return Optional.ofNullable(printerDPDAO.findActivedPrinter()).isPresent();
  }

  public PrinterDP searchPrinterByMac(String mac) throws Throwable {
    return Optional.ofNullable(printerDPDAO.searchPrinterByMac(mac)).orElseThrow(Exception::new);
  }

}

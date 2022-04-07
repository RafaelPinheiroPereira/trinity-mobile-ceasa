package br.com.app.ceasa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.HistoricRepository;
import br.com.app.ceasa.repository.PrinterDPRepository;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.PrinterDatecsUtil;

public class HistoricViewModel extends AndroidViewModel {

  private String datePayment;
  private PrinterDatecsUtil printerDatecsUtil;
  private List<Historic> historics= new ArrayList<>();

  HistoricRepository historicRepository;
  ConfigurationDataRepository configurationDataRepository;
  PrinterDPRepository printerDPRepository;

  public HistoricViewModel(@NonNull Application application) {
    super(application);
    historicRepository = new HistoricRepository(application);
    configurationDataRepository = new ConfigurationDataRepository(application);
    printerDPRepository= new PrinterDPRepository(application);
  }

  public LiveData<List<Historic>> getHistoricByDatePayment(final Date datePayment)
      throws ParseException {
    return this.historicRepository.findHistorictByDate(datePayment);
  }

  public ConfigurationData getConfigurationDataSalved() {
    return this.configurationDataRepository.findConfigurationData();
  }

  public String getDatePayment() {
    return datePayment;
  }

  public void setDatePayment(String datePayment) {
    this.datePayment = datePayment;
  }

  public PrinterDatecsUtil getPrinterDatecsUtil() {
    return printerDatecsUtil;
  }

  public List<Historic> getHistorics() {
    return historics;
  }

  public void setHistorics(List<Historic> historics) {
    this.historics = historics;
  }

  public void setPrinterDatecsUtil(PrinterDatecsUtil printerDatecsUtil) {
    this.printerDatecsUtil = printerDatecsUtil;
  }

  public void waitForConnection() throws Throwable {
    this.printerDatecsUtil.waitForConection(this.printerDPRepository.findActivedPrinter());
  }

  public boolean isAtivedPrinter() {
    return this.printerDPRepository.isActivedPrinter();
  }

  public void printHistoric() {
    Collections.sort(this.getHistorics(), Comparator.comparing(Historic::getValue));
    this.getPrinterDatecsUtil().printHistoric(this.getHistorics());
  }

  public void closeConnection() {
    this.getPrinterDatecsUtil().closeConnection();
  }

  public Date getToday() throws ParseException {
      Date dateToday =
            DateFormat.getDateInstance(DateFormat.SHORT)
                    .parse(
                            DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
                                    new Date(System.currentTimeMillis())));
    return dateToday;
  }
}

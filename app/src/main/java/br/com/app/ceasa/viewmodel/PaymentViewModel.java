package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.PaymentRepository;
import br.com.app.ceasa.repository.PrinterDPRepository;
import br.com.app.ceasa.util.PrinterDatecsUtil;

import java.util.Date;
import java.util.Optional;

public class PaymentViewModel extends AndroidViewModel {

  /*Objetos da tela*/
  Client client;

  Date paymentDate;

  Date dateInitial;

  Date dateFinal;

  Payment payment;

  String description;

  Context context;

  Double paymentValue;

  PrinterDatecsUtil printerDatecsUtil;

  /*Repositorios de acesso ao dados */
  PaymentRepository paymentRepository;
  ConfigurationDataRepository configurationDataRepository;
  PrinterDPRepository printerDPRepository;

  public PaymentViewModel(@NonNull final Application application) {
    super(application);
    paymentRepository = new PaymentRepository(application);
    configurationDataRepository = new ConfigurationDataRepository(application);
    printerDPRepository= new PrinterDPRepository(application);
  }

  public boolean existConfigurationData() {
    Optional<ConfigurationData> configurationDataOptional =
        Optional.ofNullable(this.configurationDataRepository.findConfigurationData());
    return configurationDataOptional.isPresent();
  }

  public Client getClient() {
    return client;
  }

  public Long getLastId() {
    return this.paymentRepository.findLastId();
  }

  public Payment getPaymentByDateAndClient() {
    return this.paymentRepository.findPaymentByDateAndClient(
        this.getPaymentDate(), this.getClient().getId());
  }

  public void insertPayment() {
    Long lastId = this.getLastId();
    if (lastId != null) {
      this.getPayment().setId(lastId + 1);
    } else {
      this.getPayment().setId(1L);
    }

    this.paymentRepository.insertPayment(this.getPayment());
  }

  public void setClient(final Client client) {
    this.client = client;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(final Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public Payment getPayment() {
    return this.payment;
  }

  public void setPayment(final Payment payment) {
    this.payment = payment;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(final Context context) {
    this.context = context;
  }

  /*Prepara os dados da venda para a insercao*/
  public Payment getPaymentToInsert() {

    Payment payment = new Payment();
    payment.setIdClient(this.getClient().getId());
    payment.setDescription(this.getDescription());
    payment.setValue(this.getPaymentValue());
    payment.setDate(this.getPaymentDate());
    return payment;
  }

  public ConfigurationData getConfigurationDataSalved() {
    return this.configurationDataRepository.findConfigurationData();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getPaymentValue() {
    return paymentValue;
  }

  public void setPaymentValue(Double paymentValue) {
    this.paymentValue = paymentValue;
  }

  public PrinterDatecsUtil getPrinterDatecsUtil() {
    return printerDatecsUtil;
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

  public void printPayment() {
    this.getPrinterDatecsUtil().printPayment(this.getPayment());
  }

  public void closeConnection() {
    this.getPrinterDatecsUtil().closeConnection();
  }
}

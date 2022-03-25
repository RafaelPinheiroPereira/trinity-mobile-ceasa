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

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

public class PaymentViewModel extends AndroidViewModel {

  /*Objetos da tela*/
  Client client;

  Date paymentDate;

  Payment payment;

  Context context;

  /*Repositorios de acesso ao dados */
  PaymentRepository paymentRepository;
  ConfigurationDataRepository configurationDataRepository;

  public PaymentViewModel(@NonNull final Application application) {
    super(application);
    paymentRepository = new PaymentRepository(application);
    configurationDataRepository= new ConfigurationDataRepository(application);
  }

  public boolean existConfigurationData(){
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

  public Payment getPaymentByDateAndClient(){
    return  this.paymentRepository.findPaymentByDateAndClient(this.getPaymentDate(),this.getClient().getId());
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
    payment.setId(this.getPayment().getId());
    payment.setDescription(this.getPayment().getDescription());
    payment.setBaseValue(this.getPayment().getBaseValue());
    payment.setDateSale(this.getPaymentDate());
    return payment;
  }

  public ConfigurationData getConfigurationDataSalved() {
    return this.configurationDataRepository.findConfigurationData();
  }
}

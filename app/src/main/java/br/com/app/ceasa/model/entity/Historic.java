package br.com.app.ceasa.model.entity;

import java.util.Date;

public class Historic {
  private String name;
  private long idClient;
  private double value;
  private Date datePayment;
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getIdClient() {
    return idClient;
  }

  public void setIdClient(long idClient) {
    this.idClient = idClient;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public Date getDatePayment() {
    return datePayment;
  }

  public void setDatePayment(Date datePayment) {
    this.datePayment = datePayment;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Client")
public class Client implements Serializable {

  @Embedded Adress adress;

  @ColumnInfo(name = "cnpj")
  private String CNPJ;

  @ColumnInfo(name = "cpf")
  private String CPF;

  @ColumnInfo(name = "rg")
  private String RG;

  @ColumnInfo(name = "average_delay")
  private int averageDelay;

  @ColumnInfo(name = "average_purchase_value")
  private Double averagePurchaseValue;

  @ColumnInfo(name = "bank_check_amount_returned")
  private Double bankCheckAmountReturned;

  @ColumnInfo(name = "bank_check_history")
  private String bankCheckHistory;

  @ColumnInfo(name = "contact")
  private String contact;

  @ColumnInfo(name = "date_last_purchase")
  private String dateLastPurchase;

  @ColumnInfo(name = "fantasy_name")
  private String fantasyName;

  @PrimaryKey @NonNull private Long id;

  @ColumnInfo(name = "observation")
  private String observation;

  @ColumnInfo(name = "open_note")
  private int openNote;

  @ColumnInfo(name = "phone")
  private String phone;

  @ColumnInfo(name = "route_order")
  private int routeOrder;

  @ColumnInfo(name = "route_id")
  private long routeId;

  @ColumnInfo(name = "social_name")
  private String socialName;

  @ColumnInfo(name = "total_open_value")
  private Double totalOpenValue;

  @ColumnInfo(name = "value_last_purchase")
  private Double valueLastPurchase;

  public Adress getAdress() {
    return adress;
  }

  public void setAdress(final Adress adress) {
    this.adress = adress;
  }

  public int getAverageDelay() {
    return averageDelay;
  }

  public void setAverageDelay(final int averageDelay) {
    this.averageDelay = averageDelay;
  }

  public Double getAveragePurchaseValue() {
    return averagePurchaseValue;
  }

  public void setAveragePurchaseValue(final Double averagePurchaseValue) {
    this.averagePurchaseValue = averagePurchaseValue;
  }

  public Double getBankCheckAmountReturned() {
    return bankCheckAmountReturned;
  }

  public void setBankCheckAmountReturned(final Double bankCheckAmountReturned) {
    this.bankCheckAmountReturned = bankCheckAmountReturned;
  }

  public String getBankCheckHistory() {
    return bankCheckHistory;
  }

  public void setBankCheckHistory(final String bankCheckHistory) {
    this.bankCheckHistory = bankCheckHistory;
  }

  public String getCNPJ() {
    return CNPJ;
  }

  public void setCNPJ(final String CNPJ) {
    this.CNPJ = CNPJ;
  }

  public String getCPF() {
    return CPF;
  }

  public void setCPF(final String CPF) {
    this.CPF = CPF;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(final String contact) {
    this.contact = contact;
  }

  public String getDateLastPurchase() {
    return dateLastPurchase;
  }

  public void setDateLastPurchase(final String dateLastPurchase) {
    this.dateLastPurchase = dateLastPurchase;
  }

  public String getFantasyName() {
    return fantasyName;
  }

  public void setFantasyName(final String fantasyName) {
    this.fantasyName = fantasyName;
  }

  @NonNull
  public Long getId() {
    return id;
  }

  public void setId(@NonNull final Long id) {
    this.id = id;
  }

  public String getObservation() {
    return observation;
  }

  public void setObservation(final String observation) {
    this.observation = observation;
  }

  public int getOpenNote() {
    return openNote;
  }

  public void setOpenNote(final int openNote) {
    this.openNote = openNote;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }

  public String getRG() {
    return RG;
  }

  public void setRG(final String RG) {
    this.RG = RG;
  }

  public int getRouteOrder() {
    return routeOrder;
  }

  public void setRouteOrder(final int routeOrder) {
    this.routeOrder = routeOrder;
  }

  public String getSocialName() {
    return socialName;
  }

  public void setSocialName(final String socialName) {
    this.socialName = socialName;
  }

  public Double getTotalOpenValue() {
    return totalOpenValue;
  }

  public void setTotalOpenValue(final Double totalOpenValue) {
    this.totalOpenValue = totalOpenValue;
  }

  public Double getValueLastPurchase() {
    return valueLastPurchase;
  }

  public void setValueLastPurchase(final Double valueLastPurchase) {
    this.valueLastPurchase = valueLastPurchase;
  }

  public long getRouteId() {
    return routeId;
  }

  public void setRouteId(final long routeId) {
    this.routeId = routeId;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Client) {
      Client other = (Client) obj;
      return id != null && id.equals(other.id);
    }
    return false;
  }
}

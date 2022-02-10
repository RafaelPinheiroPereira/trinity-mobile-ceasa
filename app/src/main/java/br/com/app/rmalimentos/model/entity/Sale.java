package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import br.com.app.rmalimentos.model.converter.Converters;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(tableName = "Sale")
@TypeConverters(Converters.class)
public class Sale implements Serializable {

  @ColumnInfo(name = "amount")
  private double amount;

  @ColumnInfo(name = "client_id")
  private long clientId;


  @ColumnInfo(name = "date_sale")
  @TypeConverters(Converters.class)
  public Date dateSale;

  @PrimaryKey @NonNull private Long id;

  @ColumnInfo(name = "payment_description")
  private String paymentDescription;

  @ColumnInfo(name = "payment_id")
  private Long paymentId;

    @Ignore
  List<SaleItem> saleItemList;

  public double getAmount() {
    return amount;
  }

  public void setAmount(final double amount) {
    this.amount = amount;
  }

  public long getClientId() {
    return clientId;
  }

  public void setClientId(final long clientId) {
    this.clientId = clientId;
  }

    public Date getDateSale() {
    return dateSale;
  }

    public void setDateSale(final Date dateSale) {
    this.dateSale = dateSale;
  }

  @NonNull
  public Long getId() {
    return id;
  }

  public void setId(@NonNull final Long id) {
    this.id = id;
  }

  public String getPaymentDescription() {
    return paymentDescription;
  }

  public void setPaymentDescription(final String paymentDescription) {
    this.paymentDescription = paymentDescription;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(final Long paymentId) {
    this.paymentId = paymentId;
  }

  public List<SaleItem> getSaleItemList() {
    return saleItemList;
  }

  public void setSaleItemList(final List<SaleItem> saleItemList) {
    this.saleItemList = saleItemList;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof Sale) {
      Sale other = (Sale) obj;
      return id != null && id.equals(other.id);
    }
    return false;
  }


}

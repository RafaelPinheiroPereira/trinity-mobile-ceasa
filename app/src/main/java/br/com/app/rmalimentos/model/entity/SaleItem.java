package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "SaleItem")
public class SaleItem implements Serializable {

  @ColumnInfo(name = "description")
  private String description;

  @PrimaryKey @NonNull private Long id;

  @ColumnInfo(name = "product_id")
  private long productId;

  @ColumnInfo(name = "quantity")
  private int quantity;

  @ColumnInfo(name = "unity_code")
  private String unityCode;

  @ColumnInfo(name = "value")
  private Double value;

  @ColumnInfo(name = "total_value")
  private Double totalValue;

  @ColumnInfo(name = "sale_id")
  private Long saleId;

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  @NonNull
  public Long getId() {
    return id;
  }

  public void setId(@NonNull final Long id) {
    this.id = id;
  }

  public long getProductId() {
    return productId;
  }

  public void setProductId(final long productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(final int quantity) {
    this.quantity = quantity;
  }

  public String getUnityCode() {
    return unityCode;
  }

  public void setUnityCode(final String unityCode) {
    this.unityCode = unityCode;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(final Double value) {
    this.value = value;
  }

  public Double getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(final Double totalValue) {
    this.totalValue = totalValue;
  }

  public Long getSaleId() {
    return saleId;
  }

  public void setSaleId(final Long saleId) {
    this.saleId = saleId;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof SaleItem) {
      SaleItem other = (SaleItem) obj;
      return id != null && id.equals(other.id);
    }
    return false;
  }


}

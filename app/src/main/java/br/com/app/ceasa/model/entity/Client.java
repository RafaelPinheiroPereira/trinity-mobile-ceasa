package br.com.app.ceasa.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Client")
public class Client implements Serializable {

  @PrimaryKey
  @NonNull
  private Long id;

  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "order")
  private int  order;

  @Ignore
  private List<Payment> payments;


  @NonNull
  public Long getId() {
    return id;
  }

  public void setId(@NonNull Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(List<Payment> payments) {
    this.payments = payments;
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

package br.com.app.ceasa.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "PrinterDP250")
public class PrinterDP implements Serializable {
  @PrimaryKey @NonNull private Long id;

  @ColumnInfo(name = "mac")
  private String mac;

  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "atived")
  private boolean atived = false;

  @NonNull
  public Long getId() {
    return id;
  }

  public void setId(@NonNull Long id) {
    this.id = id;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAtived() {
    return atived;
  }

  public void setAtived(boolean atived) {
    this.atived = atived;
  }
}

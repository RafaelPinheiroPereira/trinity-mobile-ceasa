package br.com.app.ceasa.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import br.com.app.ceasa.model.converter.Converters;

@Entity(tableName = "Payment")
public class Payment implements Serializable {

    @PrimaryKey
    @NonNull
    private Long id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "id_client")
    private long idClient;

    @ColumnInfo(name = "date_sale")
    @TypeConverters(Converters.class)
    public Date dateSale;

    @ColumnInfo(name = "amount")
    private double amount;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public long getIdClient() {
        return idClient;
    }

    public void setIdClient(long idClient) {
        this.idClient = idClient;
    }

    public Date getDateSale() {
        return dateSale;
    }

    public void setDateSale(Date dateSale) {
        this.dateSale = dateSale;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Payment) {
            Payment other = (Payment) obj;
            return id != null && id.equals(other.id);
        }
        return false;
    }
}

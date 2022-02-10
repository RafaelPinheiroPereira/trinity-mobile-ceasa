package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Price")
public class Price implements Serializable {

    @ColumnInfo(name = "value")
    Double value;

    @PrimaryKey
    @NonNull
    private Long id;

    @ColumnInfo(name = "payment_id")
    private long paymentId;

    @ColumnInfo(name = "product_id")
    private long productId;

    @ColumnInfo(name = "unity_code")
    private String unityCode;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull final Long id) {
        this.id = id;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final long paymentId) {
        this.paymentId = paymentId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(final long productId) {
        this.productId = productId;
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
}

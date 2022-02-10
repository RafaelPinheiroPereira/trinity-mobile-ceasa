package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Product")
public class Product implements Serializable {

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "differentiated_weight")
    private String differentiatedWeight;

    @PrimaryKey
    @NonNull
    private Long id;

    @ColumnInfo(name = "promotion")
    private String promotion;

    @ColumnInfo(name = "standard_unit")
    private String standardUnit;

    @ColumnInfo(name = "stock_quantity")
    private int stockQuantity;

    @ColumnInfo(name = "system_date")
    private String systemDate;

    @ColumnInfo(name = " unit_quantity")
    private int unitQuantity;

    @ColumnInfo(name = "validity")
    private String validity;



    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getDifferentiatedWeight() {
        return differentiatedWeight;
    }

    public void setDifferentiatedWeight(final String differentiatedWeight) {
        this.differentiatedWeight = differentiatedWeight;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull final Long id) {
        this.id = id;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(final String promotion) {
        this.promotion = promotion;
    }

    public String getStandardUnit() {
        return standardUnit;
    }

    public void setStandardUnit(final String standardUnit) {
        this.standardUnit = standardUnit;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(final int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(final String systemDate) {
        this.systemDate = systemDate;
    }

    public int getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(final int unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(final String validity) {
        this.validity = validity;
    }

    @Override
    public String toString() {
        return  id + " - "+description ;
    }
}

package br.com.app.ceasa.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import br.com.app.ceasa.model.converter.Converters;

@Entity(tableName = "BaseValue")
public class BaseValue implements Serializable {
    @PrimaryKey
    @NonNull
    private Long id;

    @ColumnInfo(name = "base_date")
    @TypeConverters(Converters.class)
    public Date baseDate;

    @ColumnInfo(name = "base_value")
    private double base_value;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public Date getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
    }

    public double getBase_value() {
        return base_value;
    }

    public void setBase_value(double base_value) {
        this.base_value = base_value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof BaseValue) {
            BaseValue other = (BaseValue) obj;
            return id != null && id.equals(other.id);
        }
        return false;
    }
}

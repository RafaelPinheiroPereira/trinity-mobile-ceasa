package br.com.app.ceasa.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import br.com.app.ceasa.model.converter.Converters;

@Entity(tableName = "ConfigurationData")
public class ConfigurationData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Long id;

    @ColumnInfo(name = "base_date")
    @TypeConverters(Converters.class)
    public Date baseDate;

    @ColumnInfo(name = "base_value")
    private double baseValue;

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

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ConfigurationData) {
            ConfigurationData other = (ConfigurationData) obj;
            return id != null && id.equals(other.id);
        }
        return false;
    }
}

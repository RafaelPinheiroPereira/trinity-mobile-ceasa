package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Payment")
public class Payment implements Serializable {

    @ColumnInfo(name = "description")
    private String description;

    public Payment() {
    }

    public Payment(final String description, @NonNull final Long id) {
        this.description = description;
        this.id = id;
    }

    @PrimaryKey
    @NonNull
    private Long id;

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

    @Override
    public String toString() {
        return  description ;
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

package br.com.app.rmalimentos.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "Employee")
public class Employee implements Serializable {

    @PrimaryKey
    @NonNull
    private Long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    public Long getId() {
        return id;
    }

    @ColumnInfo(name = "atived")
    private int atived = 0;

    public void setId(@NonNull final Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getAtived() {
        return atived;
    }

    public void setAtived(final int atived) {
        this.atived = atived;
    }
}

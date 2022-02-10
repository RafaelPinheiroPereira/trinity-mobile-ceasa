package br.com.app.rmalimentos.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import java.io.Serializable;

@Dao
public abstract class GenericDAO<T extends Serializable> {

    @Delete
    public abstract void delete(T obj);
    @Delete
    public abstract void delete(T... objs);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(T... objs);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(T obj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(T... objs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(T obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(T obj);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(T... objs);
}

package br.com.app.ceasa.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import br.com.app.ceasa.model.entity.Historic;

@Dao
public interface HistoricDAO {

    @Query("SELECT payment.id_client AS idClient,client.name AS name,payment.value AS value, payment.date as datePayment " +
            "FROM client , payment  " +
            "WHERE client.id = payment.id_client and  payment.date= :datePayment")
    public LiveData<List<Historic>> findHistoricByDatePayment(Date datePayment);
}


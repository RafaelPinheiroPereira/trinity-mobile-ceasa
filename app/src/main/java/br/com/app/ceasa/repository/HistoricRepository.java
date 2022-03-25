package br.com.app.ceasa.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.HistoricDAO;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.model.entity.Payment;

public class HistoricRepository {
    private HistoricDAO historicDAO;
    private AppDataBase appDataBase;

    public HistoricRepository(Application application) {
        appDataBase = AppDataBase.getDatabase(application);
        historicDAO = appDataBase.historicDAO();
    }

    public LiveData<List<Historic>>  findHistorictByDate(final Date datePayment) {
        return this.historicDAO.findHistoricByDatePayment(datePayment);
    }
}

package br.com.app.ceasa.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.repository.ConfigurationDataRepository;
import br.com.app.ceasa.repository.HistoricRepository;

public class HistoricViewModel   extends AndroidViewModel {

    private String datePayment;

    HistoricRepository historicRepository;
    ConfigurationDataRepository configurationDataRepository;

    public HistoricViewModel(@NonNull Application application) {
        super(application);
        historicRepository= new HistoricRepository(application);
        configurationDataRepository= new ConfigurationDataRepository(application);
    }

    public LiveData<List<Historic>> getHistoricByDatePayment(final Date datePayment) throws ParseException {
        return this.historicRepository.findHistorictByDate(datePayment);
    }

    public ConfigurationData getConfigurationDataSalved() {
        return this.configurationDataRepository.findConfigurationData();
    }

    public String getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(String datePayment) {
        this.datePayment = datePayment;
    }
}

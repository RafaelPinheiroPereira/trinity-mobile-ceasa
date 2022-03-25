package br.com.app.ceasa.repository;

import android.app.Application;

import java.util.Date;
import java.util.List;

import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.ConfigurationDataDAO;
import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;

public class ConfigurationDataRepository {

    ConfigurationDataDAO configurationDataDAO;
    private AppDataBase appDataBase;

    public ConfigurationDataRepository(Application application) {
        this.appDataBase = AppDataBase.getDatabase(application);
        this.configurationDataDAO=this.appDataBase.configurationDataDAO();
    }

    public Long findLastId() {
        return this.configurationDataDAO.findLastId();
    }

    public ConfigurationData findConfigurationData() {
        return this.configurationDataDAO.findConfigurationData();
    }
    public void insertConfigurationData(final ConfigurationData configurationData) {
        this.configurationDataDAO.insert(configurationData);

    }

    public void updateConfigurationData(ConfigurationData configurationData) {
        this.configurationDataDAO.update(configurationData);
    }
}

package br.com.app.ceasa.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.Date;
import java.util.Optional;

import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.repository.ConfigurationDataRepository;

public class ConfigurationDataViewModel extends AndroidViewModel {
  ConfigurationDataRepository configurationDataRepository;

  ConfigurationData configurationData;
  Date initialDateBase;
  Double valueBase;
  Context context;

  public ConfigurationDataViewModel(@NonNull Application application) {
    super(application);

    configurationDataRepository = new ConfigurationDataRepository(application);
  }

  public void insertConfigurationData() {
    this.configurationDataRepository.insertConfigurationData(this.getConfigurationData());
  }

  public boolean existConfigurationData() {
    Optional<ConfigurationData> configurationDataOptional =
        Optional.ofNullable(this.findConfigurationData());
    return configurationDataOptional.isPresent();
  }

  public ConfigurationData getConfigurationDataSalved() {
    return this.findConfigurationData();
  }

  public ConfigurationData getConfigurationDataToInsert() {

    ConfigurationData configurationData = new ConfigurationData();
    configurationData.setBaseValue(this.getValueBase());
    configurationData.setBaseDate(this.getInitialDateBase());

    return configurationData;
  }

  public ConfigurationData findConfigurationData() {
    return this.configurationDataRepository.findConfigurationData();
  }

  public ConfigurationData getConfigurationData() {
    return configurationData;
  }

  public void setConfigurationData(ConfigurationData configurationData) {
    this.configurationData = configurationData;
  }

  public Date getInitialDateBase() {
    return initialDateBase;
  }

  public void setInitialDateBase(Date initialDateBase) {
    this.initialDateBase = initialDateBase;
  }

  public Double getValueBase() {
    return valueBase;
  }

  public void setValueBase(Double valueBase) {
    this.valueBase = valueBase;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public void updateConfigurationData() {
    this.configurationDataRepository.updateConfigurationData(this.getConfigurationData());
  }
}

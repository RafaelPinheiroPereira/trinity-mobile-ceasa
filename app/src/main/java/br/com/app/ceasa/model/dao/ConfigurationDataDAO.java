package br.com.app.ceasa.model.dao;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.Date;

import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;

@Dao
public abstract class ConfigurationDataDAO extends GenericDAO<ConfigurationData> {


  @Query(value = "SELECT * FROM configurationdata")
  public abstract ConfigurationData findConfigurationData();

  @Query(value="SELECT MAX(id) FROM configurationdata ")
  public abstract Long findLastId();

  private class OperationsAsyncTask extends AsyncTask<ConfigurationData, Void, Void> {

    ConfigurationDataDAO mAsyncTaskDao;

    OperationsAsyncTask(ConfigurationDataDAO dao) {
      this.mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(ConfigurationData... configurationData) {
      return null;
    }
  }

  private class InsertAsyncTask extends OperationsAsyncTask {

    InsertAsyncTask(ConfigurationDataDAO configurationDataDAO) {
      super(configurationDataDAO);
    }

    @Override
    protected Void doInBackground(ConfigurationData... configurationData) {
      mAsyncTaskDao.insert(configurationData[0]);
      return null;
    }
  }

  @Override
  public void save(final ConfigurationData obj) {

    new ConfigurationDataDAO.InsertAsyncTask(this).execute(obj);
  }
}

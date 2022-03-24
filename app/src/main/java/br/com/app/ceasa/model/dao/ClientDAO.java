package br.com.app.ceasa.model.dao;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import br.com.app.ceasa.model.entity.Client;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ClientDAO extends GenericDAO<Client> {
  @Query(
      value =
          "select c.* from client c,payment p where c.id=p.id_client and p.date_sale= :dateSale ORDER BY c.`order`")
  public abstract LiveData<List<Client>> findPositivedClient(final Date dateSale);

  @Query(
      value =
          "select c.* from client c where c.id not in (select c.id from client c,payment p where c.id=p.id_client  and p.date_sale= :dateSale) ORDER BY c.`order`")
  public abstract LiveData<List<Client>> findNotPositivedClient(final Date dateSale);

  @Query(value = "select * from client  ORDER BY `order`")
  public abstract LiveData<List<Client>> getAll();

  private class OperationsAsyncTask extends AsyncTask<Client, Void, Void> {

    ClientDAO mAsyncTaskDao;

    OperationsAsyncTask(ClientDAO dao) {
      this.mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(Client... clients) {
      return null;
    }
  }

  private class InsertAsyncTask extends OperationsAsyncTask {

    InsertAsyncTask(ClientDAO clientDAO) {
      super(clientDAO);
    }

    @Override
    protected Void doInBackground(Client... clients) {
      mAsyncTaskDao.insert(clients[0]);
      return null;
    }
  }

  @Override
  public void save(final Client obj) {

    new ClientDAO.InsertAsyncTask(this).execute(obj);
  }
}

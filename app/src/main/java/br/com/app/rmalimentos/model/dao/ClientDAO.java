package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Client;
import java.util.Date;
import java.util.List;

@Dao
public abstract class ClientDAO extends GenericDAO<Client> {
  @Query(
      value =
          "select c.* from client c,sale s where c.id=s.client_id and c.route_id= :routeId and s.date_sale= :dateSale ORDER BY c.route_order")
  public abstract LiveData<List<Client>> findPositivedClient(final Date dateSale, final Long routeId);

    @Query(
            value =
                    "select c.* from client c where c.id not in (select c.id from client c,sale s where c.id=s.client_id and c.route_id= :routeId and s.date_sale= :dateSale) ORDER BY c.route_order")
    public abstract LiveData<List<Client>> findNotPositivedClient(final Date dateSale, Long routeId);

  @Query(value = "select * from client  ORDER BY route_order")
  public abstract LiveData<List<Client>> getAll();

  @Query(value = "select * from client  where route_id= :routeId ORDER BY route_order")
  public abstract LiveData<List<Client>> getAllClientByRoute(Long routeId);

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

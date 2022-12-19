package br.com.app.ceasa.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.ClientDAO;
import br.com.app.ceasa.model.entity.Client;
import java.util.Date;
import java.util.List;

public class ClientRepository {

  private ClientDAO clientDAO;

  private AppDataBase appDataBase;

  public ClientRepository(Application application) {
    this.appDataBase = AppDataBase.getDatabase(application);
    this.clientDAO = this.appDataBase.clientDAO();
  }

  public LiveData<List<Client>> findNotPositived(final Date dateSale) {

    return this.clientDAO.findNotPositivedClient(dateSale);
  }

  public LiveData<List<Client>> findPositivedClient(final Date dateSale) {
    return this.clientDAO.findPositivedClient(dateSale);
  }



  public List<Client> getAll() {
    return this.clientDAO.getAll();
  }

  public void saveAll(final List<Client> clients) {

    this.clientDAO.insert(clients.toArray(new Client[clients.size()]));
  }

  public void deleteAll() {
    this.clientDAO.deleteAll();
  }
}

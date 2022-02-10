package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.ClientDAO;
import br.com.app.rmalimentos.model.entity.Client;
import java.util.Date;
import java.util.List;

public class ClientRepository {

    private ClientDAO clientDAO;

    private AppDataBase appDataBase;

    public ClientRepository(Application application) {
        this.appDataBase = AppDataBase.getDatabase(application);
        this.clientDAO=this.appDataBase.clientDAO();
    }

    public LiveData<List<Client>> findNotPositived(final Date dateSale, final Long routeId) {

        return this.clientDAO.findNotPositivedClient(dateSale,routeId);
    }

    public LiveData<List<Client>> findPositivedClient(final Date dateSale, final Long routeId) {
        return this.clientDAO.findPositivedClient(dateSale,routeId);
    }

    public LiveData<List<Client>> getAll() {

        return clientDAO.getAll();
    }

    public void saveAll(final List<Client> clients) {

        this.clientDAO.insert(clients.toArray(new Client[clients.size()]));

    }

    public  LiveData<List<Client>> getAllClientByRoute(Long routeId){
        return this.clientDAO.getAllClientByRoute(routeId);
    }
}

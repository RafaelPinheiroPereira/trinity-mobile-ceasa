package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.RouteDAO;
import br.com.app.rmalimentos.model.entity.Route;
import java.util.List;

public class RouteRepository {

  private RouteDAO routeDAO;

  private AppDataBase appDataBase;

  public RouteRepository(Application application) {
    appDataBase = AppDataBase.getDatabase(application);
    routeDAO = appDataBase.routeDAO();
  }

  public LiveData<List<Route>> getAll() {
    return routeDAO.getAll();
  }

  public void saveAll(final List<Route> routes) {

          this.routeDAO.save(routes.toArray(new Route[routes.size()]));

  }
}

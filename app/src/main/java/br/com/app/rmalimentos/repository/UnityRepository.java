package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.UnityDAO;
import br.com.app.rmalimentos.model.entity.Product;
import br.com.app.rmalimentos.model.entity.Unity;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UnityRepository {


    private UnityDAO unityDAO;

    private LiveData<List<Unity>> listLiveData;

    private AppDataBase appDataBase;

    public UnityRepository(Application application) {
        appDataBase = AppDataBase.getDatabase(application);
        unityDAO = appDataBase.unityDAO();
    }

    public List<Unity> findUnitiesByProduct(final Product product) {

        return this.unityDAO.findUnitiesByProduct(product.getId());
    }

    public LiveData<List<Unity>> getAll() throws ExecutionException, InterruptedException {

        return unityDAO.getAll();
    }

    public void saveAll(final List<Unity> unitys) {

            this.unityDAO.save(unitys.toArray(new Unity[unitys.size()]));

    }
}

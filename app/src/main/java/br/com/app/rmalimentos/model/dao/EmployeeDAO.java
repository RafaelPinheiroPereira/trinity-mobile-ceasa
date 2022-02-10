package br.com.app.rmalimentos.model.dao;

import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import br.com.app.rmalimentos.model.entity.Employee;
import java.util.List;

@Dao
public abstract class EmployeeDAO extends GenericDAO<Employee> {

    @Query("select * from employee order by id")
    public abstract LiveData<List<Employee>> getAll();

    @Query("select * from employee where id=:employeeId order by id")
    public abstract Employee findEmployeeById(Long employeeId);

    @Query("select * from employee where atived=1 order by id")
    public abstract Employee getAtivedEmployee();

    @Query("select * from employee order by id limit 1")
    public abstract Employee findSessionEmployee();

    private class OperationsAsyncTask extends AsyncTask<Employee, Void, Void> {

        EmployeeDAO mAsyncTaskDao;

        OperationsAsyncTask(EmployeeDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(EmployeeDAO employeeDAO) {
            super(employeeDAO);
        }

        @Override
        protected Void doInBackground(Employee... employees) {
            mAsyncTaskDao.insert(employees[0]);
            return null;
        }
    }

    @Override
    public void save(final Employee obj) {

        new InsertAsyncTask(this).execute(obj);

    }
}

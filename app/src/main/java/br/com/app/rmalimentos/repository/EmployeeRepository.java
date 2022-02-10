package br.com.app.rmalimentos.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.AppDataBase;
import br.com.app.rmalimentos.model.dao.EmployeeDAO;
import br.com.app.rmalimentos.model.entity.Employee;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeRepository {

  private EmployeeDAO employeeDAO;

  private LiveData<List<Employee>> listLiveData;

  private AppDataBase appDataBase;

  public EmployeeRepository(Application application) {
    appDataBase = AppDataBase.getDatabase(application);
    employeeDAO = appDataBase.employeeDAO();
  }

  public Employee findEmployeeById(final Long id) {
    return employeeDAO.findEmployeeById(id);
  }

  public LiveData<List<Employee>> getAll() throws ExecutionException, InterruptedException {

    return employeeDAO.getAll();
  }

  public Employee getEmployeeActived() {
    return this.employeeDAO.getAtivedEmployee();
  }

  public Employee findSessionEmployee() {
    return this.employeeDAO.findSessionEmployee();
  }

  public void removeUserSession() {
    Employee employee = this.findSessionEmployee();
    employee.setAtived(0);
    this.employeeDAO.update(employee);
  }

  public void save(final Employee employee) {
    employeeDAO.save(employee);
  }

  public void updateEmployee(final Employee employee) {
    employeeDAO.update(employee);
  }

  // TODO Implementar uma AsyncTask Generica de consultas
}

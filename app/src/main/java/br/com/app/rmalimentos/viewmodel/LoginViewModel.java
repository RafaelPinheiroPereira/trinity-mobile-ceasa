package br.com.app.rmalimentos.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import br.com.app.rmalimentos.model.entity.Employee;
import br.com.app.rmalimentos.repository.EmployeeRepository;
import br.com.app.rmalimentos.repository.FileManagerRepository;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.Singleton;
import java.io.IOException;
import java.util.Optional;

public class LoginViewModel extends AndroidViewModel {

    private Employee employee;

    private EmployeeRepository employeeRepository;



    FileManagerRepository fileManagerRepository;

    Context context;



    public LoginViewModel(@NonNull final Application application)
            throws InstantiationException, IllegalAccessException {

        super(application);

        employeeRepository = new EmployeeRepository(application);
        fileManagerRepository= Singleton.getInstance(FileManagerRepository.class);
    }

    public boolean employeeFileExists() {

        return fileManagerRepository.fileExists(Constants.INPUT_FILES[0]);
    }

    public boolean isExistEmployee() {
        return Optional.ofNullable(this.employeeRepository.findEmployeeById(this.getEmployee().getId())).isPresent();
    }

    public void readEmployeeFile() throws IOException, InstantiationException, IllegalAccessException {

        fileManagerRepository.readEmployeeFile();
        setEmployee(fileManagerRepository.getEmployee());
    }

    public void createAppDirectory() throws IOException {

        fileManagerRepository.createAppDirectory(this.getContext());
    }

    public Employee getEmployee() {
        return employee;
    }

    public void saveEmployee() {
        employeeRepository.save(this.getEmployee());
    }

    public void updateEmployee() {
        employeeRepository.updateEmployee(this.getEmployee());
    }

    private void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(final Context context) {
        this.context = context;
    }
}

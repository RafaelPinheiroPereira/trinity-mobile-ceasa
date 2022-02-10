package br.com.app.rmalimentos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import br.com.app.rmalimentos.repository.EmployeeRepository;
import java.util.Optional;

public class SessionManagerViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private EmployeeRepository employeeRepository;

    public SessionManagerViewModel(@NonNull final Application application)
            throws IllegalAccessException, InstantiationException {
        super(application);
        employeeRepository = new EmployeeRepository(application);
    }

    public boolean checkedLogin() {
        return Optional.ofNullable(employeeRepository.getEmployeeActived()).isPresent();

    }
}

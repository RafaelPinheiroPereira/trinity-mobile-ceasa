package br.com.app.rmalimentos.repository;

import br.com.app.rmalimentos.model.entity.Employee;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.EmployeeFile;
import br.com.app.rmalimentos.utils.Singleton;
import java.io.File;
import java.io.IOException;

public class EmployeeFileRepository  implements  IFileRepository{


    EmployeeFile employeeFile;
    Employee employee;

    public EmployeeFileRepository() throws IllegalAccessException, InstantiationException {

        employeeFile=Singleton.getInstance(EmployeeFile.class);
    }

    public boolean fileExists(final String inputFile) {
        return employeeFile.fileExists(inputFile);
    }

    public Employee getEmployee() {
        return employee;
    }



    @Override
    public void readFile() throws IOException {
        File file= employeeFile.createFile(Constants.APP_DIRECTORY, Constants.INPUT_FILES[0]);
        employeeFile.readFile(file);
        this.setEmployee(employeeFile.getEmployee());
    }

    private void setEmployee(final Employee employee) {
        this.employee = employee;
    }
}

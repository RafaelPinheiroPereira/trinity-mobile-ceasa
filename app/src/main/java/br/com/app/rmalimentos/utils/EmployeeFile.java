package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Employee;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EmployeeFile extends FileManager {

  Employee employee;

  public EmployeeFile() {}

  @Override
  public void readFile(File file) throws IOException {

    String line;
    FileInputStream fileInputStream = new FileInputStream(file);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));

    while ((line = br.readLine()) != null && !line.equals("")) {
      Employee employee = new Employee();
      employee.setId(Long.valueOf(line.substring(0, 5)));
      employee.setName(line.substring(5, 45).trim());
      employee.setPassword(line.trim().substring(45));
      this.setEmployee(employee);
    }

    br.close();
  }

  public Employee getEmployee() {
    return employee;
  }

  private void setEmployee(final Employee employee) {
    this.employee = employee;
  }
}

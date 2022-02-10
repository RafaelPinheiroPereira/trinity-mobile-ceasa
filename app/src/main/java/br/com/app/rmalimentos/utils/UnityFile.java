package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Unity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UnityFile extends FileManager {

  List<Unity> unities = new ArrayList<>();

  public UnityFile() {}

  @Override
  public void readFile(final File file)
      throws IOException, IllegalAccessException, InstantiationException {
    String line;
    FileInputStream fileInputStream = new FileInputStream(file);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));

    while ((line = br.readLine()) != null && !line.equals("")) {

      Unity unity = new Unity();

      unity.setProductId(Long.valueOf(line.substring(0, 4)));
      unity.setCode(line.substring(4, 7).trim());
      unity.setStandard(line.substring(7, 8).trim());
      unity.setMultiple(Float.parseFloat(line.substring(10, 16).replaceAll(",", ".").trim()));
      unity.setWeight(Float.parseFloat(line.substring(16).replaceAll(",", ".").trim()));
      unities.add(unity);
    }

    this.setUnities(unities);
  }

  public List<Unity> getUnities() {
    return unities;
  }

  private void setUnities(final List<Unity> unities) {
    this.unities = unities;
  }
}

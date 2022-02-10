package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public  class ProductFile extends FileManager {

  List<Product> products;



  @Override
  public void readFile(final File file)
      throws IOException, IllegalAccessException, InstantiationException {

    String line;
    FileInputStream fileInputStream = new FileInputStream(file);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));
    products = new ArrayList<>();
    while ((line = br.readLine()) != null && !line.equals("")) {
      Product product = new Product();

      product.setId(Long.valueOf(line.substring(0, 4)));
      product.setDescription(line.substring(4, 44).trim());
      product.setDifferentiatedWeight(line.substring(44, 45).trim());
      product.setValidity(line.substring(45, 55).trim());
      product.setSystemDate(line.substring(55, 65).trim());
      product.setPromotion(line.substring(65, 66).trim());

      // MILSON TEM QUE MUDAR O PADRAO NAO CONSIGO PEGAR
      String quantity = line.substring(66, 81);
      String stockQuantity =
          quantity.replaceAll("\\D", ""); // Troca tudo que não for dígito por vazio

      String unity = line.substring(66 + stockQuantity.length(), 81).trim();
      product.setUnitQuantity(Integer.parseInt(line.substring(81).trim()));

      product.setStandardUnit(unity);

      if (stockQuantity.length() > 0) {
        product.setStockQuantity(Integer.parseInt(stockQuantity));
      } else {
        product.setStockQuantity(0);
      }
      products.add(product);
    }

    this.setProducts(products);
  }

  public List<Product> getProducts() {
    return products;
  }

  private void setProducts(final List<Product> products) {
    this.products = products;
  }
}

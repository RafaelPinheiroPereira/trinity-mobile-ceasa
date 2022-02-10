package br.com.app.rmalimentos.repository;

import br.com.app.rmalimentos.model.entity.Product;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.ProductFile;
import br.com.app.rmalimentos.utils.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductFileRepository implements IFileRepository {

  ProductFile productFile;
  List<Product> products;

  public ProductFileRepository() throws IllegalAccessException, InstantiationException {

    productFile = Singleton.getInstance(ProductFile.class);
  }

  @Override
  public void readFile() throws IOException, InstantiationException, IllegalAccessException {

    File file = productFile.createFile(Constants.APP_DIRECTORY, Constants.INPUT_FILES[1]);
    productFile.readFile(file);
    this.setProducts(productFile.getProducts());
  }

  public List<Product> getProducts() {
    return products;
  }

  private void setProducts(final List<Product> products) {
    this.products = products;
  }
}

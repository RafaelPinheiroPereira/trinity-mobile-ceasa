package br.com.app.rmalimentos.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.model.entity.Payment;
import br.com.app.rmalimentos.model.entity.Price;
import br.com.app.rmalimentos.model.entity.Product;
import br.com.app.rmalimentos.model.entity.Sale;
import br.com.app.rmalimentos.model.entity.SaleItem;
import br.com.app.rmalimentos.model.entity.Unity;
import br.com.app.rmalimentos.repository.PaymentRepository;
import br.com.app.rmalimentos.repository.PriceRepository;
import br.com.app.rmalimentos.repository.ProductRepository;
import br.com.app.rmalimentos.repository.SaleItemRepository;
import br.com.app.rmalimentos.repository.SaleRepository;
import br.com.app.rmalimentos.repository.UnityRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SaleViewModel extends AndroidViewModel {

  /*Objetos da tela*/
  BigDecimal productQuantity;
  Product productSelected;
  Price priceProductSelected;
  Unity unitySelected;
  Payment paymentSelected;
  Client client;

  Date dateSale;
  Sale sale;
  List<SaleItem> saleItems;

  List<Payment> payments = new ArrayList<>();

  List<Product> products = new ArrayList<>();

  List<Unity> unities = new ArrayList<>();

  Context context;

  /*Repositorios de acesso ao dados */
  PaymentRepository paymentRepository;
  SaleRepository saleRepository;
  ProductRepository productRepository;
  UnityRepository unityRepository;
  PriceRepository priceRepository;
  SaleItemRepository saleItemRepository;

  public SaleViewModel(@NonNull final Application application) {
    super(application);
    paymentRepository = new PaymentRepository(application);
    saleRepository = new SaleRepository(application);
    productRepository = new ProductRepository(application);
    unityRepository = new UnityRepository(application);
    priceRepository = new PriceRepository(application);
    saleItemRepository = new SaleItemRepository(application);
  }

  public void deleteSaleItem(final SaleItem saleItemToRemove) {
    this.saleItemRepository.deleteItem(saleItemToRemove);
  }

  public Product findProductById(final long productId) {
    return this.productRepository.findProductById(productId);
  }

    public Sale getSaleByDateAndClient() {

    return saleRepository.findSaleByDateAndClient(this.getDateSale(), this.getClient().getId());
  }

  public List<SaleItem> getAllSaleItens() {
    return this.saleItemRepository.findSaleItemBySale(getSale().getId());
  }

  public Client getClient() {
    return client;
  }

  public Long getLastId() {
    return this.saleRepository.findLastId();
  }

    public List<Payment> getAllPaymentsType() {
    return this.paymentRepository.getAll();
  }

    public List<Product> getAllProducts() {
    return this.productRepository.getAll();
  }

  public LiveData<List<Unity>> loadAllUnities() throws ExecutionException, InterruptedException {
    return this.unityRepository.getAll();
  }

    public Price getPriceByUnitAndProduct() {
    return this.priceRepository.findPriceByUnitAndProduct(getProductSelected(), getUnitySelected());
  }

  public List<Unity> loadUnitiesByProduct(final Product product) {
    return this.unityRepository.findUnitiesByProduct(product);
  }

  public void insertSale() {
    Long lastId = this.getLastId();
    if (lastId != null) {
      this.getSale().setId(lastId + 1);
    } else {
      this.getSale().setId(1L);
    }

    this.getSale()
            .getSaleItemList()
            .forEach(saleItem->saleItem.setSaleId(this.getSale().getId()));
    this.saleItemRepository.insertItens(this.getSale().getSaleItemList());
    this.saleRepository.createSale(this.getSale());
  }

  public Sale searchSaleByDateAndClient() {

    return this.saleRepository.findSaleByDateAndClient(
            this.getDateSale(), this.getClient().getId());
  }

  public void setClient(final Client client) {
    this.client = client;
  }

  public Date getDateSale() {
    return dateSale;
  }

  public void setDateSale(final Date dateSale) {
    this.dateSale = dateSale;
  }

  public LiveData<Sale> findSaleByDate() throws ParseException {
    return saleRepository.findSaleByDate(this.getDateSale());
  }

  public BigDecimal getProductQuantity() {
    return productQuantity;
  }

  public void setProductQuantity(final BigDecimal productQuantity) {
    this.productQuantity = productQuantity;
  }

  public BigDecimal totalValueProduct() {
    return getProductQuantity().multiply(new BigDecimal(getPriceProductSelected().getValue()));
  }

  public Double getAmount() {
    return new BigDecimal(getSaleItems().stream().mapToDouble(SaleItem::getTotalValue).sum())
            .setScale(2, BigDecimal.ROUND_HALF_DOWN)
            .doubleValue();
  }

  public Product getProductSelected() {
    return productSelected;
  }

  public void setProductSelected(final Product productSelected) {
    this.productSelected = productSelected;
  }

  public Price getPriceProductSelected() {
    return priceProductSelected;
  }

  public void setPriceProductSelected(final Price priceProductSelected) {
    this.priceProductSelected = priceProductSelected;
  }

  public Unity getUnitySelected() {
    return unitySelected;
  }

  public void setUnitySelected(final Unity unitySelected) {
    this.unitySelected = unitySelected;
  }

  public List<SaleItem> getSaleItems() {
    return this.saleItems;
  }

  public void insertItem(SaleItem saleItem) {
    this.getSaleItems().add(saleItem);
  }

  public void setSaleItems(final List<SaleItem> saleItems) {
    this.saleItems = saleItems;
  }

  public void updateSale() {
    this.getSale()
            .getSaleItemList()
            .forEach(saleItem->saleItem.setSaleId(this.getSale().getId()));

    List<SaleItem> itensToInsert =
            this.getSale().getSaleItemList().stream()
                    .filter(saleItem->saleItem.getId() == null)
                    .collect(Collectors.toList());
    List<SaleItem> itensToUpdate =
            this.getSale().getSaleItemList().stream()
                    .filter(saleItem->saleItem.getId() != null)
                    .collect(Collectors.toList());
    this.saleItemRepository.insertItens(itensToInsert);
    this.saleItemRepository.updateItens(itensToUpdate);
    this.saleRepository.updateSale(this.getSale());
  }

  public Sale getSale() {
    return this.sale;
  }

  public void setSale(final Sale sale) {
    this.sale = sale;
  }

  public Payment getPaymentSelected() {
    return paymentSelected;
  }

  public void setPaymentSelected(final Payment paymentSelected) {
    this.paymentSelected = paymentSelected;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(final Context context) {
    this.context = context;
  }

  public SaleItemRepository getSaleItemRepository() {
    return saleItemRepository;
  }

  public List<Payment> getPayments() {
    return payments;
  }

  public void setPayments(final List<Payment> payments) {
    this.payments = payments;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(final List<Product> products) {
    this.products = products;
  }

  public List<Unity> getUnities() {
    return unities;
  }

  public void setUnities(final List<Unity> unities) {
    this.unities = unities;
  }

    /*Obtem o item preparado para a insercao*/
    public SaleItem getItemToInsert() {
        SaleItem saleItem = new SaleItem();
        saleItem.setProductId(this.getProductSelected().getId());
        saleItem.setUnityCode(this.getUnitySelected().getCode());
        saleItem.setDescription(this.getProductSelected().getDescription());
        saleItem.setValue(this.getPriceProductSelected().getValue());
        saleItem.setTotalValue(this.totalValueProduct().doubleValue());
        saleItem.setQuantity(this.getProductQuantity().intValue());

        return saleItem;
    }

    /*Prepara os dados da venda para a insercao*/
    public Sale getSaleToInsert() throws ParseException {

        Sale sale = new Sale();
        sale.setClientId(this.getClient().getId());
        sale.setPaymentId(this.getPaymentSelected().getId());
        sale.setPaymentDescription(this.getPaymentSelected().getDescription());
        sale.setSaleItemList(this.getSaleItems());
        sale.setAmount(this.getAmount());
        sale.setDateSale(this.getDateSale());

        return sale;
    }

    public boolean isUpdate() {
        return Optional.ofNullable(this.getSale()).isPresent();
    }

    /*Prepara os dados da venda para a alteracao*/
    public void configSaleToUpdate() {

        this.getSale().setPaymentId(this.getPaymentSelected().getId());
        this
                .getSale()
                .setPaymentDescription(this.getPaymentSelected().getDescription());
        this.getSale().setSaleItemList(this.getSaleItems());
        this.getSale().setAmount(this.getAmount());
    }

}

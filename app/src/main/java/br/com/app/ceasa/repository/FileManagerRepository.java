package br.com.app.ceasa.repository;

import android.content.Context;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.util.FileManager;
import br.com.app.ceasa.util.Singleton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class FileManagerRepository {

  ClientFileRepository clientFileRepository;
  PaymentFileRepository paymentFileRepository;

  FileManager fileManager;

  public FileManagerRepository() throws IllegalAccessException, InstantiationException {

    fileManager = Singleton.getInstance(FileManager.class);
  }

  public boolean containsInputFile(Context context) {

    return fileManager.containsInputFile(context);
  }

  public void createAppDirectory(final Context context) throws FileNotFoundException {
    this.fileManager.createAppDirectory(context);
  }

  public void downloadFiles(final Context context) throws IOException, IllegalAccessException, InstantiationException {

    clientFileRepository = Singleton.getInstance(ClientFileRepository.class);
    clientFileRepository.readFile(context);
  }

  public void uploadFile(List<Payment> payments, Context context)
      throws IllegalAccessException, InstantiationException, FileNotFoundException {
    paymentFileRepository = Singleton.getInstance(PaymentFileRepository.class);
    paymentFileRepository.writeFile(payments, context);
  }





  public List<Client> getClients() {
    return clientFileRepository.getClients();
  }
}

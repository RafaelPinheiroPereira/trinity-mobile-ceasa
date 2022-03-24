package br.com.app.ceasa.repository;

import android.content.Context;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.utils.FileManager;
import br.com.app.ceasa.utils.Singleton;

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

  public boolean containsAllFiles() {

    return fileManager.containsAllFiles();
  }

  public void createAppDirectory(final Context context) throws FileNotFoundException {
    this.fileManager.createAppDirectory(context);
  }

  public void downloadFiles() throws IOException, IllegalAccessException, InstantiationException {

    clientFileRepository = Singleton.getInstance(ClientFileRepository.class);
    readFiles();
  }

  public void uploadFile(List<Payment> payments, Context context)
      throws IllegalAccessException, InstantiationException, FileNotFoundException {
    paymentFileRepository = Singleton.getInstance(PaymentFileRepository.class);
    paymentFileRepository.writeFile(payments, context);
  }

  private void readFiles() throws IOException {
    clientFileRepository.readFile();
  }

  public boolean fileExists(final String inputFile) {
    return fileManager.fileExists(inputFile);
  }

  public StringBuilder searchInexistsFilesNames() {
    return fileManager.searchInexistsFilesNames();
  }

  public List<Client> getClients() {
    return clientFileRepository.getClients();
  }
}

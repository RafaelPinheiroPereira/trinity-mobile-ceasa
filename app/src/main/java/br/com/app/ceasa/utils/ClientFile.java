package br.com.app.ceasa.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import br.com.app.ceasa.model.entity.Client;

public class ClientFile extends FileManager {

  List<Client> clients  = new ArrayList<>();;

  @Override
  public void readFile(File file) throws IOException {

    String line;
    FileInputStream fileInputStream = new FileInputStream(file);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));

    while ((line = br.readLine()) != null && !line.equals("")) {
      Client client = new Client();
      client.setOrder(Integer.parseInt(line.substring(0, 3)));
      client.setId(Long.valueOf(line.substring(3, 12)));
      client.setName(line.substring(12).trim());

      clients.add(client);
    }

    br.close();
  }

  public List<Client> getClients() {
    return this.clients;
  }

}

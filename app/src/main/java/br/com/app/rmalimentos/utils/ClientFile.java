package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Adress;
import br.com.app.rmalimentos.model.entity.Client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ClientFile extends FileManager {

  List<Client> clients = new ArrayList<>();

  @Override
  public void readFile(final File file)
      throws IOException, IllegalAccessException, InstantiationException {
    String line;
    FileInputStream fileInputStream = new FileInputStream(file);
    BufferedReader br =
        new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));
    while ((line = br.readLine()) != null && !line.equals("")) {
      Client client = new Client();
      client.setId(Long.valueOf(line.substring(0, 5)));
      client.setFantasyName(line.substring(5, 45).trim());
      client.setSocialName(line.substring(45, 85).trim());
      client.setPhone(line.substring(196, 226).trim());
      client.setCNPJ(line.substring(226, 246).trim());
      client.setCPF(line.substring(246, 266).trim());
      client.setObservation(line.substring(356, 456).trim());
      client.setRG(line.substring(456, 486).trim());
      client.setOpenNote(Integer.parseInt(line.substring(486, 488).trim()));
      client.setBankCheckHistory(line.substring(488, 489).trim());
      client.setRouteOrder(Integer.parseInt(line.substring(532, 537).trim()));
      client.setTotalOpenValue(MonetaryFormatting.convertMonetaryValueStringToDouble(line.substring(537, 547).replace(",", ".")));

      client.setRouteId(Long.valueOf(line.substring(548, 550)));
      client.setBankCheckAmountReturned(
          Double.parseDouble(line.substring(489, 499).replace(",", ".")));
      client.setAveragePurchaseValue(MonetaryFormatting.convertMonetaryValueStringToDouble(line.substring(499, 509).replace(",", ".")));
      client.setAverageDelay(Integer.parseInt(line.substring(509, 512).trim()));
      client.setDateLastPurchase(line.substring(512, 522).trim());
      client.setValueLastPurchase(MonetaryFormatting.convertMonetaryValueStringToDouble(line.substring(522, 532).replace(",", ".")));
      client.setContact(line.substring(316, 356).trim());

      Adress adress = new Adress();
      adress.setDescription(line.substring(85, 125).trim());
      adress.setNeighborhood(line.substring(125, 155).trim());
      adress.setPostalCode(Integer.parseInt(line.substring(155, 163).trim()));
      adress.setCity(line.substring(163, 192).trim());
      adress.setLocalityCode(Integer.parseInt(line.substring(192, 196).trim()));
      adress.setReferencePoint(line.substring(266, 316).trim());

      client.setAdress(adress);

      clients.add(client);
    }
  }

  public List<Client> getClients() {
    return this.clients;
  }
}

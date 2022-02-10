package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Price;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PriceFile extends  FileManager {

    List<Price> prices=new ArrayList<>();

    public PriceFile() {
    }

    public List<Price> getPrices() {
        return prices;
    }



    private void setPrices(final List<Price> prices) {
        this.prices = prices;
    }

    @Override
    public void readFile(final File file) throws IOException {
        String line;
        FileInputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(
                in, StandardCharsets.ISO_8859_1));

        while ((line = br.readLine()) != null && !line.equals("")) {
            Price price= new Price();
            price.setProductId(Long.valueOf(line.substring(0, 4)));
            price.setUnityCode(line.substring(4, 7).trim());
            price.setPaymentId(Long.valueOf(line.substring(7, 10).trim()));
            price.setValue(MonetaryFormatting.convertMonetaryValueStringToDouble(line.substring(11)));
            prices.add(price);
        }
        this.setPrices(prices);
    }
}

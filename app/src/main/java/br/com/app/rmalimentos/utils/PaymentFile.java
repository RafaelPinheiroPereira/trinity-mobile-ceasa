package br.com.app.rmalimentos.utils;

import br.com.app.rmalimentos.model.entity.Payment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PaymentFile extends  FileManager {

    List<Payment> payments = new ArrayList<>();



    @Override
    public void readFile(final File file) throws IOException, IllegalAccessException, InstantiationException {
        String line;
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br =
                new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));

        while ((line = br.readLine()) != null && !line.equals("")) {
            Payment payment= new Payment();
            payment.setId(Long.valueOf(line.substring(0, 3)));
            payment.setDescription(line.substring(3, 42).trim());
            payments.add(payment);

        }

        this.setPayments(payments);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    private void setPayments(final List<Payment> payments) {
        this.payments = payments;
    }
}

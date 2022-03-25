package br.com.app.ceasa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import br.com.app.ceasa.model.entity.Payment;

public class PaymentFile extends FileManager {

  String TAG = this.getClass().getSimpleName();

  public PaymentFile() {}

  public void writeFile(final List<Payment> payments, final File file)
      throws FileNotFoundException {
    FileOutputStream pen = new FileOutputStream(file);

    try {
      payments.forEach(
          payment -> {
            try {
              writePaymentData(pen, payment);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });

      pen.flush();
      pen.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writePaymentData(final FileOutputStream pen, final Payment payment)
      throws IOException {

    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    String formatedDatePayment = dateFormat.format(payment.getDateSale());

    String formatedClientId = String.format("%09d", payment.getIdClient());

    String formatedValueBase = String.format("%06.2f", payment.getValue());

    String formatedPaymentId = String.format("%09d", payment.getId());

    pen.write(
        (formatedClientId
                + formatedDatePayment
                + formatedValueBase
                + formatedPaymentId
                + payment.getDescription()
                + "\r\n")
            .getBytes());
  }
}

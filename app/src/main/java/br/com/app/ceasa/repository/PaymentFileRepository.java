package br.com.app.ceasa.repository;

import android.content.Context;
import android.media.MediaScannerConnection;

import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.utils.Constants;
import br.com.app.ceasa.utils.PaymentFile;
import br.com.app.ceasa.utils.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class PaymentFileRepository {

    PaymentFile paymentFile;

    public PaymentFileRepository() throws IllegalAccessException, InstantiationException {
        paymentFile = Singleton.getInstance(PaymentFile.class);

    }

    public void writeFile(
            final List<Payment> payments, Context context) throws FileNotFoundException {
        File file = paymentFile.createFile(Constants.APP_DIRECTORY, Constants.OUTPUT_FILE);
        paymentFile.writeFile(payments, file);
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
    }


}

package br.com.app.rmalimentos.repository;

import android.content.Context;
import android.media.MediaScannerConnection;
import br.com.app.rmalimentos.model.entity.Employee;
import br.com.app.rmalimentos.model.entity.Sale;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.SaleFile;
import br.com.app.rmalimentos.utils.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class SaleFileRepository {

    SaleFile saleFile;

    public SaleFileRepository() throws IllegalAccessException, InstantiationException {
        saleFile = Singleton.getInstance(SaleFile.class);

    }

    public void writeFile(final Employee employee,
            final List<Sale> sales, Context context) throws FileNotFoundException {
        File file = saleFile.createFile(Constants.APP_DIRECTORY, Constants.OUTPUT_FILE);
        saleFile.writeFile(sales, file, employee);
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
    }


}

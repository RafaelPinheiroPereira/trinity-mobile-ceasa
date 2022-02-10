package br.com.app.rmalimentos.utils;

import android.util.Log;
import br.com.app.rmalimentos.model.entity.Employee;
import br.com.app.rmalimentos.model.entity.Sale;
import br.com.app.rmalimentos.model.entity.SaleItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

public class SaleFile extends FileManager {

    String TAG = this.getClass().getSimpleName();

    public SaleFile() {
    }

    public void writeFile(final List<Sale> sales, final File file, Employee employee)
            throws FileNotFoundException {
        FileOutputStream pen = new FileOutputStream(file);
        String formatedEmployeeId = String.format("%03d", employee.getId());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        try {
            sales.forEach(
                    sale->{
                        if (sale.getSaleItemList().size() > 0) {
                            String formatedDateSale = dateFormat.format(sale.getDateSale());

                            try {
                                writeSaleData(pen, formatedEmployeeId, sale, formatedDateSale);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sale.getSaleItemList()
                                    .forEach(
                                            saleItem->{
                                                try {
                                                    writeSaleItemData(pen, sale, formatedEmployeeId, saleItem);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                        } else {
                            Log.d("Excecao", "Vendas sem itens");
                        }

                    });

            pen.flush();
            pen.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSaleItemData(
            final FileOutputStream pen,
            final Sale sale,
            final String formatedEmployeeId,
            final SaleItem saleItem)
            throws IOException {
        pen.write(
                (formatedEmployeeId
                        + "VENDAITENS          "
                        + "VENCOD = "
                        + String.format("%08d", sale.getId())
                        + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAITENS          " + "PROCOD = " + saleItem.getProductId() + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAITENS          " + "UNDCOD = " + saleItem.getUnityCode() + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAITENS          " + "ITVQTD = " + saleItem.getQuantity() + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId
                        + "VENDAITENS          "
                        + "ITVVAL = "
                        + String.format("%06.2f", saleItem.getValue())
                        + "\r\n")
                        .getBytes());
        pen.write((formatedEmployeeId + "VENDAITENS          " + "ITVOUT = " + "0" + "\r\n").getBytes());
        pen.write((formatedEmployeeId + "VENDAITENS          " + "ITVFAT = " + "0" + "\r\n").getBytes());
    }

    private void writeSaleData(
            final FileOutputStream pen,
            final String formatedEmployeeId,
            final Sale sale,
            final String formatedDateSale)
            throws IOException {
        pen.write(
                (formatedEmployeeId
                        + "VENDAS              "
                        + "VENCOD = "
                        + String.format("%08d", sale.getId())
                        + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "VENTIP = " + "V" + "\r\n").getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "VENNOT = " + "0" + "\r\n").getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "VENDAT = " + formatedDateSale + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "FUNCOD = " + formatedEmployeeId + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId
                        + "VENDAS              "
                        + "CLICOD = "
                        + String.format("%05d", sale.getClientId())
                        + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId
                        + "VENDAS              "
                        + "TPRCOD = "
                        + String.format("%03d", sale.getPaymentId())
                        + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "VENVEN = " + formatedDateSale + "\r\n")
                        .getBytes());
        pen.write(
                (formatedEmployeeId + "VENDAS              " + "VENOBS = " + " " + "\r\n").getBytes());
    }
}

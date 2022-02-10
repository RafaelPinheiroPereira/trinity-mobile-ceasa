package br.com.app.rmalimentos.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MonetaryFormatting {
    public static String convertToDolar(Double value) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(value);
    }

    public static String convertToReal(double doubleValue) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(doubleValue);
    }



    public static Double convertMonetaryValueStringToDouble(String valorFormatado){
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols sfs = new DecimalFormatSymbols();
        sfs.setDecimalSeparator(',');
        sfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(sfs);
        double value = 0;
        try {
            value = df.parse(valorFormatado).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  value;


    }
}

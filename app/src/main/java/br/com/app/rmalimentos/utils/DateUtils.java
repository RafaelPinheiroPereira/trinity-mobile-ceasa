package br.com.app.rmalimentos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final SimpleDateFormat FORMATADOR_dd_MM_YYYY_hh_mm =
            new SimpleDateFormat("dd/MM/yyyy hh:mm");

    private static final SimpleDateFormat FORMATADOR_dd_MM_YYYY =
            new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat FORMATADOR_YYYY_MM_DD_HH_MM_SS =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    public static String convertDateToStringInFormat_dd_mm_yyyy(Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY.format(dateToFormat);
    }

    public static Date formatarDateParaddMMyyyyhhmm(Date dateToFormat)
            throws ParseException {


        String strDate = FORMATADOR_dd_MM_YYYY_hh_mm.format(dateToFormat);

        return FORMATADOR_dd_MM_YYYY_hh_mm.parse(strDate);
    }

    public static Date formatarDateParaYYYYMMDDHHMMSS(Date dateToFormat)
            throws ParseException {

        String strDate = FORMATADOR_YYYY_MM_DD_HH_MM_SS.format(dateToFormat);

        return FORMATADOR_YYYY_MM_DD_HH_MM_SS.parse(strDate);
    }

    public static String formatarParaYYYYMMDDHHMMSS(Date dateToFormat)
            {



        return FORMATADOR_YYYY_MM_DD_HH_MM_SS.format(dateToFormat);
    }

    public static String formatarDateddMMyyyyhhmmParaString(Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY_hh_mm.format(dateToFormat);
    }

    public static String retirarBarrasDaDataNoPadraoddMMyyyyParaString(
            Date dateToFormat) {

        return FORMATADOR_dd_MM_YYYY.format(dateToFormat).replace("/", "");
    }
    public static Date converterStringParaDate(
            String dataSTR) throws ParseException {

        return FORMATADOR_dd_MM_YYYY.parse(dataSTR);
    }

    public static boolean isValidPeriod(Date dataInicial, Date dataFinal) {
        return dataInicial.before(dataFinal) || dataInicial.equals(dataFinal);
    }
}

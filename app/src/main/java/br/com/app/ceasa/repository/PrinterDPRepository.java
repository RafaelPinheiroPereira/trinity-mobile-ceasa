package br.com.app.ceasa.repository;

import android.app.Application;

import br.com.app.ceasa.AppDataBase;
import br.com.app.ceasa.model.dao.PrinterDPDAO;

public class PrinterDPRepository {
    private PrinterDPDAO printerDPDAO;
    private AppDataBase  appDataBase;

    public PrinterDPRepository(Application application) {
        appDataBase = AppDataBase.getDatabase(application);
        printerDPDAO = appDataBase.printerDPDAO();
    }
}

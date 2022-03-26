package br.com.app.ceasa.model.dao;

import android.os.AsyncTask;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.model.entity.PrinterDP;

@Dao
public abstract class PrinterDPDAO extends GenericDAO<PrinterDP> {
  @Query("select * from printerdp where printerdp.atived=1 order by id")
  public abstract PrinterDP findActivedPrinter();

  @Query("select * from printerdp where printerdp.mac=:mac order by id")
  public abstract PrinterDP searchPrinterByMac(String mac);

  private class OperationsAsyncTask extends AsyncTask<PrinterDP, Void, Void> {

    PrinterDPDAO mAsyncTaskDao;

    OperationsAsyncTask(PrinterDPDAO dao) {
      this.mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(PrinterDP... PrinterDP) {
      return null;
    }
  }

  private class InsertAsyncTask extends PrinterDPDAO.OperationsAsyncTask {

    InsertAsyncTask(PrinterDPDAO PrinterDPDAO) {
      super(PrinterDPDAO);
    }

    @Override
    protected Void doInBackground(PrinterDP... PrinterDP) {
      mAsyncTaskDao.insert(PrinterDP[0]);
      return null;
    }
  }

  @Override
  public void save(final PrinterDP obj) {

    new PrinterDPDAO.InsertAsyncTask(this).execute(obj);
  }
}

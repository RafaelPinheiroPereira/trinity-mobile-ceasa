package br.com.app.ceasa.model.dao;

import android.os.AsyncTask;

import br.com.app.ceasa.model.entity.ConfigurationData;
import br.com.app.ceasa.model.entity.PrinterDP;

public abstract class PrinterDPDAO extends GenericDAO<PrinterDP> {
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

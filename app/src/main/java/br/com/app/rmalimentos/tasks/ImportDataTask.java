package br.com.app.rmalimentos.tasks;

import android.os.AsyncTask;
import android.widget.Toast;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.model.entity.Payment;
import br.com.app.rmalimentos.model.entity.Price;
import br.com.app.rmalimentos.model.entity.Product;
import br.com.app.rmalimentos.model.entity.Route;
import br.com.app.rmalimentos.model.entity.Unity;
import br.com.app.rmalimentos.viewmodel.HomeViewModel;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ImportDataTask extends AsyncTask<Void, Void, Boolean> {

  HomeViewModel homeViewModel;

  public ImportDataTask(final HomeViewModel homeViewModel) {
    this.homeViewModel = homeViewModel;
  }

  @Override
  protected Boolean doInBackground(final Void... voids) {
    try {
      this.homeViewModel.getFileManagerRepository().downloadFiles();
      this.homeViewModel.saveData();

      return true;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }

    return false;
  }

  @Override
  protected void onProgressUpdate(final Void... values) {
    super.onProgressUpdate(values);
  }

  @Override
  protected void onPostExecute(final Boolean importou) {
    super.onPostExecute(importou);


    if (importou) {

      Toast.makeText(
              this.homeViewModel.getContext(),
              "Importação realizada com sucesso!",
              Toast.LENGTH_LONG)
          .show();
    } else {

      Toast.makeText(
              this.homeViewModel.getContext(), "Erro ao realizar importação!", Toast.LENGTH_LONG)
          .show();
    }
      this.homeViewModel.getProgressDialog().dismiss();
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    this.homeViewModel.getProgressDialog().setTitle("Sincronização de dados");
    this.homeViewModel.getProgressDialog().setCancelable(false);
    this.homeViewModel
        .getProgressDialog()
        .setMessage("Realizando a importação dos dados, Por favor aguarde...");
    this.homeViewModel.getProgressDialog().show();
  }
}

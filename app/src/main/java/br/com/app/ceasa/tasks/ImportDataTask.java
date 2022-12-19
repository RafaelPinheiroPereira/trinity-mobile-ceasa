package br.com.app.ceasa.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import br.com.app.ceasa.viewmodel.HomeViewModel;
import java.io.IOException;

public class ImportDataTask extends AsyncTask<Void, Void, Boolean> {

  HomeViewModel homeViewModel;

  public ImportDataTask(final HomeViewModel homeViewModel) {
    this.homeViewModel = homeViewModel;
  }

  @Override
  protected Boolean doInBackground(final Void... voids) {
    try {
      if(!this.homeViewModel.getClientsAll().isEmpty()){
          this.homeViewModel.deleteClients();
      }
      this.homeViewModel.getFileManagerRepository().downloadFiles(this.homeViewModel.getContext());
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

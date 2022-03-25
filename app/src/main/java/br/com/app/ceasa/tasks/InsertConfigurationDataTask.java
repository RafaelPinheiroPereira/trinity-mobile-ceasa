package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.app.ceasa.ui.ConfigurationDataActivity;
import br.com.app.ceasa.viewmodel.ConfigurationDataViewModel;

public class InsertConfigurationDataTask extends AsyncTask<Void, Void, Boolean> {
  ConfigurationDataViewModel configurationDataViewModel;
  ProgressDialog progressDialog;
  ConfigurationDataActivity configurationDataActivity;

  public InsertConfigurationDataTask(
      final ConfigurationDataViewModel configurationDataViewModel,
      final ConfigurationDataActivity configurationDataActivity) {
    this.configurationDataViewModel = configurationDataViewModel;
    this.configurationDataActivity = configurationDataActivity;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progressDialog = new ProgressDialog(this.configurationDataViewModel.getContext());
    progressDialog.setTitle("Salvar Configuração");
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Salvando configuração, Por favor aguarde...");
    progressDialog.show();
  }

  @Override
  protected Boolean doInBackground(Void... voids) {
    this.configurationDataViewModel.insertConfigurationData();
    return true;
  }

  @Override
  protected void onPostExecute(final Boolean salvou) {
    super.onPostExecute(salvou);
    this.progressDialog.dismiss();

    if (salvou) {

      Toast.makeText(
              this.configurationDataViewModel.getContext(),
              "Configuração salva com sucesso!",
              Toast.LENGTH_LONG)
          .show();
      this.configurationDataActivity.finish();

    } else {

      Toast.makeText(
              this.configurationDataViewModel.getContext(),
              "Erro ao salvar configuração!",
              Toast.LENGTH_LONG)
          .show();
    }
  }
}

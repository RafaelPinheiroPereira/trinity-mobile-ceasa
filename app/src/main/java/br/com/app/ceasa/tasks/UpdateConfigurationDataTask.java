package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.app.ceasa.ui.ConfigurationDataActivity;
import br.com.app.ceasa.viewmodel.ConfigurationDataViewModel;

public class UpdateConfigurationDataTask extends AsyncTask<Void, Void, Boolean> {
  ConfigurationDataViewModel configurationDataViewModel;
  ProgressDialog progressDialog;
  ConfigurationDataActivity configurationDataActivity;

  public UpdateConfigurationDataTask(
      final ConfigurationDataViewModel configurationDataViewModel,
      final ConfigurationDataActivity configurationDataActivity) {
    this.configurationDataViewModel = configurationDataViewModel;
    this.configurationDataActivity = configurationDataActivity;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progressDialog = new ProgressDialog(this.configurationDataViewModel.getContext());
    progressDialog.setTitle("Alterar Configuração");
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Alterando configuração, Por favor aguarde...");
    progressDialog.show();
  }

  @Override
  protected Boolean doInBackground(Void... voids) {
    this.configurationDataViewModel.updateConfigurationData();

    return true;
  }

  @Override
  protected void onPostExecute(final Boolean salvou) {
    super.onPostExecute(salvou);
    this.progressDialog.dismiss();

    if (salvou) {

      Toast.makeText(
              this.configurationDataViewModel.getContext(),
              "Configuração alterada com sucesso!",
              Toast.LENGTH_LONG)
          .show();
      this.configurationDataActivity.finish();

    } else {

      Toast.makeText(
              this.configurationDataViewModel.getContext(),
              "Erro ao alterar configuração!",
              Toast.LENGTH_LONG)
          .show();
    }
  }
}

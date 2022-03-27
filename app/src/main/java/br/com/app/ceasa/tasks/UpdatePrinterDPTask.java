package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.app.ceasa.ui.PrinterActivity;
import br.com.app.ceasa.viewmodel.PrinterDPViewModel;

public class UpdatePrinterDPTask extends AsyncTask<Void, Void, Boolean> {
  PrinterDPViewModel printerDPViewModel;
  ProgressDialog progressDialog;
  PrinterActivity printerActivity;

  public UpdatePrinterDPTask(
      final PrinterDPViewModel printerDPViewModel, final PrinterActivity printerActivity) {
    this.printerDPViewModel = printerDPViewModel;
    this.printerActivity = printerActivity;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progressDialog = new ProgressDialog(this.printerDPViewModel.getContext());
    progressDialog.setTitle("Atualizar Impressora");
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Atualizando impressora, Por favor aguarde...");
    progressDialog.show();
  }

  @Override
  protected Boolean doInBackground(Void... voids) {
    this.printerDPViewModel.updatePrinter(this.printerDPViewModel.getPrinterDP());
    return true;
  }

  @Override
  protected void onPostExecute(final Boolean salvou) {
    super.onPostExecute(salvou);
    this.progressDialog.dismiss();

    if (salvou) {

      Toast.makeText(
              this.printerDPViewModel.getContext(),
              "Impressora alterada com sucesso!",
              Toast.LENGTH_LONG)
          .show();
      this.printerActivity.finish();

    } else {

      Toast.makeText(
              this.printerDPViewModel.getContext(), "Erro ao alterar impressora!", Toast.LENGTH_LONG)
          .show();
    }
  }
}

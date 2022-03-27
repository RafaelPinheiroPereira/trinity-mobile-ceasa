package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.app.ceasa.ui.PrinterActivity;
import br.com.app.ceasa.viewmodel.PrinterDPViewModel;

public class InsertPrinterDPTask extends AsyncTask<Void, Void, Boolean> {
    PrinterDPViewModel printerDPViewModel;
    ProgressDialog progressDialog;
    PrinterActivity printerActivity;

    public InsertPrinterDPTask(
            final PrinterDPViewModel printerDPViewModel,
            final PrinterActivity printerActivity) {
        this.printerDPViewModel = printerDPViewModel;
        this.printerActivity = printerActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.printerDPViewModel.getContext());
        progressDialog.setTitle("Salvar Impressora");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Salvando impressora, Por favor aguarde...");
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
       this.printerDPViewModel.insertPrinter();
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean salvou) {
        super.onPostExecute(salvou);
        this.progressDialog.dismiss();

        if (salvou) {

            Toast.makeText(
                    this.printerDPViewModel.getContext(),
                    "Impressora salva com sucesso!",
                    Toast.LENGTH_LONG)
                    .show();
            this.printerActivity.finish();

        } else {

            Toast.makeText(
                    this.printerDPViewModel.getContext(),
                    "Erro ao salvar impressora!",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}

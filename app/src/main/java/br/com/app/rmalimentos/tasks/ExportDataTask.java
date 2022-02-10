package br.com.app.rmalimentos.tasks;

import android.os.AsyncTask;
import android.widget.Toast;
import br.com.app.rmalimentos.viewmodel.ExportViewModel;
import java.io.FileNotFoundException;

public class ExportDataTask extends AsyncTask<Void, Void, Boolean> {

    ExportViewModel exportViewModel;

    public ExportDataTask(final ExportViewModel exportViewModel) {
        this.exportViewModel = exportViewModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.exportViewModel.getProgressDialog().setTitle("Sincronização de dados");
        this.exportViewModel.getProgressDialog().setCancelable(false);
        this.exportViewModel
                .getProgressDialog()
                .setMessage("Realizando a exportação dos dados, Por favor aguarde...");
        this.exportViewModel.getProgressDialog().show();
    }

    @Override
    protected Boolean doInBackground(final Void... voids) {
        try {
            this.exportViewModel.getFileManagerRepository()
                    .uploadFile(this.exportViewModel.getEmployee(), this.exportViewModel.getSales(),
                            this.exportViewModel.getContext());
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean importou) {
        super.onPostExecute(importou);

        if (importou) {

            Toast.makeText(
                    this.exportViewModel.getContext(),
                    "Exportação realizada com sucesso!",
                    Toast.LENGTH_LONG)
                    .show();
        } else {

            Toast.makeText(
                    this.exportViewModel.getContext(), "Erro ao realizar exportação!", Toast.LENGTH_LONG)
                    .show();
        }
        this.exportViewModel.getProgressDialog().dismiss();
    }

}

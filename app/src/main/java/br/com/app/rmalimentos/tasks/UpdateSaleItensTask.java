package br.com.app.rmalimentos.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import br.com.app.rmalimentos.view.SaleActivity;
import br.com.app.rmalimentos.viewmodel.SaleViewModel;

public class UpdateSaleItensTask extends AsyncTask<Void, Void, Boolean> {


    SaleViewModel saleViewModel;
    ProgressDialog progressDialog;
    SaleActivity saleActivity;

    public UpdateSaleItensTask(final SaleViewModel saleViewModel,final SaleActivity saleActivity) {
        this.saleViewModel = saleViewModel;
        this.saleActivity=saleActivity;
    }



    @Override
    protected Boolean doInBackground(final Void... voids) {


        this.saleViewModel.updateSale();

        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= new ProgressDialog(this.saleViewModel.getContext());
        progressDialog.setTitle("Salvar Itens");
        progressDialog.setCancelable(false);
        progressDialog
                .setMessage("Salvando os itens, Por favor aguarde...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean salvou) {
        super.onPostExecute(salvou);
        this.progressDialog.dismiss();

        if (salvou) {

            Toast.makeText(
                    this.saleViewModel.getContext(),
                    "Venda atualizada com sucesso!",
                    Toast.LENGTH_LONG)
                    .show();
            this.saleActivity.finish();

        } else {

            Toast.makeText(
                    this.saleViewModel.getContext(), "Erro ao atualizar venda!", Toast.LENGTH_LONG)
                    .show();
        }


    }


}

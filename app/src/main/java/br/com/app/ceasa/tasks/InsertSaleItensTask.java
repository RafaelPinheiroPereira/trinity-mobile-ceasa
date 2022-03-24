package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import br.com.app.ceasa.view.PaymentActivity;
import br.com.app.ceasa.viewmodel.SaleViewModel;

public class InsertSaleItensTask extends AsyncTask<Void, Void, Boolean> {


    SaleViewModel saleViewModel;
    ProgressDialog progressDialog;
    PaymentActivity paymentActivity;

    public InsertSaleItensTask(final SaleViewModel saleViewModel,final PaymentActivity paymentActivity) {
        this.saleViewModel = saleViewModel;
        this.paymentActivity = paymentActivity;
    }



    @Override
    protected Boolean doInBackground(final Void... voids) {


        this.saleViewModel.insertSale();

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
                    "Venda salva com sucesso!",
                    Toast.LENGTH_LONG)
                    .show();
            this.paymentActivity.finish();

        } else {

            Toast.makeText(
                    this.saleViewModel.getContext(), "Erro ao salvar venda!", Toast.LENGTH_LONG)
                    .show();
        }


    }

}

package br.com.app.ceasa.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import br.com.app.ceasa.view.PaymentActivity;
import br.com.app.ceasa.viewmodel.PaymentViewModel;

public class InsertPaymentTask extends AsyncTask<Void, Void, Boolean> {


    PaymentViewModel paymentViewModel;
    ProgressDialog progressDialog;
    PaymentActivity paymentActivity;

    public InsertPaymentTask(final PaymentViewModel paymentViewModel, final PaymentActivity paymentActivity) {
        this.paymentViewModel = paymentViewModel;
        this.paymentActivity = paymentActivity;
    }



    @Override
    protected Boolean doInBackground(final Void... voids) {


        this.paymentViewModel.insertPayment();

        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog= new ProgressDialog(this.paymentViewModel.getContext());
        progressDialog.setTitle("Salvar Recebimento");
        progressDialog.setCancelable(false);
        progressDialog
                .setMessage("Salvando o recebimento, Por favor aguarde...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean salvou) {
        super.onPostExecute(salvou);
        this.progressDialog.dismiss();

        if (salvou) {

            Toast.makeText(
                    this.paymentViewModel.getContext(),
                    "Recebimento salvo com sucesso!",
                    Toast.LENGTH_LONG)
                    .show();
            this.paymentActivity.finish();

        } else {

            Toast.makeText(
                    this.paymentViewModel.getContext(), "Erro ao salvar recebimento!", Toast.LENGTH_LONG)
                    .show();
        }


    }

}

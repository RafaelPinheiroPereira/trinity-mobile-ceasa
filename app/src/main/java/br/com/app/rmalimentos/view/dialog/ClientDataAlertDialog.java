package br.com.app.rmalimentos.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.utils.MonetaryFormatting;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClientDataAlertDialog extends DialogFragment {

  private Unbinder unbinder;

  @BindView(R.id.txt_client_id)
  TextView txtClienteId;

  @BindView(R.id.txt_social_name)
  TextView txtSocialName;

  @BindView(R.id.txt_fantasy_name)
  TextView txtFantasyName;

  @BindView(R.id.txt_adress)
  TextView txtAdress;

  @BindView(R.id.txt_cpf_cnpj)
  TextView txtCNPJCPF;

  @BindView(R.id.txt_observation)
  TextView txtObservation;

  @BindView(R.id.txt_rg)
  TextView txtRG;

  @BindView(R.id.txt_phone)
  TextView txtPhone;

  @BindView(R.id.txt_contact)
  TextView txtContact;

  @BindView(R.id.txt_average_purchase_value)
  TextView txtAveragePurchaseValue;

  @BindView(R.id.txt_open_note)
  TextView txtOpenNote;

  Client client;
  Context ctx;

  public ClientDataAlertDialog(final Client client, Context ctx) {
    this.client = client;
    this.ctx=ctx;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_client_details, container, false);
    return v;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
    LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_client_details, null);
    ButterKnife.bind(this, dialogView);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setTitle("Dados do cliente");
    return dialogBuilder.create();
  }

  private void initView() {
    this.txtFantasyName.setText("Nome Fantasia: "+this.client.getFantasyName());
    this.txtSocialName.setText("Razão Social: "+this.client.getSocialName());
    this.txtClienteId.setText("Código: "+String.format("%05d", this.client.getId()));
    this.txtRG.setText("RG: "+this.client.getRG());
    this.txtCNPJCPF.setText("CPF/CNPJ: "+this.client.getCNPJ());
    this.txtContact.setText("Contato: "+this.client.getContact());
    this.txtObservation.setText("Observação: "+this.client.getObservation());
    this.txtPhone.setText("Telefone: "+this.client.getPhone());
    this.txtAdress.setText("Endereço: "+this.client.getAdress().getDescription()+", "+this.client.getAdress().getNeighborhood());
    this.txtOpenNote.setText("Notas Abertas: "+ this.client.getOpenNote());
    this.txtAveragePurchaseValue.setText("Valor Médio Compras: "+ MonetaryFormatting.convertToReal(this.client.getAveragePurchaseValue()));
  }

  @Override
  public void onStart() {
    super.onStart();
    unbinder = ButterKnife.bind(this, getDialog());
    initView();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}

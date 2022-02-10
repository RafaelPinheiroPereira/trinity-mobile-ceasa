package br.com.app.rmalimentos.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.model.entity.Product;
import br.com.app.rmalimentos.model.entity.SaleItem;
import br.com.app.rmalimentos.model.entity.Unity;
import br.com.app.rmalimentos.utils.CurrencyEditText;
import br.com.app.rmalimentos.utils.MonetaryFormatting;
import br.com.app.rmalimentos.view.adapter.SaleItemAdapter;
import br.com.app.rmalimentos.viewmodel.SaleViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;
import java.util.List;

public class EditSaleItemDialog extends DialogFragment {
  private Unbinder unbinder;

  @BindView(R.id.txt_product_dialog)
  TextView txtProduct;

  @BindView(R.id.spn_unity_dialog)
  Spinner spnUnity;

  @BindView(R.id.edt_quantity_dialog)
  EditText edtQuantity;

  @BindView(R.id.cet_price_dialog)
  CurrencyEditText cetPrice;

  @BindView(R.id.btn_cancel_dialog)
  Button btnCancel;

  @BindView(R.id.btn_save_dialog)
  Button btnSave;

  SaleViewModel saleViewModel;

  TextView txtAmount;
  int position;
  ArrayAdapter unitiesAdapter;
  SaleItem salemItemToUpdate;
  SaleItemAdapter saleItemAdapter;

  public EditSaleItemDialog(final SaleViewModel saleViewModel, final int position, SaleItemAdapter saleItemAdapter,
          final TextView txtViewAmount) {
    this.saleViewModel = saleViewModel;
    this.position = position;
    salemItemToUpdate = this.saleViewModel.getSaleItems().get(this.position);
    this.saleItemAdapter=saleItemAdapter;
    this.txtAmount = txtViewAmount;
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.saleViewModel.getContext());
    LayoutInflater inflater = ((Activity) this.saleViewModel.getContext()).getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.dialog_edit_sale_item, null);
    ButterKnife.bind(this, dialogView);
    dialogBuilder.setView(dialogView);
    dialogBuilder.setTitle("Editar Item");
    return dialogBuilder.create();
  }

  @Override
  public void onStart() {
    super.onStart();
    unbinder = ButterKnife.bind(this, getDialog());
    initView();
    setAdapters();
  }

  private void setAdapters() {

    List<Unity> unities = saleViewModel
            .loadUnitiesByProduct(this.saleViewModel.getProductSelected());
    this.saleViewModel.setUnities(unities);

              unitiesAdapter =
                  new ArrayAdapter(
                      this.saleViewModel.getContext(),
                      android.R.layout.simple_list_item_1,
                          this.saleViewModel.getUnities());
              spnUnity.setAdapter(unitiesAdapter);
              spnUnity.setSelection(
                  unitiesAdapter.getPosition(this.salemItemToUpdate.getUnityCode()));

  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void initView() {

    Product product = this.saleViewModel.findProductById(salemItemToUpdate.getProductId());
    this.saleViewModel.setProductSelected(product);
    txtProduct.setText(String.format("%05d", product.getId()) + " " + product.getDescription());
    edtQuantity.setText(String.valueOf(salemItemToUpdate.getQuantity()));
    cetPrice.setText(MonetaryFormatting.convertToDolar(salemItemToUpdate.getValue()));
  }

    @OnClick(R.id.btn_save_dialog)
    public void onBtnSaveDialogClicked(View view) {

        salemItemToUpdate.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
        salemItemToUpdate.setValue(cetPrice.getCurrencyDouble());
        salemItemToUpdate.setTotalValue(salemItemToUpdate.getQuantity()*salemItemToUpdate.getValue());
        this.saleViewModel.getSaleItems().set(position,salemItemToUpdate);
      txtAmount.setText(MonetaryFormatting.convertToReal(saleViewModel.getAmount()));


        this.saleItemAdapter.notifyDataSetChanged();

        dismiss();
    }
    @OnClick(R.id.btn_cancel_dialog)
    public void onBtnCancelDialogClicked(View view) {
        dismiss();
    }

  @OnItemSelected(R.id.spn_unity_dialog)
  public void setSpnUnityOnSelected(int position) {
    Unity unitySelected= (Unity) unitiesAdapter.getItem(position);
    this.salemItemToUpdate
        .setUnityCode(unitySelected.getCode());
    }
}

package br.com.app.rmalimentos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.model.entity.SaleItem;
import br.com.app.rmalimentos.utils.MonetaryFormatting;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;

public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.MyViewHolder> {

  private LayoutInflater mLayoutInflater;

  private List<SaleItem> saleItems;

  public SaleItemAdapter(Context ctx, List<SaleItem> saleItems) {
    this.saleItems = saleItems;
    this.mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public void onBindViewHolder(
      @NonNull final SaleItemAdapter.MyViewHolder holder, final int position) {

      holder.txtProductDescription
              .setText(String.format("%05d ", this.saleItems.get(position).getProductId()) +
                      this.saleItems.get(position).getDescription());
    holder.txtUnity.setText(
            this.saleItems.get(position).getQuantity()
            + " x "
                    + this.saleItems.get(position).getUnityCode());
    holder.txtProductValue.setText(
            this.saleItems.get(position).getQuantity()
            + " x "
            + MonetaryFormatting.convertToReal(
                    this.saleItems.get(position).getValue()));
    holder.txtTotalValue.setText(
        "TOTAL: "
            + MonetaryFormatting.convertToReal(
                this.saleItems.get(position).getTotalValue()));

    holder.txtPosition.setText((position+1) + "/" + (this.getItemCount() ));
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override
  public int getItemCount() {
    return this.saleItems.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_product_description)
    public TextView txtProductDescription;

    @BindView(R.id.txt_product_value)
    public TextView txtProductValue;

    @BindView(R.id.txt_unity)
    public TextView txtUnity;

    @BindView(R.id.txt_total_value)
    public TextView txtTotalValue;
      @BindView(R.id.txt_position)
      public TextView txtPosition;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  @NonNull
  @Override
  public SaleItemAdapter.MyViewHolder onCreateViewHolder(
      @NonNull final ViewGroup parent, final int viewType) {


    View v = mLayoutInflater.inflate(R.layout.item_sale_item, parent, false);
    SaleItemAdapter.MyViewHolder mvh = new SaleItemAdapter.MyViewHolder(v);
    return mvh;
  }
}

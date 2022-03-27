package br.com.app.ceasa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.app.ceasa.R;
import br.com.app.ceasa.listener.RecyclerViewOnClickListenerHack;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.util.MonetaryFormatting;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricAdapter extends RecyclerView.Adapter<HistoricAdapter.MyViewHolder> {
  private LayoutInflater mLayoutInflater;
  private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
  private List<Historic> historics = new ArrayList<>();

  public HistoricAdapter(Context ctx, List<Historic> historics) {
    this.mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.historics = historics;
  }

  @NonNull
  @Override
  public HistoricAdapter.MyViewHolder onCreateViewHolder(
      @NonNull final ViewGroup parent, final int viewType) {
    View v = null;
    if (this.historics.size() == 0) {
      v = mLayoutInflater.inflate(R.layout.empty_sate, parent, false);
    } else {
      v = mLayoutInflater.inflate(R.layout.item_recycle_historic, parent, false);
    }
    HistoricAdapter.MyViewHolder mvh = new HistoricAdapter.MyViewHolder(v);
    return mvh;
  }

  @Override
  public void onBindViewHolder(
      @NonNull final HistoricAdapter.MyViewHolder holder, final int position) {

    holder.txtClientId.setText(String.format("%05d", historics.get(position).getIdClient()));
    holder.txtName.setText(historics.get(position).getName());
    holder.txtValue.setText(MonetaryFormatting.convertToReal(historics.get(position).getValue()));
  }

  public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.txt_fantasy_name)
    public TextView txtName;

    @BindView(R.id.txt_client_id)
    public TextView txtClientId;

    @BindView(R.id.txt_value)
    public TextView txtValue;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(final View v) {
      if (recyclerViewOnClickListenerHack != null) {
        recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
      }
    }
  }

  public Historic getItem(int position) {
    return this.historics.get(position);
  }

  @Override
  public int getItemCount() {
    return this.historics.size();
  }
}

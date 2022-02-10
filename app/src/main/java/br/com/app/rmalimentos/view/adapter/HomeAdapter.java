package br.com.app.rmalimentos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.app.rmalimentos.R;
import br.com.app.rmalimentos.listener.RecyclerViewOnClickListenerHack;
import br.com.app.rmalimentos.model.entity.Client;
import br.com.app.rmalimentos.view.adapter.HomeAdapter.MyViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import java.util.stream.Collectors;

public class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

  private LayoutInflater mLayoutInflater;

    private List<Client> clientOriginalList;

    private List<Client> clientsFiltereds;
  private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

  public HomeAdapter(Context ctx, List<Client> clients) {
    this.mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.clientOriginalList = clients;
      this.clientsFiltereds = clients;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
    View v = mLayoutInflater.inflate(R.layout.item_recycle_home, parent, false);
    MyViewHolder mvh = new MyViewHolder(v);
    return mvh;
  }

  @Override
  public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

      holder.txtClientId.setText(String.format("%05d", clientsFiltereds.get(position).getId()));
      holder.txtNameFantasy.setText(clientsFiltereds.get(position).getFantasyName());
    String strAdress =
            clientsFiltereds.get(position).getAdress().toString() != null
                    ? clientsFiltereds.get(position).getAdress().getDescription()
                + ", "
                    + clientsFiltereds.get(position).getAdress().getNeighborhood()
            : "";
    holder.txtAdress.setText(strAdress);
  }

  public Client getItem(int position) {
      return this.clientsFiltereds.get(position);
  }

  @Override
  public int getItemCount() {
      return this.clientsFiltereds.size();
  }

  @Override
  public Filter getFilter() {
      return new Filter() {
          @Override
          protected FilterResults performFiltering(final CharSequence constraint) {
              FilterResults filterResults = new FilterResults();
              if (constraint.toString().isEmpty()) {
                  filterResults.values = clientOriginalList;
              } else {
                  filterResults.values = clientOriginalList.stream()
                          .filter(
                                  client->
                                          client
                                                  .getFantasyName()
                                                  .toLowerCase()
                                                  .contains(constraint.toString().toLowerCase())
                                                  || String.valueOf(client.getId())
                                                  .contains(constraint.toString())).collect(Collectors.toList());
              }

              return filterResults;

          }

          @Override
          protected void publishResults(final CharSequence constraint, final FilterResults results) {

              clientsFiltereds = (List<Client>) results.values;

              notifyDataSetChanged();
          }
      };
  }

  public class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    @BindView(R.id.txt_adress)
    public TextView txtAdress;

    @BindView(R.id.txt_fantasy_name)
    public TextView txtNameFantasy;

    @BindView(R.id.txt_client_id)
    public TextView txtClientId;

    @BindView(R.id.img_info)
    Button imgInfo;

    @BindView(R.id.btn_sale)
    Button btnSale;

    public MyViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      imgInfo.setOnClickListener(this);
      btnSale.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
      if (recyclerViewOnClickListenerHack != null) {
        recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
      }
    }
  }

  public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
    recyclerViewOnClickListenerHack = r;
  }
}

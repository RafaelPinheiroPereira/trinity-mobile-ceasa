package br.com.app.ceasa.ui.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Home;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientViewHolder extends ParentViewHolder {

    @BindView(R.id.txt_fantasy_name)
    public TextView txtName;

    @BindView(R.id.txt_client_id)
    public TextView txtClientId;


    @BindView(R.id.contentContainerRl)
    LinearLayout linearLayout;
    @BindView(R.id.btn_historic)
    ImageButton btnHistoric;
    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public ClientViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Home home) {
        txtName.setText(home.getClient().getName());
        txtClientId.setText(home.getClient().getId().toString());
    }
}

package br.com.app.ceasa.ui.view;

import android.view.View;
import android.widget.TextView;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.util.DateUtils;
import br.com.app.ceasa.util.MonetaryFormatting;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentHistoricViewHolder extends ChildViewHolder {

  @BindView(R.id.txt_payment_date)
  TextView txtPaymentDate;

  @BindView(R.id.txt_description)
  TextView txtDescription;

  @BindView(R.id.txt_value)
  TextView txtValue;

  public PaymentHistoricViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public void bind(final Payment payment) {
         txtPaymentDate.setText(DateUtils.convertDateToStringInFormat_dd_mm_yyyy(payment.getDate()));
         txtDescription.setText(payment.getDescription());
         txtValue.setText(MonetaryFormatting.convertToDolar(payment.getValue()));
  }
}

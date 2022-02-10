package br.com.app.rmalimentos.view.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.fragment.app.DialogFragment;
import br.com.app.rmalimentos.utils.Constants;

public class DateSalePickerDialog extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    // Do something with the date chosen by the user
    sendDateSaleForHomeFragment(year, month, day);
  }

  private void sendDateSaleForHomeFragment(final int year, final int month, final int day) {

    /*Existe um bug no calendar relacionado ao mes por isso ha necessidade de acrescentar +1 */
    String strMonth = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
    String strDay = (day) < 10 ? "0" + (day) : String.valueOf(day);

    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_DATE_SALE, strDay + "/" + strMonth + "/" + year);
    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
  }
}

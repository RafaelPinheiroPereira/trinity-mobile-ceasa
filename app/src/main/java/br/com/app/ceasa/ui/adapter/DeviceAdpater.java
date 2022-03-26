package br.com.app.ceasa.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.PrinterDP;
import br.com.app.ceasa.viewmodel.PrinterDPViewModel;

public class DeviceAdpater extends BaseAdapter {
  private List<PrinterDP> printerDPS = new ArrayList<>();
  private PrinterDPViewModel printerDPViewModel;

  public DeviceAdpater(final PrinterDPViewModel printerDPViewModel) {

    this.printerDPViewModel = printerDPViewModel;
  }

  public void add(String name, String mac) {
    PrinterDP printerDP = new PrinterDP(name, mac);
    printerDPS.add(printerDP);
  }


  public PrinterDP find(String mac) {
    for (PrinterDP printerDP : printerDPS) {
      if (mac.equals(printerDP.getMac())) {
        return printerDP;
      }
    }

    return null;
  }

  @Override
  public int getCount() {
    return printerDPS.size();
  }

  @Override
  public PrinterDP getItem(int position) {
    return printerDPS.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get layout to populate
    View v = convertView;
    if (v == null) {
      LayoutInflater vi = LayoutInflater.from(printerDPViewModel.getContext());
      v = vi.inflate(R.layout.device_node, null);
    }

    // Populate the layout with new data
    PrinterDP printerDP = getItem(position);

    ((TextView) v.findViewById(R.id.name)).setText(printerDP.getName());
    ((TextView) v.findViewById(R.id.address)).setText(printerDP.getMac());

    return v;
  }
}

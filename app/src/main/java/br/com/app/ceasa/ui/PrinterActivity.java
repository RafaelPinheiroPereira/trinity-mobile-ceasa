package br.com.app.ceasa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.PrinterDP;
import br.com.app.ceasa.ui.adapter.DeviceAdpater;
import br.com.app.ceasa.viewmodel.PrinterDPViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PrinterActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.btnConect)
  Button btnConect;

  @BindView(R.id.btnScan)
  Button btnScan;

  @BindView(R.id.edtAdressBluetooth)
  EditText edtAdressBluetooth;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.lvDevices)
  ListView lvDevices;

  private BluetoothAdapter mBtAdapter;
  private DeviceAdpater deviceAdpater;

  private PrinterDPViewModel viewModel;

  private final BroadcastReceiver mReceiver =
          new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              String action = intent.getAction();

              // When discovery finds a device
              if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                boolean bonded = device.getBondState() == BluetoothDevice.BOND_BONDED;
                int iconId =
                        bonded
                                ? R.mipmap.ic_bluetooth_connected_black_48dp
                                : R.mipmap.ic_bluetooth_black_48dp;
                // Find is device is already exists
                PrinterDP printerDP = deviceAdpater.find(device.getAddress());
                // Skip if device is already in list
                if (printerDP == null) {
                  deviceAdpater.add(device.getName(), device.getAddress());
                } else {
                  printerDP.setName(device.getName());
                  printerDP.setMac(device.getAddress());
                }

                // When discovery is finished, change the Activity title
              } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.title_select_device);
                findViewById(R.id.lnlEscanear).setVisibility(View.VISIBLE);
              }
            }
          };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_printer);
    ButterKnife.bind(this);
    initViews();
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
      setFinishOnTouchOutside(false);
    }
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Impressora");

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}

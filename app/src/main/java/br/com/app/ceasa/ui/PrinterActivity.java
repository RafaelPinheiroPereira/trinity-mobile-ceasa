package br.com.app.ceasa.ui;

import static br.com.app.ceasa.util.Constants.PREF_DEVICE_ADDRESS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Set;

import br.com.app.ceasa.R;
import br.com.app.ceasa.model.entity.PrinterDP;
import br.com.app.ceasa.tasks.InsertPrinterDPTask;
import br.com.app.ceasa.tasks.UpdatePrinterDPTask;
import br.com.app.ceasa.ui.adapter.DeviceAdapter;
import br.com.app.ceasa.util.Singleton;
import br.com.app.ceasa.viewmodel.PrinterDPViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrinterActivity extends AbstractActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.btnConect)
  Button btnConect;

  @BindView(R.id.btnScan)
  Button btnScan;

  @BindView(R.id.edtAdressBluetooth)
  EditText edtAdressBluetooth;

  @BindView(R.id.lvDevices)
  ListView lvDevices;

  private BluetoothAdapter mBtAdapter;
  private DeviceAdapter deviceAdapter;

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
                if (ActivityCompat.checkSelfPermission(PrinterActivity.this,
                        Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                  // TODO: Consider calling
                  //    ActivityCompat#requestPermissions
                  // here to request the missing permissions, and then
                  // overriding
                  //   public void onRequestPermissionsResult(int
                  //   requestCode, String[] permissions,
                  //                                          int[]
                  //                                          grantResults)
                  // to handle the case where the user grants the permission.
                  // See the documentation
                  // for ActivityCompat#requestPermissions for more details.
                  return;
                }
                boolean bonded = device.getBondState() == BluetoothDevice.BOND_BONDED;
                int iconId =
                        bonded
                                ? R.mipmap.ic_bluetooth_connected_black_48dp
                                : R.mipmap.ic_bluetooth_black_48dp;
                // Find is device is already exists
                PrinterDP printerDP = deviceAdapter.find(device.getAddress());
                // Skip if device is already in list
                if (printerDP == null) {
                  deviceAdapter.add(device.getName(), device.getAddress());
                } else {
                  printerDP.setName(device.getName());
                  printerDP.setMac(device.getAddress());
                }

                // When discovery is finished, change the Activity title
              } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.title_select_device);
                findViewById(R.id.lnlScan).setVisibility(View.VISIBLE);
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

  @Override
  protected void onStart() {
    super.onStart();
    mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    viewModel = new ViewModelProvider(this).get(PrinterDPViewModel.class);
    this.viewModel.setContext(this);
    deviceAdapter = new DeviceAdapter(this.viewModel);
    // Initialize the button to perform connect
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    edtAdressBluetooth.setText(prefs.getString(PREF_DEVICE_ADDRESS, ""));
    lvDevices.setAdapter(deviceAdapter);

    lvDevices.setOnItemClickListener(
            (parent, view, position, id) -> {
              if (ActivityCompat.checkSelfPermission(this,
                      Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode,
                //   String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission.
                // See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
              }
              mBtAdapter.cancelDiscovery();

              PrinterDP printerDPScanned = deviceAdapter.getItem(position);
              if (BluetoothAdapter.checkBluetoothAddress(printerDPScanned.getMac())) {
                this.selectPrinter(printerDPScanned.getMac(), printerDPScanned.getName());
                showMessage(this, "Impressora Selecionada:".concat(printerDPScanned.getName()));
                NavUtils.navigateUpFromSameTask(this);
              }
            });

    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(mReceiver, filter);

    // Register for broadcasts when discovery has finished
    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    registerReceiver(mReceiver, filter);

    if (mBtAdapter != null && mBtAdapter.isEnabled()) {
      // Get a set of currently paired devices
      if (ActivityCompat.checkSelfPermission(this,
              Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[]
        //   permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the
        // documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
      }
      if (ActivityCompat.checkSelfPermission(this,
              Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[]
        //   permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the
        // documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
      }
      Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

      // If there are paired devices, add each one to the ArrayAdapter
      if (pairedDevices.size() > 0) {
        for (BluetoothDevice device : pairedDevices) {
          deviceAdapter.add(device.getName(), device.getAddress());
        }
      }
      findViewById(R.id.txtTituloDesabilitado).setVisibility(View.GONE);
    } else {
      findViewById(R.id.lnlScan).setVisibility(View.GONE);
    }
  }

  private void initViews() {
    toolbar.setTitle("Trinity Mobile - Impressora");

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.btnConect)
  public void setBtnConect() {
    String address = edtAdressBluetooth.getText().toString();
    if (!address.isEmpty()) {
      this.selectPrinter(address, address);

      showMessage(PrinterActivity.this, "Impressora Selecionada:".concat(address));
      NavUtils.navigateUpFromSameTask(this);
    } else {
      edtAdressBluetooth.setError("Endereço obrigatório!");
    }
  }

  @OnClick(R.id.btnScan)
  public void setBtnScan() {
    startDiscovery();
  }

  private void startDiscovery() {
    // Indicate scanning in the title
    setProgressBarIndeterminateVisibility(true);
    setTitle(R.string.title_scanning);
    findViewById(R.id.lnlScan).setVisibility(View.GONE);

    // If we're already discovering, stop it
    if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[]
      //   permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the
      // documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    if (mBtAdapter.isDiscovering()) {
      mBtAdapter.cancelDiscovery();
    }

    // Request discover from BluetoothAdapter
    mBtAdapter.startDiscovery();
  }

  private void selectPrinter(String address, String name) {
    if (this.viewModel.isActivedPrinter()) {
      try {
        PrinterDP printerDPActived = this.viewModel.searchActivedPrinter();
        if (printerDPActived.getMac().equals(address)) {
          PrinterDP printerDPSearched = this.viewModel.searchPrinterByMac(address);
          if (printerDPSearched.getMac() != null) {
            // edita
            printerDPActived.setAtived(false);
            printerDPSearched.setAtived(true);

            this.viewModel.setPrinterDP(printerDPActived);
            new UpdatePrinterDPTask(viewModel, this).execute();
            this.viewModel.setPrinterDP(printerDPSearched);
            new UpdatePrinterDPTask(viewModel, this).execute();
          } else { // cria}
            printerDPSearched.setAtived(true);
            printerDPSearched.setName(printerDPSearched.getName());
            printerDPSearched.setMac(address);
            this.viewModel.setPrinterDP(printerDPSearched);
            new InsertPrinterDPTask(this.viewModel, this).execute();
          }
        }
      } catch (Throwable throwable) {
        showErrorMessage(this, throwable.getMessage());
      }
    } else {
      PrinterDP printerDP = new PrinterDP();
      printerDP.setName(name);
      printerDP.setMac(address);
      printerDP.setAtived(true);
      this.viewModel.setPrinterDP(printerDP);
      new InsertPrinterDPTask(this.viewModel, this).execute();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    final SharedPreferences.Editor edit = prefs.edit();
    edit.putString(PREF_DEVICE_ADDRESS, edtAdressBluetooth.getText().toString());
    edit.commit();

    // Make sure we're not doing discovery anymore
    cancelDiscovery();

    // Unregister broadcast listeners
    unregisterReceiver(mReceiver);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      setResult(RESULT_FIRST_USER);
      finish();
      return true;
    }
    return super.onKeyUp(keyCode, event);
  }

  private void cancelDiscovery() {
    if (mBtAdapter != null) {
      if (ActivityCompat.checkSelfPermission(this,
              Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[]
        //   permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the
        // documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
      }
      mBtAdapter.cancelDiscovery();
    }
  }
}

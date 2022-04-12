package br.com.app.ceasa.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.datecs.api.emsr.EMSR;
import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;
import java.util.UUID;

import br.com.app.ceasa.R;
import br.com.app.ceasa.listener.IPrinterRunnableListener;
import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.model.entity.Historic;
import br.com.app.ceasa.model.entity.Payment;
import br.com.app.ceasa.model.entity.PrinterDP;
import br.com.app.ceasa.ui.AbstractActivity;

public class PrinterDatecsUtil {

  AbstractActivity abstractActivity = new AbstractActivity();

  Activity activity;

  private Printer mPrinter;

  private ProtocolAdapter.Channel mPrinterChannel;

  private ProtocolAdapter mProtocolAdapter;

  private BluetoothSocket mBluetoothSocket;

  public PrinterDatecsUtil(final Activity activity) {
    this.activity = activity;
  }

  public synchronized boolean waitForConection(PrinterDP printerDP) {

    status(null);
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (bluetoothAdapter.isEnabled()) {
      // Checa conexao
      if (BluetoothAdapter.checkBluetoothAddress(printerDP.getMac())) {
        establishConnectionBluetooth(printerDP);
        return true;
      } else {
        abstractActivity.showMessage(
            activity.getApplicationContext(), "Não foi possível  estabelecer conexão");
        return false;
      }
    } else {
      abstractActivity.showMessage(
          activity.getApplicationContext(),
          "Bluetooth desligado!\nPor favor, habilite o bluetooth!");
      return false;
    }
  }

  public void closeConnection() {
    closeConection();
  }

  public void printPayment(final Payment payment, Client client) {

    runTask(
        (dialog, printer) -> {
          if (printer != null) {

            StringBuffer textBuffer = configuratePrinterLayoutPayment(payment,client);

            printer.reset();
            printer.printTaggedText(textBuffer.toString());
            printer.feedPaper(110);
            printer.flush();
          } else {
            // Tentar estabelecer conexao com impressora

          }
        },
        R.string.app_name);
  }

  public void printHistoric(final List<Historic> historics) {
    runTask(
        (dialog, printer) -> {
          StringBuffer textBuffer = configuratePrinterLayoutHistoric(historics);

          printer.reset();
          printer.printTaggedText(textBuffer.toString());
          printer.feedPaper(110);
          printer.flush();
        },
        R.string.app_name);
  }

  private void initPrint(InputStream inputStream, OutputStream outputStream, PrinterDP printerDP)
      throws IOException {

    // Here you can enable various debug information
    // ProtocolAdapter.setDebug(true);
    Printer.setDebug(true);
    EMSR.setDebug(true);

    // Check if printer is into protocol mode. Ones the object is created it can not be released
    // without closing base streams.
    mProtocolAdapter = new ProtocolAdapter(inputStream, outputStream);
    if (mProtocolAdapter.isProtocolEnabled()) {

      // Into protocol mode we can callbacks to receive printer notifications
      mProtocolAdapter.setPrinterListener(
          new ProtocolAdapter.PrinterListener() {
            @Override
            public void onBatteryStateChanged(boolean lowBattery) {
              if (lowBattery) {

                status("LOW BATTERY");
              } else {
                status(null);
              }
            }

            @Override
            public void onPaperStateChanged(boolean hasPaper) {
              if (hasPaper) {

                status("PAPER OUT");
              } else {
                status(null);
              }
            }

            @Override
            public void onThermalHeadStateChanged(boolean overheated) {
              if (overheated) {

                status("OVERHEATED");
              } else {
                status(null);
              }
            }
          });

      // Get printer instance
      mPrinterChannel = mProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
      mPrinter =
          new Printer(mPrinterChannel.getInputStream(), mPrinterChannel.getOutputStream());

    } else {

      // Protocol mode it not enables, so we should use the row streams.
      mPrinter =
          new Printer(mProtocolAdapter.getRawInputStream(), mProtocolAdapter.getRawOutputStream());
    }

    mPrinter.setConnectionListener(
        () -> {
          abstractActivity.showErrorMessage(this.activity, "Impressora está desconectada");

          abstractActivity.runOnUiThread(
              () -> {
                if (!activity.isFinishing()) {
                  //  esperarPorConexao(impressora);
                }
              });
        });
  }

  public void status(final String text) {

    abstractActivity.runOnUiThread(
        new Runnable() {
          @Override
          public void run() {}
        });

    abstractActivity.runOnUiThread(
        () -> {
          if (text != null) {
            abstractActivity.showMessage(activity.getApplicationContext(), text);
          } else {

          }
        });
  }

  private StringBuffer configuratePrinterLayoutPayment(final Payment payment,
                                                       Client client) {
    StringBuffer textBuffer = new StringBuffer();

    textBuffer.append("{center}{b}COHORTIFRUTI - MA ");
    textBuffer.append("{br}");
    textBuffer.append("{s}Cooperativa dos Hortifrutigranjeiros do MA");
    textBuffer.append("{br}");
    textBuffer.append("{s}------------------------------------------");
    textBuffer.append("{br}");
    textBuffer.append("{center}{h}COMPROVANTE DE PAGAMENTO ");
    textBuffer.append("{br}");
    textBuffer.append("{center}{h} (BANCA VAREJAO) {br}");
    textBuffer.append("{br}{reset}");
    textBuffer.append("{br}");
    textBuffer.append("{br}");
    textBuffer.append("{b}Data:");
    textBuffer.append(
        "{b}" + DateUtils.convertDateToStringInFormat_dd_mm_yyyy(payment.getDate()) + "{br}");
    textBuffer.append("{b}Cliente:");
    textBuffer.append("{b}" + client.getName() + "{br}");
    if(payment.getDescription()!=null && !payment.getDescription().isEmpty()){
        textBuffer.append("{b}OBS:");
        textBuffer.append("{b}" + payment.getDescription() + "{br}");
        textBuffer.append("{br}");
    }
    textBuffer.append("{br}");
    textBuffer.append("{reset}{right}{h}TOTAL: {/w}" + "R$ "+String.format("%.2f", payment.getValue()));
    textBuffer.append("{br}");
    textBuffer.append("{s}------------------------------------------");
    textBuffer.append("{br}");
    textBuffer.append("{s}" +  DateUtils.convertDateToStringInFormat_dd_mm_yyyy(payment.getDate()).replace("/","")+payment.getId());
    textBuffer.append("{br}");
    textBuffer.append("{br}");
    return textBuffer;
  }

  private StringBuffer configuratePrinterLayoutHistoric(final List<Historic> historicList) {

    StringBuffer textBuffer = new StringBuffer();
    textBuffer.append("{center}{b}COHORTIFRUTI - MA ");
    textBuffer.append("{br}");
    textBuffer.append("{s}Cooperativa dos Hortifrutigranjeiros do MA");
    textBuffer.append("{br}");
    textBuffer.append("{s}------------------------------------------");
    textBuffer.append("{br}");
    textBuffer.append(
        "{center}{h}RESUMO DE PAGAMENTO  "
            + DateUtils.convertDateToStringInFormat_dd_mm_yyyy(
                historicList.get(0).getDatePayment()));
    textBuffer.append("{br}");
    textBuffer.append("{center}{h} (BANCA VAREJAO) {br}{br}");
    historicList.forEach(
        historic -> {

          textBuffer.append(
              "{b}" + getNameConfigured(historic.getName()) + "R$"+String.format("% 9.2f",historic.getValue()) + "{br}");
        });
    // Aqui vem um foreach

    textBuffer.append("------------------------------------------{br}");
    textBuffer.append(
        "{reset}{right}{h}TOTAL: {/w}"
            +"R$ "+ String.format("%.2f", historicList.stream().mapToDouble(Historic::getValue).sum()));
    textBuffer.append("{br}");
    textBuffer.append("{br}");

    return textBuffer;
  }

    private String getNameConfigured(String name) {
      if(name.length()<30){
          for(int i=name.length();i<30;i++){
             name=name.concat(" ");
          }
      }else{
          name=name.substring(0,30);
      }
      return name.concat(" ");
    }

    private void establishConnectionBluetooth(final PrinterDP printerDP) {
    final ProgressDialog dialog = new ProgressDialog(this.activity);
    dialog.setTitle("Por favor , aguarde a conexão...");
    dialog.setMessage("Conectando o dispositivo");
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);

    if (!((Activity) this.activity).isFinishing()) {
      dialog.show();
    }

    final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    final Thread t =
        new Thread(
            () -> {
              btAdapter.cancelDiscovery();
              try {
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(printerDP.getMac());
                InputStream in = null;
                OutputStream out = null;

                try {

                  mBluetoothSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                  in = mBluetoothSocket.getInputStream();
                  out = mBluetoothSocket.getOutputStream();
                  mBluetoothSocket.connect();

                } catch (IOException e) {
                  return;
                }

                initPrint(in, out, printerDP);
              } catch (IOException e) {
                e.printStackTrace();
                abstractActivity.showErrorMessage(
                    this.activity, "Falha ao inicializar: " + e.getMessage());
              } finally {
                dialog.dismiss();
              }
            });
    t.start();
  }

  private synchronized void closeConection() {

    if (mBluetoothSocket != null) {

      try {
        mBluetoothSocket.close();
        if (mBluetoothSocket.isConnected()) {
          mBluetoothSocket.close();
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void runTask(final IPrinterRunnableListener r, final int msgResId) {
    final ProgressDialog dialog = new ProgressDialog(this.activity);
    dialog.setTitle("Impressão");
    dialog.setMessage("Por favor aguarde,imprimindo...");
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();

    Thread t =
        new Thread(
            () -> {
              try {
                r.run(dialog, mPrinter);
              } catch (IOException e) {
                e.printStackTrace();

                abstractActivity.showErrorMessage(
                    this.activity, "Ocorreu um erro de I/O: " + e.getMessage());
              } catch (Exception e) {
                e.printStackTrace();
                abstractActivity.showErrorMessage(
                    this.activity, "Ocorreu um erro critico: " + e.getMessage());

              } finally {
                dialog.dismiss();
              }
            });
    t.start();
  }
}

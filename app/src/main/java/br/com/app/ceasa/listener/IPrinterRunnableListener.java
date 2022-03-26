package br.com.app.ceasa.listener;

import android.app.ProgressDialog;

import com.datecs.api.printer.Printer;

import java.io.IOException;

public interface IPrinterRunnableListener {

  void run(ProgressDialog dialog, Printer printer) throws IOException;
}

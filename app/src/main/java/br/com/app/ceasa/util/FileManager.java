package br.com.app.ceasa.util;

import android.content.Context;
import android.media.MediaScannerConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager implements IFileManager {

  public FileManager() {}

  @Override
  public File createAppDirectory(Context context) throws FileNotFoundException {
    File directory = new File(context.getExternalMediaDirs()[0],Constants.APP_FOLDER_NAME);
    if (!directory.exists()) {
      directory.mkdirs();
      File file = new File(directory.getPath(), Constants.LOG_FILE);
      FileOutputStream pen = new FileOutputStream(file);
      try {
        pen.write("CEASA".getBytes());
        pen.flush();
        pen.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MediaScannerConnection.scanFile(context, new String[] {file.toString()}, null, null);
    }
    return directory;
  }

  @Override
  public File createOutputFile() {

    File outputFile = new File(Constants.APP_FOLDER_NAME + Constants.OUTPUT_FILE);
    return outputFile;
  }

  @Override
  public boolean fileExists(final Context context) {
    return new File(context.getExternalMediaDirs()[0]+ Constants.APP_FOLDER_NAME, Constants.INPUT_FILE).exists();
  }

  @Override
  public File createFile(String nameDirectory, String nameFile) {
    return new File(nameDirectory, nameFile);
  }

  @Override
  public void readFile(File file) throws IOException {}

  @Override
  public boolean containsInputFile(Context context) {
    return this.fileExists(context);
  }

}

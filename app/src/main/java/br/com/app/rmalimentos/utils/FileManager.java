package br.com.app.rmalimentos.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileManager implements IFileManager {

  public FileManager() {
  }

  @Override
  public File createAppDirectory(Context context) throws FileNotFoundException {
    File directory = Environment.getExternalStoragePublicDirectory(Constants.APP_FOLDER_NAME);
    if (!directory.exists()) {
      directory.mkdirs();
      File file = new File(Constants.APP_DIRECTORY, Constants.LOG_FILE);
      FileOutputStream pen = new FileOutputStream(file);
      try {
        pen.write("R&M-Alimentos LTDA".getBytes());
        pen.flush();
        pen.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
    }
    return directory;
  }

  @Override
  public File createOutputFile() throws IOException {

    File outputFile = new File(Constants.APP_FOLDER_NAME + Constants.OUTPUT_FILE);
    return outputFile;
  }

  @Override
  public boolean fileExists(final String inputFile) {
    File file = new File(Constants.APP_DIRECTORY);

    long count =
        Arrays.asList(file.listFiles()).stream()
                .filter(item->item.getName().equalsIgnoreCase(inputFile))
            .count();

    return count > 0;
  }

  @Override
  public File createFile(String nameDirectory, String nameFile) {
    return new File(nameDirectory, nameFile);
  }

  @Override
  public void readFile(File file)
      throws IOException, IllegalAccessException, InstantiationException {}

  @Override
  public boolean containsAllFiles() {

    long countFilesInexists =
        Arrays.asList((Constants.INPUT_FILES)).stream()
            .filter(inputFileName -> !this.fileExists(inputFileName))
            .count();
    return countFilesInexists <= 0;
  }

  @Override
  public StringBuilder searchInexistsFilesNames() {

    Stream<String> namesFiles =
        Arrays.asList((Constants.INPUT_FILES)).stream()
            .filter(inputFileName -> !this.fileExists(inputFileName));
    StringBuilder nameStringBuilder = new StringBuilder();
    namesFiles.forEach(nameFile -> nameStringBuilder.append(nameFile).append("\n"));
    return nameStringBuilder;
  }
}

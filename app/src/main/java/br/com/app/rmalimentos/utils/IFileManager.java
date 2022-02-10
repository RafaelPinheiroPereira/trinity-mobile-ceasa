package br.com.app.rmalimentos.utils;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileManager {

    File createAppDirectory(Context context) throws FileNotFoundException;

    boolean fileExists(final String inputFile);

    File createFile(String nameDirectory, String nameFile);

    void readFile(File file)
      throws IOException, IllegalAccessException, InstantiationException;

    boolean containsAllFiles();

    StringBuilder searchInexistsFilesNames();

    File createOutputFile() throws IOException;
}

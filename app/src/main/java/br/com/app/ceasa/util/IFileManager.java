package br.com.app.ceasa.util;

import android.content.Context;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFileManager {

    File createAppDirectory(Context context) throws FileNotFoundException;



    boolean fileExists(Context context);

    File createFile(String nameDirectory, String nameFile);

    void readFile(File file) throws IOException;

    File createOutputFile() ;

    boolean containsInputFile(Context context);


}

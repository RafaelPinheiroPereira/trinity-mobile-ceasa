package br.com.app.ceasa.repository;

import android.content.Context;

import java.io.IOException;

public interface IFileRepository {

    void readFile(Context context) throws IOException;
}

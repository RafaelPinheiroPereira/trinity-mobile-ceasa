package br.com.app.ceasa.repository;

import java.io.IOException;

public interface IFileRepository {

    void readFile() throws IOException, InstantiationException, IllegalAccessException;

}

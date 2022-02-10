package br.com.app.rmalimentos.repository;

import java.io.IOException;

public interface IFileRepository {

    void readFile() throws IOException, InstantiationException, IllegalAccessException;

}

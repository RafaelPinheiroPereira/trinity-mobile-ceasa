package br.com.app.ceasa.repository;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.utils.Constants;
import br.com.app.ceasa.utils.ClientFile;
import br.com.app.ceasa.utils.Singleton;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClientFileRepository implements IFileRepository {


    ClientFile clientFile;
    List<Client> clients;

    public ClientFileRepository() throws IllegalAccessException, InstantiationException {

        clientFile = Singleton.getInstance(ClientFile.class);
    }

    public boolean fileExists(final String inputFile) {
        return clientFile.fileExists(inputFile);
    }

    private void setCLients(final List<Client> cLients) {
        this.clients = cLients;
    }

    public List<Client> getClients() {
        return this.clients;
    }


    @Override
    public void readFile() throws IOException {
        File file = clientFile.createFile(Constants.APP_DIRECTORY, Constants.INPUT_FILES[0]);
        clientFile.readFile(file);
        this.setCLients(clientFile.getClients());
    }

}

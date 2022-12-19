package br.com.app.ceasa.repository;

import android.content.Context;

import br.com.app.ceasa.model.entity.Client;
import br.com.app.ceasa.util.Constants;
import br.com.app.ceasa.util.ClientFile;
import br.com.app.ceasa.util.Singleton;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClientFileRepository implements IFileRepository {


    ClientFile clientFile;
    List<Client> clients;

    public ClientFileRepository() throws IllegalAccessException, InstantiationException {

        clientFile = Singleton.getInstance(ClientFile.class);
    }



    private void setCLients(final List<Client> cLients) {
        this.clients = cLients;
    }

    public List<Client> getClients() {
        return this.clients;
    }


    @Override
    public void readFile(Context context) throws IOException {
        File file = clientFile.createFile(context.getExternalMediaDirs()[0]+Constants.APP_FOLDER_NAME, Constants.INPUT_FILE);
        clientFile.readFile(file);
        this.setCLients(clientFile.getClients());
    }

}

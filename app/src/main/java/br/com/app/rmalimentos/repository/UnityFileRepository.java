package br.com.app.rmalimentos.repository;

import br.com.app.rmalimentos.model.entity.Unity;
import br.com.app.rmalimentos.utils.Constants;
import br.com.app.rmalimentos.utils.ProductFile;
import br.com.app.rmalimentos.utils.Singleton;
import br.com.app.rmalimentos.utils.UnityFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class UnityFileRepository  implements  IFileRepository{
    UnityFile unityFile;
    List<Unity> unities;



    public UnityFileRepository() throws IllegalAccessException, InstantiationException {

        unityFile= Singleton.getInstance(UnityFile.class);
    }

    @Override
    public void readFile() throws IOException, InstantiationException, IllegalAccessException {
        File file = unityFile.createFile(Constants.APP_DIRECTORY, Constants.INPUT_FILES[4]);
        unityFile.readFile(file);
        this.setUnities(unityFile.getUnities());

    }
    public List<Unity> getUnities() {
        return unities;
    }

    private void setUnities(final List<Unity> unities) {
        this.unities = unities;
    }
}

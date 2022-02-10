package br.com.app.rmalimentos.utils;

import android.nfc.Tag;
import android.util.Log;
import br.com.app.rmalimentos.model.entity.Route;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RouteFile  extends FileManager  {

    String TAG = this.getClass().getSimpleName();
    List<Route> routes = new ArrayList<>();

    public RouteFile() {
    }

    @Override
    public void readFile(final File file) throws IOException, IllegalAccessException, InstantiationException {
        String line;
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br =
                new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.ISO_8859_1));

        while ((line = br.readLine()) != null && !line.equals("")){

             Route route= new Route();
             route.setId(Long.valueOf(line.substring(548,550)));
             route.setDescription(line.substring(550).trim());

             if(!routes.contains(route)){
                 routes.add(route);
             }else{
                 Log.d(TAG,route.toString());
             }


        }

        
    }

    public List<Route> getRoutes() {
        return this.routes;
    }
}

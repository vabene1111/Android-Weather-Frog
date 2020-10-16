package de.droidenschmiede.weather;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static java.net.HttpURLConnection.HTTP_MOVED_PERM;
import static java.net.HttpURLConnection.HTTP_MOVED_TEMP;
import static java.net.HttpURLConnection.HTTP_SEE_OTHER;

public class StatsManager {

    public static final String BASEURL = "http://droidenschmiede.bplaced.net/statsurl.php?";
    public static final String APPID = "de.droidenschmiede.weather";

    public MainActivity m;

    public StatsManager(MainActivity m){
        this.m = m;
    }

    public void fireStatOpened(){

        int userID = m.prefManager.getUniqueID();

        if(userID == -1){
            userID = Hilfsklassen.getRandomIntBetween(1, 1000000000);
            m.prefManager.setUniqueID(userID);
        }

        //payloadID = 1 => Messpunkt Oeffnungen der App
        //payloadValue = 0 => Ist egal, registriert nur die Oeffnungen über den Call
        String parameterURL = getParameterURL(userID + "", "1", "0");
        callServer(parameterURL);
    }

    public void callServer(final String parameterUrl){

        new Thread()
        {
            @Override
            public void run()
            {
                try {
                    HttpManager.makeRequest(BASEURL, true, parameterUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public String getParameterURL(String userID, String payloadID, String payloadValue){
        String parameters = "";
        parameters = "appid=" + APPID
                + "&userid=" + userID
                + "&payloadtyp=" + payloadID
                + "&payloadvalue=" + payloadValue;

        return parameters;
    }
}

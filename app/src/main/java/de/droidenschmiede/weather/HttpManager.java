package de.droidenschmiede.weather;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpManager {

    public MainActivity m;

    String result = null;
    InputStream is = null;
    StringBuilder sb = null;

    public HttpManager(MainActivity m){
        this.m = m;
    }

    /**
     * Redirecting-Server anfragen, als Response bekommt man die neue URL, diese dann wieder requesten
     * @param urlString
     * @param redirecting
     * @throws IOException
     */
    public static void makeRequest(String urlString, boolean redirecting, String parameterURL) throws IOException {

        Log.d("Call URL", urlString);

        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        // setDoOutput to true as we recieve data from json file
        urlConnection.setDoOutput(true);

        if(redirecting){
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String resultURL = readStream(in);
                Log.d("Redirecting to", resultURL);
                //makeRequest(resultURL + parameterURL, false, parameterURL);

                /*
                try {
                    openSSLAndGetStream(resultURL + parameterURL);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }

                 */
                new updateData().execute(resultURL + parameterURL);
            } finally {
                urlConnection.disconnect();
            }
        }
    }

    public static String readStream(InputStream in) throws IOException {

        String responseString = "";

        Reader inputStreamReader = new InputStreamReader(in);

        int data = inputStreamReader.read();
        while(data != -1){
            char theChar = (char) data;
            data = inputStreamReader.read();

            responseString += theChar;
        }
        inputStreamReader.close();

        return responseString;
    }

    public static InputStream openSSLAndGetStream(String urlStr) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Create the SSL connection
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());
        conn.setSSLSocketFactory(sc.getSocketFactory());

        // set Timeout and method
        conn.setReadTimeout(7000);
        conn.setConnectTimeout(7000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);

        // Add any data you wish to post here

        conn.connect();
        return conn.getInputStream();
    }

    private static class updateData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;

            try {
                URL url;
                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                } else {
                    InputStream err = conn.getErrorStream();
                }

                return "Done";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
        }

    }


}

package com.HashTagApps.WATool.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPDataHandler {

    private  String stream= null;
    public HTTPDataHandler(){

    }
    public String GetHTTPData(String urlString){
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in =new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = r.readLine())!=null){
                    stringBuilder.append(line);
                    stream= stringBuilder.toString();
                    urlConnection.disconnect();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}

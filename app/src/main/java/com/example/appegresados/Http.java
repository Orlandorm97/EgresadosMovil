package com.example.appegresados;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http{
    Context context;
    private String url,method="get", data=null, response=null;
    private Integer statusCode= 0;
    private Boolean token= false;
    private LocalStorage localStorage;

    public Integer getStatusCode(){
        return statusCode;
    }
    public Http(Context context, String url){
        this.context = context;
        this.url = url;
        localStorage = new LocalStorage(context);
    }

    public void setMethod(String method){
        this.method = method.toUpperCase();
    }

    public void setData(String data){
        this.data = data;
    }

    public void setToken(Boolean token){
        this.token = token;
    }

    public String getResponse(){
        return response;
    }


    public void send() {
        try {
            URL sUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) sUrl.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            if (token) {
                connection.setRequestProperty("Authorization", "Bearer " + localStorage.getToken());
            }
            if (!method.equals("GET")) {
                connection.setDoOutput(true);
            }

            if (data != null) {
                OutputStream ou = connection.getOutputStream();
                ou.write(data.getBytes());
                ou.flush();
                ou.close();
            }

            statusCode = connection.getResponseCode();

            InputStreamReader isr;
            if (statusCode >= 200 && statusCode <= 299) {
                // if success response
                isr = new InputStreamReader(connection.getInputStream());


            } else {
                // if error response
                isr = new InputStreamReader(connection.getErrorStream());
            }

            BufferedReader br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            response = sb.toString();
            Log.d("response", "***"+ response);

        }catch (IOException e){
                e.printStackTrace();
        }
    }
}

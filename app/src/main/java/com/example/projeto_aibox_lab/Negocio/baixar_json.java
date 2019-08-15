package com.example.projeto_aibox_lab.Negocio;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.HttpAuthHandler;

import com.example.projeto_aibox_lab.GUI.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class baixar_json {

   private static final String TAG = baixar_json.class.getSimpleName();


public baixar_json(){

}


public String makeServiceCall(String reqUrl){

    String response = null;

    try {

        URL url = new URL(reqUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = new BufferedInputStream(conn.getInputStream());
        response = convertStreamToString(in);

    } catch (MalformedURLException e) {
        Log.e(TAG,"MalformedURLException: " + e.getMessage());

    }catch (ProtocolException e) {
        Log.e(TAG,"ProtocolException: " + e.getMessage());

    }catch (IOException e) {
        Log.e(TAG,"IOException: " + e.getMessage());

    }catch (Exception e) {
        Log.e(TAG,"Exception: " + e.getMessage());

    }

    return response;

}

private String convertStreamToString(InputStream is){
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line;
    try{

        while ((line = reader.readLine()) != null){
            sb.append(line).append(("\n"));
        }
    } catch (IOException e){
        e.printStackTrace();

    } finally {
        try{
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

return sb.toString();
}

}
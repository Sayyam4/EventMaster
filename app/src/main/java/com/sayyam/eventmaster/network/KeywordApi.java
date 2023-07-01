package com.sayyam.eventmaster.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class KeywordApi {

    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList = new ArrayList<String>();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://tactile-runway-381307.wl.r.appspot.com/suggest?");
            sb.append("keyword="+input);
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;
            char[] buffer = new char[1024];
            while ((read = inputStreamReader.read(buffer)) != -1){
                jsonResult.append(buffer, 0, read);
            }


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        try {

            JSONObject jsonobject = new JSONObject(jsonResult.toString());
            JSONArray data = jsonobject.getJSONArray("data");

            Log.e("abc", String.valueOf(data.length()));

            for(int i = 0; i < data.length(); i++) {
                arrayList.add(data.getJSONObject(i).getString("name"));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return arrayList;
    }
}

package com.caribou.yaweapp.task;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DeleteAsyncTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {

        if (strings.length != 1) {
            throw new IllegalArgumentException("GetAsyncTask can only be executed with one argument !");
        }

        InputStream is = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setUseCaches(false);
            connection.connect();

            is = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {

            return e.getMessage();

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("GetAsyncTask", e.getMessage());
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("GetAsyncTask", e.getMessage());
                }
            }
        }
    }

}

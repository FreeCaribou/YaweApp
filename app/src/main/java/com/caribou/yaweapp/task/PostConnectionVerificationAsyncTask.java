package com.caribou.yaweapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class PostConnectionVerificationAsyncTask extends AsyncTask<User, String, String> {

    private PostConnectionVerificationAsyncTaskCallback callback;

    public PostConnectionVerificationAsyncTask(PostConnectionVerificationAsyncTaskCallback callback) {
        this.callback = callback;
    }


    @Override
    protected String doInBackground(User... users) {
        User u = users[0];
        InputStream inputStream = null;
        String result = "";

        String urlString = ListOfApiUrl.getUrlConnectionVerification();
        Log.i("url:",urlString);
        String message = "";
        JSONObject jo = new JSONObject();
        try {
            jo.accumulate("name", u.getName());
            jo.accumulate("password", u.getPassword());

            message = jo.toString();
            Log.i("message:",message);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            //open
            conn.connect();

            //setup send
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            //do somehting with response
            inputStream = conn.getInputStream();

            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            os.close();
            conn.disconnect();

            return sb.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return "FALSE";
    }


    @Override
    protected void onPostExecute(String sJSON) {
        super.onPostExecute(sJSON);
        Log.i("json:", sJSON);
        callback.onVerificationGet(sJSON);
    }

    public interface PostConnectionVerificationAsyncTaskCallback {
        void onVerificationGet(String sJSON);
    }
}

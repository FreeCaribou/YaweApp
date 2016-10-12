package com.caribou.yaweapp.task;

import android.os.AsyncTask;
import android.util.Log;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class PostUserAsyncTask extends AsyncTask<User, String, String>{


    @Override
    protected String doInBackground(User... users) {
        User u = users[0];
        InputStream inputStream = null;
        String result = "";

        String urlString = ListOfApiUrl.getUrlAddUser();
        String message = "";
        JSONObject jo = new JSONObject();
        try {

            byte admin = 0;
            byte heretic = 0;
            if(u.isAdmin()){
                admin = 1;
            }
            if(u.isHeretic()){
                heretic = 1;
            }

            jo.accumulate("name", u.getName());
            jo.accumulate("password", u.getPassword());
            jo.accumulate("admin", admin);
            jo.accumulate("heretic", heretic);

            message = jo.toString();
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

            os.close();
            conn.disconnect();
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

        return null;
    }


}

package com.caribou.yaweapp.task;


import android.os.AsyncTask;
import android.util.Log;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.model.UserDetail;

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

public class UpdateUserDetailAsyncTask extends AsyncTask<UserDetail,String,String> {
    @Override
    protected String doInBackground(UserDetail... params) {
        UserDetail ud = params[0];

        InputStream inputStream = null;
        String result = "";

        String urlString = ListOfApiUrl.getUrlUpdateUserDetail(String.valueOf(ud.getId()));
        Log.i("url put", urlString);
        String message = "";
        JSONObject jo = new JSONObject();
        try {

            jo.accumulate("id", ud.getId());
            jo.accumulate("race", ud.getFavoriteRace());
            jo.accumulate("profession", ud.getFavoriteProfession());
            jo.accumulate("activity", ud.getFavoriteActiviy());
            jo.accumulate("mood", ud.getMood());
            jo.accumulate("gameplay_style", ud.getGamePlayStyle());

            message = jo.toString();
            URL url = new URL(urlString);
            Log.i("up json",message);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
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
            Log.i("test", inputStream.toString());
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

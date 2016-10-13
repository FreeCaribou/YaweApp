package com.caribou.yaweapp.task;


import android.os.AsyncTask;
import android.util.Log;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.Tweet;
import com.caribou.yaweapp.model.TweetResponse;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostTweetResponseAsyncTask extends AsyncTask<TweetResponse,String,String>{

    @Override
    protected String doInBackground(TweetResponse... params) {
        TweetResponse t = params[0];

        InputStream inputStream = null;
        String result = "";

        String urlString = ListOfApiUrl.getUrlAddTweetResponse();
        Log.i("url: ", urlString);
        String message = "";
        JSONObject jo = new JSONObject();
        try {

            Date dt = new Date(t.getPostDate().getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curentTime = sdf.format(dt);


            jo.accumulate("tweet", t.getTweet());
            jo.accumulate("id_user", t.getId_user());
            jo.accumulate("postDate", curentTime);
            jo.accumulate("id_core", t.getId_core());

            message = jo.toString();
            Log.i("message json:" , message);
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

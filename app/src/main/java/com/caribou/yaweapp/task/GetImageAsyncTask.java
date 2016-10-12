package com.caribou.yaweapp.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public GetImageAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public Bitmap doInBackground(String... strings) {

        Bitmap image = null;
        InputStream is = null;

        try {
            String strUrl = "" + strings[0];
            URL url = new URL(strUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e("GetImageAsyncTaskError", e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("GetImageAsyncTaskError", e.getMessage());
                }
            }
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}

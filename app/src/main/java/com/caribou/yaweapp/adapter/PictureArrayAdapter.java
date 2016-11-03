package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.CustomVolleyRequestQueue;
import com.caribou.yaweapp.PictureDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PictureArrayAdapter extends ArrayAdapter<Picture> implements GetAsyncTask.GetAsyncTaskCallback{

    String author;

    public PictureArrayAdapter(Context context, ArrayList<Picture> pictures){
        super(context, 0,pictures);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        author = "";
        // Get the data item for this position
        final Picture p = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.picture_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
        // Populate the data into the template view using the data object

        // TODO give the user author
        tvTitle.setText(p.getTitle());

        ImageLoader mImageLoader;
        NetworkImageView mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.iv_item_image);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext()).getImageLoader();
        //Image URL - This can point to any image file supported by Android
        final String url = p.getUrl();
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                R.drawable.yawe_logo, android.R.drawable
                        .ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);

        GetAsyncTask task = new GetAsyncTask(PictureArrayAdapter.this);
        task.execute(ListOfApiUrl.getUrlUserById(String.valueOf(p.getId_user())));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goDetail = new Intent(getContext(), PictureDetailActivity.class);
                goDetail.putExtra("id_picture", p.getId());
                goDetail.putExtra("author_picture", author);
                goDetail.putExtra("title_picture", p.getTitle());
                goDetail.putExtra("description_picture", p.getDescription());
                goDetail.putExtra("url_picture", p.getUrl());
                goDetail.putExtra("id_author", p.getId_user());
                getContext().startActivity(goDetail);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }


    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            long jId = jsonObject.getLong("id");
            String jUsername = jsonObject.getString("name");
            author = jUsername;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

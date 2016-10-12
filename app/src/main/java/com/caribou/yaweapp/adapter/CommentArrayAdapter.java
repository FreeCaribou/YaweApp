package com.caribou.yaweapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.EventDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.model.Event;
import com.caribou.yaweapp.task.GetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentArrayAdapter extends ArrayAdapter<CommentPicture> implements GetAsyncTask.GetAsyncTaskCallback {

    String author;
    TextView tvAuthor;

    public CommentArrayAdapter(Context context, ArrayList<CommentPicture> commentPictures) {
        super(context, 0, commentPictures);
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        final CommentPicture cp = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }
        // Lookup view for data population
        tvAuthor = (TextView) convertView.findViewById(R.id.tv_commentItem_author);
//        GetAsyncTask task = new GetAsyncTask(CommentArrayAdapter.this);
//        task.execute(ListOfApiUrl.getUrlUserById(String.valueOf(cp.getId_user())));
        tvAuthor.setText(cp.getAuthor_name());
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_commentItem_date);
        TextView tvHour = (TextView) convertView.findViewById(R.id.tv_commentItem_hour);
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_commentItem_text);
        // Populate the data into the template view using the data object
        tvText.setText(cp.getText());

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        String date = sdfDate.format(cp.getPostDate());
        String hour = sdfHour.format(cp.getPostDate());

        tvDate.setText(date);
        tvHour.setText(hour);



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
            tvAuthor.setText(jUsername);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.Tweet;
import com.caribou.yaweapp.model.TweetResponse;
import com.caribou.yaweapp.task.PostTweetResponseAsyncTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TweetResponseArrayAdapter extends ArrayAdapter<TweetResponse> {

    int listSize;
    TextView tvAuthor;
    TextView tvTweet;
    TextView tvDate;

    public TweetResponseArrayAdapter(Context context, ArrayList<TweetResponse> tweets) {
        super(context, 0, tweets);
        listSize = tweets.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final Tweet t = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_response_list_item, parent, false);
        }
        // Lookup view for data population
        tvAuthor = (TextView) convertView.findViewById(R.id.tv_tweetResponseItem_author);
        tvAuthor.setText(t.getAuthor());
        tvDate = (TextView) convertView.findViewById(R.id.tv_tweetResponseItem_date);
        tvTweet = (TextView) convertView.findViewById(R.id.tv_tweetResponseItem_tweet);
        // Populate the data into the template view using the data object
        tvTweet.setText(t.getTweet());

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy HH:mm");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        String date = sdfDate.format(t.getPostDate());
        tvDate.setText(date);
        if ((position + 1) == listSize) {
            convertView.setPadding(0, 0, 0, 80);
            tvTweet.setPadding(0, 0, 0, 50);
            Log.i("sbire:", "j'aggrandi mle dernier");
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

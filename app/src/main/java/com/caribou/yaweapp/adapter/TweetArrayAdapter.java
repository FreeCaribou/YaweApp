package com.caribou.yaweapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.MemberDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.model.Tweet;
import com.caribou.yaweapp.model.TweetResponse;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.PostTweetAsyncTask;
import com.caribou.yaweapp.task.PostTweetResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> implements GetAsyncTask.GetAsyncTaskCallback {

    int listSize;
    TextView tvAuthor;
    TextView tvTweet;
    TextView tvDate;
    Button btTweetResponse;
    String newTweet;
    LinearLayout llTweet;

    public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets, Activity activity) {
        super(context, 0, tweets);
        listSize = tweets.size();
    }

    public View getView(int position, View convertView, final ViewGroup parent) {


        // Get the data item for this position
        final Tweet t = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }
        // Lookup view for data population
        btTweetResponse = (Button) convertView.findViewById(R.id.bt_tweetItem_response);
        tvAuthor = (TextView) convertView.findViewById(R.id.tv_tweetItem_author);
        tvAuthor.setText(t.getAuthor());
        tvDate = (TextView) convertView.findViewById(R.id.tv_tweetItem_date);
        tvTweet = (TextView) convertView.findViewById(R.id.tv_tweetItem_tweet);
        // Populate the data into the template view using the data object
        tvTweet.setText(t.getTweet());

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy HH:mm");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
        String date = sdfDate.format(t.getPostDate());
        tvDate.setText(date);
        if ((position + 1) == listSize) {
            convertView.setPadding(0, 0, 0, 100);
            tvTweet.setPadding(0, 0, 0, 100);
            Log.i("sbire:", "j'aggrandi mle dernier");
        }

        // TODO indiquer combien de reponse il y a


        btTweetResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                final EditText edNewTweet = new EditText(getContext());
                edNewTweet.setHint("Your tweet response");
                edNewTweet.setText(newTweet);
                alert.setMessage("Response at: " + t.getTweet());
                alert.setTitle("Post a response");
                alert.setView(edNewTweet);
                alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        newTweet = edNewTweet.getText().toString();
                        Log.i("text:", newTweet);
                        if (newTweet.length() > 140) {
                            Toast.makeText(getContext(), "Max 140 character please", Toast.LENGTH_SHORT).show();
                        } else if (newTweet.isEmpty()) {
                            Toast.makeText(getContext(), "Your tweet is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            TweetResponse tr = new TweetResponse();
                            tr.setId_core(t.getId());
                            tr.setTweet(newTweet);
                            tr.setPostDate(Calendar.getInstance().getTime());
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            long idUser = prefs.getLong("id_user", 0);
                            tr.setId_user(idUser);

                            PostTweetResponseAsyncTask task = new PostTweetResponseAsyncTask();
                            task.execute(tr);

                            Toast.makeText(getContext(), "Tweet response posted!", Toast.LENGTH_SHORT).show();
                            newTweet = "";
                        }
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        newTweet = edNewTweet.getText().toString();
                    }
                });
                alert.show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llTweet = new LinearLayout(getContext());
                llTweet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llTweet.setOrientation(LinearLayout.VERTICAL);
                GetAsyncTask task = new GetAsyncTask(TweetArrayAdapter.this);
                task.execute(ListOfApiUrl.getUrlAllTweetResponseByIdCore(String.valueOf(t.getId())));
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage(t.getTweet());
                alert.setTitle("The response of the tweet:");
                Log.i("sbire:", "avant set view");
                alert.setView(llTweet);
                Log.i("sbire:", "apres set view");
                alert.show();
                Log.i("sbire:", "show");

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
            JSONArray jResponse = new JSONArray(sJSON);
            for (int i = 0; i < jResponse.length(); i++) {
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("postDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String tweet = jEvent.getString("tweet");
                long id_user = jEvent.getLong("id_user");
                long id_core = jEvent.getLong("id_core");
                Date date = new Date(d.getTime());
                String author_name = jEvent.getString("name");
                TweetResponse t = new TweetResponse(id, id_user, tweet, date, author_name, id_core);

                LinearLayout llOneTweet = new LinearLayout(getContext());
                llOneTweet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llOneTweet.setOrientation(LinearLayout.VERTICAL);

                LinearLayout llAuthorDate = new LinearLayout(getContext());
                llAuthorDate.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llAuthorDate.setOrientation(LinearLayout.HORIZONTAL);

                TextView textAuthor = new TextView(getContext());
                textAuthor.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textAuthor.setText(t.getAuthor());
                textAuthor.setPadding(0,0,10,0);
                llAuthorDate.addView(textAuthor);

                TextView textDate = new TextView(getContext());
                textDate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                SimpleDateFormat sdfPost = new SimpleDateFormat("dd-MM-yy HH:mm");
                textDate.setText(sdfPost.format(t.getPostDate()));
                textDate.setPadding(10,0,0,0);
                llAuthorDate.addView(textDate);
                llAuthorDate.setPadding(0,0,10,0);
                llOneTweet.addView(llAuthorDate);

                TextView textTweet = new TextView(getContext());
                textTweet.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textTweet.setText(t.getTweet());
                llOneTweet.addView(textTweet);
                llOneTweet.setPadding(5,0,5,20);


                llTweet.addView(llOneTweet);
                Log.i("tweet:", t.getTweet());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

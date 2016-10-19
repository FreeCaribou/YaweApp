package com.caribou.yaweapp.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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
    TextView tvNbResponse;
    int nbResponse;
    //    Button btTweetResponse;
    String newTweet;
    LinearLayout llTweet;
    ScrollView svTweet;
    EditText edNewTweet;
    AlertDialog alert;
    long id;

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
//        btTweetResponse = (Button) convertView.findViewById(R.id.bt_tweetItem_response);
        tvAuthor = (TextView) convertView.findViewById(R.id.tv_tweetItem_author);
        tvAuthor.setText(t.getAuthor());
        tvDate = (TextView) convertView.findViewById(R.id.tv_tweetItem_date);
        tvTweet = (TextView) convertView.findViewById(R.id.tv_tweetItem_tweet);
        tvNbResponse = (TextView) convertView.findViewById(R.id.tv_tweetItem_nbResponse);
        // Populate the data into the template view using the data object
        tvTweet.setText(t.getTweet());
        id = t.getId();
        nbResponse = t.getNbResponse();
        String responseText = getContext().getString(R.string.response);
        tvNbResponse.setText(responseText + " " + String.valueOf(nbResponse));

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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
                svTweet = new ScrollView((getContext()));
                llTweet = new LinearLayout(getContext());
                svTweet.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                llTweet.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llTweet.setOrientation(LinearLayout.VERTICAL);
                id = t.getId();
                GetAsyncTask task = new GetAsyncTask(TweetArrayAdapter.this);
                task.execute(ListOfApiUrl.getUrlAllTweetResponseByIdCore(String.valueOf(t.getId())));
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                alert = b.create();
                alert.setMessage(t.getTweet());
                alert.setTitle(getContext().getString(R.string.response_tweet_title));
                alert.setView(svTweet);
                alert.show();
                alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

    public void createDialog() {

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
                textAuthor.setPadding(0, 0, 10, 0);
                llAuthorDate.addView(textAuthor);

                TextView textDate = new TextView(getContext());
                textDate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                SimpleDateFormat sdfPost = new SimpleDateFormat("dd-MM-yy HH:mm");
                textDate.setText(sdfPost.format(t.getPostDate()));
                textDate.setPadding(10, 0, 0, 0);
                llAuthorDate.addView(textDate);
                llAuthorDate.setPadding(0, 0, 10, 0);
                llOneTweet.addView(llAuthorDate);

                TextView textTweet = new TextView(getContext());
                textTweet.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textTweet.setText(t.getTweet());
                llOneTweet.addView(textTweet);
                llOneTweet.setPadding(5, 0, 5, 20);


                llTweet.addView(llOneTweet);
                Log.i("tweet:", t.getTweet());
            }
            edNewTweet = new EditText(getContext());
            edNewTweet.setHint("Your tweet response");
            edNewTweet.setText(newTweet);
            llTweet.addView(edNewTweet);

            Button post = new Button(getContext());
            post.setText("Post");
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newTweet = edNewTweet.getText().toString();
                    Log.i("text:", newTweet);
                    if (newTweet.length() > 140) {
                        Toast.makeText(getContext(), "Max 140 character please", Toast.LENGTH_SHORT).show();
                    } else if (newTweet.isEmpty()) {
                        Toast.makeText(getContext(), "Your tweet is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        TweetResponse tr = new TweetResponse();
                        tr.setId_core(id);
                        tr.setTweet(newTweet);
                        tr.setPostDate(Calendar.getInstance().getTime());
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        long idUser = prefs.getLong("id_user", 0);
                        tr.setId_user(idUser);

                        PostTweetResponseAsyncTask task = new PostTweetResponseAsyncTask();
                        task.execute(tr);

                        Toast.makeText(getContext(), "Tweet response posted!", Toast.LENGTH_SHORT).show();
                        newTweet = "";
                        alert.dismiss();

                    }
                }
            });

            llTweet.addView(post);
            svTweet.addView(llTweet);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

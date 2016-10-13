package com.caribou.yaweapp.adapter;


import android.app.Activity;
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

public class TweetArrayAdapter extends ArrayAdapter<Tweet> implements GetAsyncTask.GetAsyncTaskCallback{

    int listSize;
    TextView tvAuthor;
    TextView tvTweet;
    TextView tvDate;
    Button btTweetResponse;
    String newTweet;
    ListView lvResponseTweet;
    ArrayList<TweetResponse> listResponseTweet;
    Activity activity;

    public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets, Activity activity) {
        super(context, 0, tweets);
        listSize = tweets.size();
        this.activity = activity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final Tweet t = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }
        // Lookup view for data population
        lvResponseTweet = (ListView) convertView.findViewById(R.id.lv_tweetItem_responseTweet);
        listResponseTweet = new ArrayList<>();

        activity.registerForContextMenu(lvResponseTweet);

        GetAsyncTask task = new GetAsyncTask(TweetArrayAdapter.this);
        task.execute(ListOfApiUrl.getUrlAllTweetResponseByIdCore(String.valueOf(t.getId())));

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
        if((position + 1) == listSize){
            convertView.setPadding(0,0,0,80);
            tvTweet.setPadding(0,0,0,50);
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

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onPreGet() {

    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     * src: http://blog.lovelyhq.com/setting-listview-height-depending-on-the-items/
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPostGet(String sJSON) {

        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listResponseTweet.removeAll(listResponseTweet);

            // TODO je pense que je vais le faire "mochement" en mode linearlayout et textview qui s'ajoute

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
                Log.i("toString cp: ", t.toString());
                listResponseTweet.add(t);
            }

            TweetResponseArrayAdapter adapter = new TweetResponseArrayAdapter(getContext(), listResponseTweet);
            lvResponseTweet.setAdapter(adapter);
            setListViewHeightBasedOnItems(lvResponseTweet);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

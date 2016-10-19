package com.caribou.yaweapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.CommentArrayAdapter;
import com.caribou.yaweapp.adapter.TweetArrayAdapter;
import com.caribou.yaweapp.fragment.MemberFragment;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.model.Tweet;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.PostTweetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MemberDetailActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    boolean isAuthor;
    TextView tvName;
    TextView tvRace;
    TextView tvProfession;
    TextView tvMood;
    TextView tvActivity;
    TextView tvGamePlay;
    Button btEdit;
    Button btPostTweet;
    ListView lvTweet;
    ArrayList<Tweet> listTweet;
    long id;
    String newTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        isAuthor = false;
        tvActivity = (TextView) findViewById(R.id.tv_memberDetail_activity);
        tvGamePlay = (TextView) findViewById(R.id.tv_memberDetail_gameplay);
        tvRace = (TextView) findViewById(R.id.tv_memberDetail_race);
        tvProfession = (TextView) findViewById(R.id.tv_memberDetail_profession);
        tvMood = (TextView) findViewById(R.id.tv_memberDetail_mood);
        tvName = (TextView) findViewById(R.id.tv_memberDetail_name);
        lvTweet = (ListView) findViewById(R.id.lv_memberDetail_tweet);

        final Bundle extra = this.getIntent().getExtras();
        if (extra != null) {
            id = extra.getLong("id");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MemberDetailActivity.this);
            long idUser = prefs.getLong("id_user", 0);
            if (id == idUser) {
                isAuthor = true;
            }
            tvActivity.setText(extra.getString("activity"));
            tvName.setText(extra.getString("name"));
            tvRace.setText(extra.getString("race"));
            tvProfession.setText(extra.getString("profession"));
            tvMood.setText(extra.getString("mood"));
            tvGamePlay.setText(extra.getString("gameplay_style"));
            listTweet = new ArrayList<>();
            registerForContextMenu(lvTweet);

            GetAsyncTask task = new GetAsyncTask(MemberDetailActivity.this);
            task.execute(ListOfApiUrl.getUrlAllTweetByIdUser(String.valueOf(id)));
        }

        if (isAuthor) {
            btEdit = (Button) findViewById(R.id.bt_memberDetail_edit);
            btPostTweet = (Button) findViewById(R.id.bt_memberDetail_postTweet);
            btEdit.setVisibility(View.VISIBLE);
            btPostTweet.setVisibility(View.VISIBLE);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goEdit = new Intent(MemberDetailActivity.this, MyAccountActivity.class);
                    startActivity(goEdit);
                    finish();
                }
            });

            newTweet = "";
            btPostTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MemberDetailActivity.this);
                    final EditText edNewTweet = new EditText(MemberDetailActivity.this);
                    edNewTweet.setHint("Your new tweet");
                    edNewTweet.setText(newTweet);
                    alert.setMessage("You have kill a german player in WvW? Say it!");
                    alert.setTitle("Post a new tweet");
                    alert.setView(edNewTweet);
                    alert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            newTweet = edNewTweet.getText().toString();
                            Log.i("text:", newTweet);
                            if (newTweet.length() > 140) {
                                Toast.makeText(MemberDetailActivity.this, "Max 140 character please", Toast.LENGTH_SHORT).show();
                            } else if (newTweet.isEmpty()) {
                                Toast.makeText(MemberDetailActivity.this, "Your tweet is empty", Toast.LENGTH_SHORT).show();
                            } else {
                                Tweet t = new Tweet();
                                t.setId_user(id);
                                t.setPostDate(Calendar.getInstance().getTime());
                                t.setTweet(newTweet);
                                PostTweetAsyncTask task = new PostTweetAsyncTask();
                                task.execute(t);
                                GetAsyncTask taskGet = new GetAsyncTask(MemberDetailActivity.this);
                                taskGet.execute(ListOfApiUrl.getUrlAllTweetByIdUser(String.valueOf(id)));
                                Toast.makeText(MemberDetailActivity.this, "Tweet posted!", Toast.LENGTH_SHORT).show();
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
        }
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
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listTweet.removeAll(listTweet);

            for (int i = 0; i < jResponse.length(); i++) {
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("postDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String tweet = jEvent.getString("tweet");
                long id_user = jEvent.getLong("id_user");
                Date date = new Date(d.getTime());
                String author_name = jEvent.getString("name");
                int nbResponse = jEvent.getInt("nbResponse");
                Tweet t = new Tweet(id, id_user, tweet, date, author_name, nbResponse);
                Log.i("toString cp: ", t.toString());
                Log.i("id du tweet", String.valueOf(t.getId()));

                listTweet.add(t);
            }

            TweetArrayAdapter adapter = new TweetArrayAdapter(MemberDetailActivity.this, listTweet, this);
            lvTweet.setAdapter(adapter);
            setListViewHeightBasedOnItems(lvTweet);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

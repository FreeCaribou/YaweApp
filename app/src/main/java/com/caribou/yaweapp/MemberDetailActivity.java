package com.caribou.yaweapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.CommentArrayAdapter;
import com.caribou.yaweapp.adapter.TweetArrayAdapter;
import com.caribou.yaweapp.fragment.MemberFragment;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.model.Tweet;
import com.caribou.yaweapp.task.GetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MemberDetailActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback{

    boolean isAuthor;
    TextView tvName;
    TextView tvRace;
    TextView tvProfession;
    TextView tvMood;
    TextView tvActivity;
    TextView tvGamePlay;
    Button btEdit;
    ListView lvTweet;
    ArrayList<Tweet> listTweet;

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
        if(extra != null){
            long id = extra.getLong("id");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MemberDetailActivity.this);
            long idUser = prefs.getLong("id_user", 0);
            if(id == idUser){
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

        if(isAuthor){
            btEdit = (Button) findViewById(R.id.bt_memberDetail_edit);
            btEdit.setVisibility(View.VISIBLE);
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goEdit = new Intent(MemberDetailActivity.this, MyAccountActivity.class);
                    startActivity(goEdit);
                    finish();
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

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("postDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String tweet = jEvent.getString("tweet");
                long id_user = jEvent.getLong("id_user");
                Date date = new Date(d.getTime());
                String author_name = jEvent.getString("name");
                Tweet t = new Tweet(id, id_user, tweet, date, author_name);
                Log.i("toString cp: ", t.toString());

                listTweet.add(t);
            }

            TweetArrayAdapter adapter = new TweetArrayAdapter(MemberDetailActivity.this, listTweet);
            lvTweet.setAdapter(adapter);
            setListViewHeightBasedOnItems(lvTweet);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

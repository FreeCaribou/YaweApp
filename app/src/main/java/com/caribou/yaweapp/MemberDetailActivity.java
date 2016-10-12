package com.caribou.yaweapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.caribou.yaweapp.fragment.MemberFragment;
import com.caribou.yaweapp.task.GetAsyncTask;

public class MemberDetailActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback{

    boolean isAuthor;
    TextView tvName;
    TextView tvRace;
    TextView tvProfession;
    TextView tvMood;
    TextView tvActivity;
    TextView tvGamePlay;
    Button btEdit;

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

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String s) {

    }
}

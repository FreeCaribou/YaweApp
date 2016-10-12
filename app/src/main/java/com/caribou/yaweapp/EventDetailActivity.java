package com.caribou.yaweapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.task.DeleteAsyncTask;

public class EventDetailActivity extends AppCompatActivity {

    Button btDelete;
    TextView tvTitle;
    TextView tvDescription;
    TextView tvDate;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        btDelete = (Button) findViewById(R.id.bt_eventDetail_delete);
        tvDate = (TextView) findViewById(R.id.tv_eventDetail_date);
        tvTitle = (TextView) findViewById(R.id.tv_eventDetail_title);
        tvDescription = (TextView) findViewById(R.id.tv_eventDetail_description);
        id = 0;


        final Bundle extra = this.getIntent().getExtras();
        if(extra != null) {
            id = extra.getLong("id");
            tvTitle.setText(extra.getString("title"));
            tvDate.setText(extra.getString("date"));
            tvDescription.setText(extra.getString("description"));

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EventDetailActivity.this);
            boolean isAdmin = prefs.getBoolean("isAdmin", false);

            if (isAdmin) {
                btDelete.setVisibility(View.VISIBLE);
            }

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteAsyncTask delete = new DeleteAsyncTask();
                    delete.execute(ListOfApiUrl.getUrlDeleteEvent(String.valueOf(id)));
                    finish();
                }
            });
        }




    }
}

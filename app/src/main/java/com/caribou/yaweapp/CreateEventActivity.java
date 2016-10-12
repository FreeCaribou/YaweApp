package com.caribou.yaweapp;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.caribou.yaweapp.model.Event;
import com.caribou.yaweapp.task.PostEventAsyncTask;
import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    EditText edTtitle;
    EditText edDescription;
    DatePicker dpDate;
    TimePicker tpDate;
    Button btCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edTtitle = (EditText) findViewById(R.id.ed_createEvent_title);
        edDescription = (EditText) findViewById(R.id.ed_createEvent_description);
        dpDate = (DatePicker) findViewById(R.id.dp_createEvent_dateDay);
        tpDate = (TimePicker) findViewById(R.id.tp_createEvent_time);
        btCreate = (Button) findViewById(R.id.bt_createEvent_create);

        tpDate.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tpDate.setHour(20);
            tpDate.setMinute(0);
        } else {
            tpDate.setCurrentHour(20);
            tpDate.setCurrentMinute(0);
        }

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Event e = new Event();
                e.setTitle(edTtitle.getText().toString());
                e.setDescription(edDescription.getText().toString());
                long dateTime = dpDate.getCalendarView().getDate();
                Date d = new Date(dateTime);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    d.setHours(tpDate.getHour());
                    d.setMinutes(tpDate.getMinute());
                }else {
                    d.setHours(tpDate.getCurrentHour());
                    d.setMinutes(tpDate.getCurrentMinute());
                }

                e.setDateEvent(d);
                String show = e.toString();
                PostEventAsyncTask task = new PostEventAsyncTask();
                task.execute(e);
                finish();

            }
        });

    }
}

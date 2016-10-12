package com.caribou.yaweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.EventArrayAdapter;
import com.caribou.yaweapp.model.Event;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListEventsActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    ListView lvListEvents;
    ArrayList<Event> listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);

        lvListEvents = (ListView) findViewById(R.id.lv_listEvents_list);
        listEvents = new ArrayList<>();

        registerForContextMenu(lvListEvents);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetAsyncTask task = new GetAsyncTask(ListEventsActivity.this);
        task.execute(ListOfApiUrl.getUrlAllEvent());
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {

        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listEvents.removeAll(listEvents);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String title = jEvent.getString("title");
                String description = jEvent.getString("description");
                Date date = new Date(d.getTime());
                Event event = new Event(id,date,title,description);
                listEvents.add(event);
            }

            EventArrayAdapter adapter = new EventArrayAdapter(ListEventsActivity.this, listEvents);
            lvListEvents.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

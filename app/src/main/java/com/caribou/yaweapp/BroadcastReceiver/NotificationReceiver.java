package com.caribou.yaweapp.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.Event;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver implements GetAsyncTask.GetAsyncTaskCallback{

    private Context context;
    ArrayList<Event> listEvents;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        listEvents = new ArrayList<>();

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected() || mobile.isConnected()) {

            GetAsyncTask task = new GetAsyncTask(NotificationReceiver.this);
            task.execute(ListOfApiUrl.getUrlAllEvent());

        } else {
            // do nothing
        }
        // an Intent broadcast.
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        listEvents.removeAll(listEvents);

        JSONArray jResponse = null;
        try {
            jResponse = new JSONArray(sJSON);

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

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date dateNow = Calendar.getInstance().getTime();
        Boolean sameDay = false;
        Event e = new Event();

        for (Event item : listEvents) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(dateNow);
            cal2.setTime(item.getDateEvent());
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                sameDay = true;
                e.setId(item.getId());
                e.setDateEvent(item.getDateEvent());
                e.setTitle(item.getTitle());
                e.setDescription(item.getDescription());
            }
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isRead = prefs.getBoolean(String.valueOf(e.getId()), false);

        if(sameDay && !isRead){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.yawe_logo);
            builder.setContentTitle(e.getTitle());
            String description;

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final String curentTime = sdf.format(e.getDateEvent());

            description = curentTime;
            builder.setContentText(description);
            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification);
            editor.putBoolean(String.valueOf(e.getId()),true);
            editor.apply();
        } else {

        }

    }
}

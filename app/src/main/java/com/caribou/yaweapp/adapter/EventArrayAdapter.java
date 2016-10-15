package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.caribou.yaweapp.EventDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.Event;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {

    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Event e = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_eventItem_title);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_eventItem_date);
        // Populate the data into the template view using the data object
        tvTitle.setText(e.getTitle());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String curentTime = sdf.format(e.getDateEvent());
        tvDate.setText(curentTime);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goDetail = new Intent(getContext(), EventDetailActivity.class);
                goDetail.putExtra("id",e.getId());
                goDetail.putExtra("title", e.getTitle());
                goDetail.putExtra("date", curentTime);
                goDetail.putExtra("description", e.getDescription());
                getContext().startActivity(goDetail);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }





}

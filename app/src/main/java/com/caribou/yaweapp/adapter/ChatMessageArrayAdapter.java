package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.caribou.yaweapp.R;
import com.caribou.yaweapp.model.ChatMessage;
import com.caribou.yaweapp.model.CommentPicture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by fgerard on 07-10-16.
 */

public class ChatMessageArrayAdapter extends ArrayAdapter<ChatMessage> {

    String author;
    TextView tvAuthor;

    public ChatMessageArrayAdapter(Context context, ArrayList<ChatMessage> chatMessages) {
        super(context, 0, chatMessages);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final ChatMessage cm = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item, parent, false);
        }
        // Lookup view for data population
        tvAuthor = (TextView) convertView.findViewById(R.id.tv_commentItem_author);
        tvAuthor.setText(cm.getAuthor_name());
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_commentItem_date);
        TextView tvHour = (TextView) convertView.findViewById(R.id.tv_commentItem_hour);
        TextView tvText = (TextView) convertView.findViewById(R.id.tv_commentItem_text);
        // Populate the data into the template view using the data object
        tvText.setText(cm.getText());

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
        String date = sdfDate.format(cm.getPostDate());
        String hour = sdfHour.format(cm.getPostDate());

        tvDate.setText(date);
        tvHour.setText(hour);

        // Return the completed view to render on screen
        return convertView;
    }

}

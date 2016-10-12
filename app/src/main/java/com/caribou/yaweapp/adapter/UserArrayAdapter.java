package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.UserDetailActivity;
import com.caribou.yaweapp.model.User;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<User> {

    public UserArrayAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final User u = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_listUserItem_name);
        Button btDetail = (Button) convertView.findViewById(R.id.bt_listUserItem_detail);
        // Populate the data into the template view using the data object
        tvName.setText(u.getName());

        btDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), String.valueOf(u.getId()), Toast.LENGTH_SHORT).show();
                Intent goDetail = new Intent(getContext(), UserDetailActivity.class);
                goDetail.putExtra("id",u.getId());
                goDetail.putExtra("name", u.getName());
                goDetail.putExtra("isAdmin", u.isAdmin());
                goDetail.putExtra("isHeretic", u.isHeretic());

                getContext().startActivity(goDetail);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }


}

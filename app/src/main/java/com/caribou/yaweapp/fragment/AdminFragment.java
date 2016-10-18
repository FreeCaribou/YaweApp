package com.caribou.yaweapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.caribou.yaweapp.CreateEventActivity;
import com.caribou.yaweapp.CreateUserActivity;
import com.caribou.yaweapp.EditGuildMessageActivity;
import com.caribou.yaweapp.ListUsersActivity;
import com.caribou.yaweapp.MainActivity;
import com.caribou.yaweapp.R;

public class AdminFragment extends Fragment {

    private Button btCreateUser;
    private Button btCreateEvent;
    private Button btListUser;
    private Button btEditMessage;

    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        btCreateUser = (Button) v.findViewById(R.id.bt_main_createUser);
        btCreateEvent = (Button) v.findViewById(R.id.bt_main_createEvent);
        btListUser = (Button) v.findViewById(R.id.bt_main_listUser);
        btEditMessage = (Button) v.findViewById(R.id.bt_main_editMessage);

        btEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goEditMessage = new Intent(getContext(), EditGuildMessageActivity.class);
                getContext().startActivity(goEditMessage);
            }
        });

        btCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goCreateUser = new Intent(getContext(), CreateUserActivity.class);
                startActivity(goCreateUser);
            }
        });

        btCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goCreateEvent = new Intent(getContext(), CreateEventActivity.class);
                startActivity(goCreateEvent);
            }
        });

        btListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goListUsers = new Intent(getContext(), ListUsersActivity.class);
                startActivity(goListUsers);
            }
        });

        return v;
    }

}

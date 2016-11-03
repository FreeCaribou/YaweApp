package com.caribou.yaweapp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.LoginActivity;
import com.caribou.yaweapp.MainActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.task.GetAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment implements GetAsyncTask.GetAsyncTaskCallback{

    // TODO add message guild and list of usefull url

    TextView tvGuildMessage;
    Button btForum;
    Button btWiki;
    Button btMetaBattle;

    private HomeFragmentCallback callback;

    public void setCallback(HomeFragmentCallback callback) {
        this.callback = callback;
    }


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tvGuildMessage = (TextView) v.findViewById(R.id.tv_home_guildMessage);

        GetAsyncTask task = new GetAsyncTask(HomeFragment.this);
        task.execute(ListOfApiUrl.getUrlGuildMessage());

        btForum = (Button) v.findViewById(R.id.bt_home_forum);
        btWiki = (Button) v.findViewById(R.id.bt_home_wiki);
        btMetaBattle = (Button) v.findViewById(R.id.bt_home_metabattle);

        btForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToWeb(ListOfApiUrl.getUrlForum());
            }
        });

        btMetaBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToWeb(ListOfApiUrl.getUrlMetabattle());
            }
        });

        btWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToWeb(ListOfApiUrl.getUrlWikiGW2());
            }
        });

        return v;
    }

    public void intentToWeb(String url){
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            String jText = jsonObject.getString("text");
            tvGuildMessage.setText(jText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface HomeFragmentCallback{
        void intentCreatePicture();
        void intentMyAccount();
        void intentListEvents();
    }

}

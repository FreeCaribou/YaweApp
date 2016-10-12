package com.caribou.yaweapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.adapter.MemberArrayAdapter;
import com.caribou.yaweapp.adapter.PictureArrayAdapter;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.model.UserDetail;
import com.caribou.yaweapp.task.GetAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment implements GetAsyncTask.GetAsyncTaskCallback {

    ListView lvMember;
    ArrayList<UserDetail> listUserDetail;

    private MemberFragmentCallback callback;

    public void setCallback(MemberFragmentCallback callback) {
        this.callback = callback;
    }


    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_member, container, false);
        lvMember = (ListView) v.findViewById(R.id.lv_member_list);
        listUserDetail = new ArrayList<>();

        registerForContextMenu(lvMember);

        return v;
    }

    private void updateListView(){
        GetAsyncTask task = new GetAsyncTask(MemberFragment.this);
        task.execute(ListOfApiUrl.getUrlAllUserDetail());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            updateListView();
        }
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listUserDetail.removeAll(listUserDetail);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jPicture = jResponse.getJSONObject(i);
                long id = jPicture.getLong("id");
                String name = jPicture.getString("name");
                String gameplaystyle = jPicture.getString("gameplay_style");
                String profession = jPicture.getString("profession");
                String race = jPicture.getString("race");
                String mood = jPicture.getString("mood");
                String activity = jPicture.getString("activity");
                UserDetail ud = new UserDetail(id,race,profession,activity,gameplaystyle,mood,name);
                listUserDetail.add(ud);
            }

            MemberArrayAdapter adapter = new MemberArrayAdapter(getContext(), listUserDetail);
            lvMember.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface MemberFragmentCallback{

    }

}

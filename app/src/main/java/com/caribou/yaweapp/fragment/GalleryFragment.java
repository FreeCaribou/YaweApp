package com.caribou.yaweapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.CreatePictureActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.adapter.PictureArrayAdapter;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class GalleryFragment extends Fragment implements GetAsyncTask.GetAsyncTaskCallback{

    ListView lvListPictures;
    ArrayList<Picture> listPicture;
    FloatingActionButton fab;

    private GalleryFragmentCallback callback;

    public void setCallback(GalleryFragmentCallback callback) {
        this.callback = callback;
    }

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        lvListPictures = (ListView) v.findViewById(R.id.lv_gallery_listGallery);
        listPicture = new ArrayList<>();

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goCreate = new Intent(getContext(), CreatePictureActivity.class);
                getActivity().startActivity(goCreate);
            }
        });

        registerForContextMenu(lvListPictures);
        return v;
    }

    private void updateListView(){
        GetAsyncTask task = new GetAsyncTask(GalleryFragment.this);
        task.execute(ListOfApiUrl.getUrlAllPicture());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            // pour afficher des trucs seulement quand on rentre dans l'onglet
            updateListView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       // setUserVisibleHint(true);
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listPicture.removeAll(listPicture);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jPicture = jResponse.getJSONObject(i);
                long id = jPicture.getLong("id");
                String url = jPicture.getString("url");
                String title = jPicture.getString("title");
                String description = jPicture.getString("description");
                long id_user = jPicture.getLong("id_user");
                Picture p = new Picture(id, url, title, description, id_user);
                listPicture.add(p);
            }

            PictureArrayAdapter adapter = new PictureArrayAdapter(getContext(), listPicture);
            lvListPictures.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface GalleryFragmentCallback{

    }

}

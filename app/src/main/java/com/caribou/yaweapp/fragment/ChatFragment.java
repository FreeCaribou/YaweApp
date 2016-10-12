package com.caribou.yaweapp.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.PictureDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.adapter.ChatMessageArrayAdapter;
import com.caribou.yaweapp.adapter.CommentArrayAdapter;
import com.caribou.yaweapp.model.ChatMessage;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.PostChatMessageAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class ChatFragment extends Fragment implements GetAsyncTask.GetAsyncTaskCallback {

    Button btSend;
    EditText edMessage;
    ListView lvChatMessage;
    ArrayList<ChatMessage> listMessage;
    Timer timer;
    TimerTask tt;



    private GalleryFragment.GalleryFragmentCallback callback;

    public void setCallback(GalleryFragment.GalleryFragmentCallback callback) {
        this.callback = callback;
    }


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvChatMessage = (ListView) v.findViewById(R.id.lv_chat_messages);
        listMessage = new ArrayList<>();

        btSend = (Button) v.findViewById(R.id.bt_chat_post);
        edMessage = (EditText) v.findViewById(R.id.ed_chat_message);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edMessage.getText().toString();
                if(message.equals("")){
                    Toast.makeText(getContext(), "Your message is empty...", Toast.LENGTH_SHORT).show();
                } else if(message.length() >= 200) {
                    Toast.makeText(getContext(), "Not more than 200 characters please", Toast.LENGTH_SHORT).show();
                } else {

                    ChatMessage cm = new ChatMessage();
                    cm.setText(message);
                    cm.setPostDate(Calendar.getInstance().getTime());
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    long idUser = prefs.getLong("id_user", 0);
                    cm.setId_user(idUser);

                    PostChatMessageAsyncTask task = new PostChatMessageAsyncTask();
                    task.execute(cm);

                    updateListView();
                    edMessage.setText("");

                }

            }
        });
        registerForContextMenu(lvChatMessage);



        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        // don't need to refresh the chat if we close the fragment
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        // don't need to refresh the chat if we close the fragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        // don't need to refresh the chat if we close the fragment
    }

    private void updateListView(){
        GetAsyncTask task = new GetAsyncTask(ChatFragment.this);
        task.execute(ListOfApiUrl.getUrlAllChatMessage());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            // pour afficher des trucs seulement quand on rentre dans l'onglet
            updateListView();

            timer = new Timer();
            tt = new TimerTask()
            {
                @Override
                public void run()
                {
                    updateListView();
                    Log.i("sbire: ", "je refresh");
                    // TODO stopper le refresh en sortant de la partie tchat
                }
            };
            timer.scheduleAtFixedRate(tt,5000,2500);  // Delay 5 seconds on the first run
            // then run every 5 second
        } else {

        }
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {

        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listMessage.removeAll(listMessage);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("postDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String text = jEvent.getString("text");
                long id_user = jEvent.getLong("id_user");
                Date date = new Date(d.getTime());
                String author_name = jEvent.getString("name");
                ChatMessage ncm = new ChatMessage(id, text, date, id_user, author_name);
                Log.i("toString cp: ", ncm.toString());

                listMessage.add(ncm);
            }

            ChatMessageArrayAdapter adapter = new ChatMessageArrayAdapter(getContext(), listMessage);
            lvChatMessage.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

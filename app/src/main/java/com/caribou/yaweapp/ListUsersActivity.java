package com.caribou.yaweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.UserArrayAdapter;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback{

    ListView lvListUsers;
    ArrayList<User> listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        lvListUsers = (ListView) findViewById(R.id.lv_listUser_list);
        listUsers = new ArrayList<>();
        registerForContextMenu(lvListUsers);
        updateListView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView(){
        GetAsyncTask task = new GetAsyncTask(ListUsersActivity.this);
        task.execute(ListOfApiUrl.getUrlAllUser());
    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listUsers.removeAll(listUsers);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jUser = jResponse.getJSONObject(i);
                long id = jUser.getLong("id");
                String name = jUser.getString("name");
                String password = jUser.getString("password");
                // TODO verifier si heretic ou admin (mais pour le moment osef)
                int admin = jUser.getInt("admin");
                int heretic = jUser.getInt("heretic");
                User u = new User();
                u.setId(id);
                u.setPassword(password);
                u.setName(name);
                if(admin == 1){
                    u.setAdmin(true);
                } else {
                    u.setAdmin(false);
                }
                if(heretic == 1){
                    u.setHeretic(true);
                } else {
                    u.setHeretic(false);
                }
                listUsers.add(u);
            }

            UserArrayAdapter adapter = new UserArrayAdapter(ListUsersActivity.this, listUsers);
            lvListUsers.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

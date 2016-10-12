package com.caribou.yaweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.fragment.HomeFragment;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.UpdateMessageAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class EditGuildMessageActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    private EditText editText;
    private Button btEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guild_message);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editText = (EditText) findViewById(R.id.ed_editMessage_edit);
        btEdit = (Button) findViewById(R.id.bt_editMessage_edit);

        GetAsyncTask task = new GetAsyncTask(EditGuildMessageActivity.this);
        task.execute(ListOfApiUrl.getUrlGuildMessage());

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateMessageAsyncTask task = new UpdateMessageAsyncTask();
                task.execute(editText.getText().toString());
                finish();
            }
        });

    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONObject jsonObject = new JSONObject(sJSON);
            String jText = jsonObject.getString("text");
            editText.setText(jText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

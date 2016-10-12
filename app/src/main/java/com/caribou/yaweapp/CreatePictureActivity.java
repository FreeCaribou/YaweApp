package com.caribou.yaweapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.task.PostPictureAsyncTask;

public class CreatePictureActivity extends AppCompatActivity {

    EditText edTitle;
    EditText edDescription;
    EditText edUrl;
    Button btCreate;
    Button btUploadByWeb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_picture);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edTitle = (EditText) findViewById(R.id.ed_createPicture_title);
        edDescription = (EditText) findViewById(R.id.ed_createPicture_description);
        edUrl = (EditText) findViewById(R.id.ed_createPicture_url);
        btCreate = (Button) findViewById(R.id.bt_createPicture_createPicture);
        btUploadByWeb = (Button) findViewById(R.id.bt_createPicture_goToWebSite);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreatePictureActivity.this);
        final long idUser = prefs.getLong("id_user", 0);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picture p = new Picture();
                p.setTitle(edTitle.getText().toString());
                p.setDescription(edDescription.getText().toString());
                p.setUrl(edUrl.getText().toString());
                p.setId_user(idUser);
                PostPictureAsyncTask task = new PostPictureAsyncTask();
                task.execute(p);
                finish();
            }
        });

        btUploadByWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(ListOfApiUrl.getUrlUploadPicture());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}

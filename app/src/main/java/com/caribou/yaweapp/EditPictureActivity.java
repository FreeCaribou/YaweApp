package com.caribou.yaweapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.caribou.yaweapp.model.Picture;
import com.caribou.yaweapp.task.UpdatePictureAsyncTask;

public class EditPictureActivity extends AppCompatActivity {

    Button btEdit;
    EditText edTitle;
    EditText edUrl;
    EditText edDescription;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btEdit = (Button) findViewById(R.id.bt_pictureDetail_edit);
        edDescription = (EditText) findViewById(R.id.ed_pictureDetail_description);
        edTitle = (EditText) findViewById(R.id.ed_pictureDetail_title);
        edUrl = (EditText) findViewById(R.id.ed_pictureDetail_url);

        final Bundle extra = this.getIntent().getExtras();
        if(extra != null){
            id = extra.getLong("id_picture");
            setTitle(extra.getString("title_picture"));

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditPictureActivity.this);

            long idAuthor = prefs.getLong("id_user", 0);

            edUrl.setText(extra.getString("url_picture"));
            edDescription.setText(extra.getString("description_picture"));
            edTitle.setText(extra.getString("title_picture"));

            String url = extra.getString("url_picture");

        }

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picture p = new Picture(id,edUrl.getText().toString(),edTitle.getText().toString(),edDescription.getText().toString(), 0);
                UpdatePictureAsyncTask task = new UpdatePictureAsyncTask();
                task.execute(p);
                Toast.makeText(EditPictureActivity.this, R.string.picture_edit, Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}

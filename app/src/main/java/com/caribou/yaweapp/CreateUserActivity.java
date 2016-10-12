package com.caribou.yaweapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.task.PostUserAsyncTask;

public class CreateUserActivity extends AppCompatActivity {

    EditText edUsername;
    EditText edPassword;
    CheckBox cbAdmin;
    CheckBox cbHeretic;
    Button btCreate;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edUsername = (EditText) findViewById(R.id.ed_createUser_username);
        edPassword = (EditText) findViewById(R.id.ed_createUser_password);
        cbAdmin = (CheckBox) findViewById(R.id.cb_createUser_isAdmin);
        cbHeretic = (CheckBox) findViewById(R.id.cb_createUser_isHeretic);
        btCreate = (Button) findViewById(R.id.bt_createUser_create);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edUsername.getText().toString().equals("")){
                    Toast.makeText(CreateUserActivity.this, R.string.put_username, Toast.LENGTH_SHORT).show();
                } else if (edPassword.getText().toString().equals("")){
                    Toast.makeText(CreateUserActivity.this, R.string.put_password, Toast.LENGTH_SHORT).show();
                } else {
                    username = edUsername.getText().toString();
                    String password = edPassword.getText().toString();
                    byte admin = 0;
                    byte heretic = 0;
                    Boolean bAdmin = false;
                    Boolean bHeretic = false;

                    if(cbAdmin.isChecked()){
                        admin = 1;
                        bAdmin = true;
                    }
                    if(cbHeretic.isChecked()){
                        heretic = 1;
                        bHeretic = true;
                    }

                    User u = new User(0,username,password,bAdmin,bHeretic);
                    PostUserAsyncTask task = new PostUserAsyncTask();
                    task.execute(u);
                    finish();
                }
            }
        });



    }

}

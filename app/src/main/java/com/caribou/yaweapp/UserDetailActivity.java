package com.caribou.yaweapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.task.DeleteAsyncTask;
import com.caribou.yaweapp.task.UpdateUserAdminAsynckTask;
import com.caribou.yaweapp.task.UpdateUserHereticAsynckTask;

public class UserDetailActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvId;
    TextView tvAdmin;
    TextView tvHeretic;
    Button btAdmin;
    Button btHeretic;
    Button btDelete;
    Long id;
    boolean isAdmin;
    boolean isHeretic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        id = Long.valueOf(0);

        tvName = (TextView) findViewById(R.id.tv_userDetail_name);
        tvId = (TextView) findViewById(R.id.tv_userDetail_id);
        btDelete = (Button) findViewById(R.id.bt_userDetail_delete);
        btAdmin = (Button) findViewById(R.id.bt_userDetail_changeAdmin);
        btHeretic = (Button) findViewById(R.id.bt_userDetail_changeHeretic);
        tvAdmin = (TextView) findViewById(R.id.tv_userDetail_isAdmin);
        tvHeretic = (TextView) findViewById(R.id.tv_userDetail_isHeretic);

        Bundle extra = this.getIntent().getExtras();
        if(extra != null){
            id = extra.getLong("id");
            tvName.setText(extra.getString("name"));
            tvId.setText(String.valueOf(id));
            isAdmin = extra.getBoolean("isAdmin");
            isHeretic = extra.getBoolean("isHeretic");
            if(isHeretic){
                tvHeretic.setText(R.string.he_is_heretic);
                btHeretic.setText(R.string.change_to_not_here);
            } else {
                tvHeretic.setText(R.string.he_is_not_heretic);
                btHeretic.setText(R.string.change_to_here);
            }
            if(isAdmin){
                tvAdmin.setText(R.string.he_is_admin);
                btAdmin.setText(R.string.change_to_not_admin);
            } else {
                tvAdmin.setText(R.string.he_is_not_admin);
                btAdmin.setText(R.string.change_to_admin);
            }

            final User u = new User();
            u.setAdmin(isAdmin);
            u.setHeretic(isHeretic);
            u.setId(id);


            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(UserDetailActivity.this);
                    alert.setTitle(R.string.are_you_sur);
                    alert.setMessage("You are going to delete this member!");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DeleteAsyncTask delete = new DeleteAsyncTask();
                            delete.execute(ListOfApiUrl.getUrlDeleteUser(String.valueOf(id)));
                            finish();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UserDetailActivity.this, "User not deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog show = alert.create();
                    show.show();
                }
            });

            btAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String alertMessage = "";
                    if (isAdmin){
                        u.setAdmin(false);
                        alertMessage = "You are going to remove admin power to this user!";
                    } else {
                        u.setAdmin(true);
                        alertMessage = "You are going to give admin power to this user!";
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(UserDetailActivity.this);
                    alert.setTitle(R.string.are_you_sur);
                    alert.setMessage(alertMessage);

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateUserAdminAsynckTask task = new UpdateUserAdminAsynckTask();
                            task.execute(u);
                            finish();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(UserDetailActivity.this, "Member not changed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog show = alert.create();
                    show.show();


                }
            });

            btHeretic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isHeretic){
                        u.setHeretic(false);
                    } else {
                        u.setHeretic(true);
                    }
                    UpdateUserHereticAsynckTask task = new UpdateUserHereticAsynckTask();
                    task.execute(u);
                    finish();
                }
            });



        }


    }
}

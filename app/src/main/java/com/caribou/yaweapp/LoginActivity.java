package com.caribou.yaweapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.task.GetAsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    Button btLogin;
    EditText edUsername;
    EditText edPassword;
    CheckBox cbRemember;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ConnectivityManager connMgr = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        edPassword = (EditText) findViewById(R.id.ed_login_password);
        edUsername = (EditText) findViewById(R.id.ed_login_username);
        btLogin = (Button) findViewById(R.id.bt_login_login);
        cbRemember = (CheckBox) findViewById(R.id.cb_login_remember);

        SharedPreferences loginPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        edUsername.setText(loginPref.getString("loginUsername", ""));
        edPassword.setText(loginPref.getString("loginPassword", ""));
        cbRemember.setChecked(loginPref.getBoolean("loginCheckRemember", false));

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("loginUsername", username );

                if(wifi.isConnected() || mobile.isConnected()) {
                    username = edUsername.getText().toString();
                    password = edPassword.getText().toString();
                    if(mobile.isConnected()){
                        Toast.makeText(LoginActivity.this, R.string.carefull_with_mobile_data, Toast.LENGTH_SHORT).show();
                    }
                    GetAsyncTask task = new GetAsyncTask(LoginActivity.this);
                    task.execute(ListOfApiUrl.getUrlUserByName(username));

                    if(cbRemember.isChecked()){

                        editor.putString("loginUsername", username );
                        editor.putString("loginPassword", password );
                        editor.putBoolean("loginCheckRemember", true);
                        editor.apply();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, R.string.you_dont_have_internet, Toast.LENGTH_SHORT).show();
                }
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
            String jPassword = jsonObject.getString("password");
            String jUsername = jsonObject.getString("name");
            long jId = jsonObject.getLong("id");
            Boolean jAdmin;
            Boolean jHeretic;
            jAdmin = jsonObject.getInt("admin") == 1;
            jHeretic = jsonObject.getInt("heretic") == 1;
            if (jPassword.equals(password)) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("id_user", jId);
                editor.putBoolean("isAdmin", jAdmin);
                editor.putBoolean("isHeretic", jHeretic);
                editor.putString("username", jUsername);
                editor.apply();
                Intent goLogin = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(goLogin);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, R.string.bad_name_password, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
}

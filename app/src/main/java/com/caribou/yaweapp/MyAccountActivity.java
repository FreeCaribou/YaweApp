package com.caribou.yaweapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.SpinnerAdapter;
import com.caribou.yaweapp.model.ItemData;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.model.UserDetail;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.UpdateUserDetailAsyncTask;
import com.caribou.yaweapp.task.UpdateUserPasswordAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    String race;
    String profession;
    TextView tvName;
    Button btChangePassword;
    EditText edPassword;
    EditText edConfirmPassword;
    EditText edGameplaystyle;
    EditText edMood;
    EditText edActivity;
    Button btUpdate;
    Spinner spRace;
    Spinner spProfession;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        race = "";
        profession = "";
        edMood = (EditText) findViewById(R.id.ed_account_mood);
        edActivity = (EditText) findViewById(R.id.ed_account_activity);
        edGameplaystyle = (EditText) findViewById(R.id.ed_account_gameplay);
        btUpdate = (Button) findViewById(R.id.bt_account_updateDetail);
        spRace = (Spinner) findViewById(R.id.sp_account_race);
        spProfession = (Spinner) findViewById(R.id.sp_account_profession);

        spRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ItemData ni = (ItemData) parent.getItemAtPosition(position);
                if (position > 0) {
                    race = ni.getText();
                    Log.i("change", "the race");
                }
                Log.i("race: ", ni.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Select a race", R.drawable.gw2_icon));
        list.add(new ItemData("Charr", R.drawable.charr_icon));
        list.add(new ItemData("Asura", R.drawable.asura_icon));
        list.add(new ItemData("Norn", R.drawable.norn_icon));
        list.add(new ItemData("Human", R.drawable.human_icon));
        list.add(new ItemData("Sylvari", R.drawable.sylvari_icon));

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_item_textandicon, R.id.tv_spinner, list);
        spRace.setAdapter(adapter);

        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ItemData ni = (ItemData) parent.getItemAtPosition(position);
                if (position > 0) {
                    profession = ni.getText();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayList<ItemData> listP = new ArrayList<>();
        listP.add(new ItemData("Select a profession", R.drawable.gw2_icon));
        listP.add(new ItemData("Mesmer", R.drawable.mesmer_icon));
        listP.add(new ItemData("Guardian", R.drawable.guardian_icon));
        listP.add(new ItemData("Necromancer", R.drawable.necro_icon));
        listP.add(new ItemData("Ranger", R.drawable.ranger_icon));
        listP.add(new ItemData("Elementalist", R.drawable.elem_icon));
        listP.add(new ItemData("Warrior", R.drawable.warrior_icon));
        listP.add(new ItemData("Thief", R.drawable.thief_icon));
        listP.add(new ItemData("Engineer", R.drawable.engineer_icon));
        listP.add(new ItemData("Revenant", R.drawable.revenant_icon));


        SpinnerAdapter adapterP = new SpinnerAdapter(this, R.layout.spinner_item_textandicon, R.id.tv_spinner, listP);
        spProfession.setAdapter(adapterP);


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyAccountActivity.this);
        String username = prefs.getString("username", "unknown");
        id = prefs.getLong("id_user", 0);

        GetAsyncTask task = new GetAsyncTask(MyAccountActivity.this);
        task.execute(ListOfApiUrl.getUrlUserDetailById(String.valueOf(id)));

        tvName = (TextView) findViewById(R.id.tv_account_name);
        btChangePassword = (Button) findViewById(R.id.bt_account_changePassword);
        edPassword = (EditText) findViewById(R.id.ed_account_newPassword);
        edConfirmPassword = (EditText) findViewById(R.id.ed_account_ConfirmNewPassword);

        String message = getResources().getString(R.string.welcome_to_your_account);

        tvName.setText(message + " " + username);

        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = edPassword.getText().toString();
                String confirmPass = edConfirmPassword.getText().toString();

                if (!pass.equals("")) {
                    if (pass.equals(confirmPass)) {
                        User u = new User(id, "name", pass, false, false);
                        UpdateUserPasswordAsyncTask task = new UpdateUserPasswordAsyncTask();
                        task.execute(u);
                        Toast.makeText(MyAccountActivity.this, "Password change", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyAccountActivity.this, "Passwords isn't the same", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyAccountActivity.this, "Put a new password please", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail ud = new UserDetail();
                ud.setFavoriteActiviy(edActivity.getText().toString());
                ud.setFavoriteRace(race);
                ud.setFavoriteProfession(profession);
                ud.setGamePlayStyle(edGameplaystyle.getText().toString());
                ud.setMood(edMood.getText().toString());
                ud.setId(id);

                UpdateUserDetailAsyncTask task = new UpdateUserDetailAsyncTask();
                task.execute(ud);

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
            Log.i("json: ", sJSON);
            String jRace = jsonObject.getString("race");
            String jProfession = jsonObject.getString("profession");
            String jActivity = jsonObject.getString("activity");
            String jMood = jsonObject.getString("mood");
            String jGameplay = jsonObject.getString("gameplay_style");

            edActivity.setText(jActivity);
            edGameplaystyle.setText(jGameplay);
            edMood.setText(jMood);

            race = jRace;
            int position = 0;
            if (race.equals("Charr")) {
                position = 1;
            } else if (race.equals("Asura")) {
                position = 2;
            } else if (race.equals("Norn")) {
                position = 3;
            } else if (race.equals("Human")) {
                position = 4;
            } else if (race.equals("Sylvari")) {
                position = 5;
            }
            spRace.setSelection(position);

            profession = jProfession;
            int positionP = 0;
            switch (profession) {
                case "Mesmer":
                    positionP = 1;
                    break;
                case "Guardian":
                    positionP = 2;
                    break;
                case "Necromancer":
                    positionP = 3;
                    break;
                case "Ranger":
                    positionP = 4;
                    break;
                case "Elementalist":
                    positionP = 5;
                    break;
                case "Warrior":
                    positionP = 6;
                    break;
                case "Thief":
                    positionP = 7;
                    break;
                case "Engineer":
                    positionP = 8;
                    break;
                case "Revenant":
                    positionP = 9;
                    break;
                default:
                    positionP = 0;
                    break;
            }

            spProfession.setSelection(positionP);


        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}

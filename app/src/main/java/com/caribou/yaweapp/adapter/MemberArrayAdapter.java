package com.caribou.yaweapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caribou.yaweapp.MemberDetailActivity;
import com.caribou.yaweapp.R;
import com.caribou.yaweapp.UserDetailActivity;
import com.caribou.yaweapp.model.User;
import com.caribou.yaweapp.model.UserDetail;

import java.util.ArrayList;

public class MemberArrayAdapter extends ArrayAdapter<UserDetail> {

    public MemberArrayAdapter(Context context, ArrayList<UserDetail> userDetails) {
        super(context, 0, userDetails);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final UserDetail u = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_memberItem_name);
        ImageView tvRace = (ImageView) convertView.findViewById(R.id.tv_memberItem_race);
        ImageView tvProfession = (ImageView) convertView.findViewById(R.id.tv_memberItem_profession);

        tvName.setText(u.getAuthorName());
        final String race = u.getFavoriteRace();
        switch (race){
            case "Charr":
                tvRace.setImageResource(R.drawable.charr_icon);
                break;
            case "Asura":
                tvRace.setImageResource(R.drawable.asura_icon);
                break;
            case "Norn":
                tvRace.setImageResource(R.drawable.norn_icon);
                break;
            case "Human":
                tvRace.setImageResource(R.drawable.human_icon);
                break;
            case "Sylvari":
                tvRace.setImageResource(R.drawable.sylvari_icon);
                break;

        }
        final String profession = u.getFavoriteProfession();
        switch (profession) {
            case "Mesmer":
                tvProfession.setImageResource(R.drawable.mesmer_icon);
                break;
            case "Guardian":
                tvProfession.setImageResource(R.drawable.guardian_icon);
                break;
            case "Necromancer":
                tvProfession.setImageResource(R.drawable.necro_icon);
                break;
            case "Ranger":
                tvProfession.setImageResource(R.drawable.ranger_icon);
                break;
            case "Elementalist":
                tvProfession.setImageResource(R.drawable.elem_icon);
                break;
            case "Warrior":
                tvProfession.setImageResource(R.drawable.warrior_icon);
                break;
            case "Thief":
                tvProfession.setImageResource(R.drawable.thief_icon);
                break;
            case "Engineer":
                tvProfession.setImageResource(R.drawable.engineer_icon);
                break;
            case "Revenant":
                tvProfession.setImageResource(R.drawable.revenant_icon);
                break;
            default:
                break;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetail = new Intent(getContext(), MemberDetailActivity.class);
                goToDetail.putExtra("race", race);
                goToDetail.putExtra("profession", profession);
                goToDetail.putExtra("id", u.getId());
                goToDetail.putExtra("mood", u.getMood());
                goToDetail.putExtra("gameplay_style", u.getGamePlayStyle());
                goToDetail.putExtra("activity", u.getFavoriteActiviy());
                goToDetail.putExtra("name", u.getAuthorName());
                getContext().startActivity(goToDetail);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}

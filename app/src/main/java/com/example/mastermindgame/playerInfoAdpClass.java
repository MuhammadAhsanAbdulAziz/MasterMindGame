package com.example.mastermindgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class playerInfoAdpClass extends BaseAdapter {
    Context context;
    ArrayList<playerInfoClass> playerInfoArrayList = new ArrayList<>();

    public playerInfoAdpClass(Context context, ArrayList<playerInfoClass> playerInfoArrayList) {
        this.context = context;
        this.playerInfoArrayList = playerInfoArrayList;
    }

    @Override
    public int getCount() {
        return playerInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView Tname;
        TextView Teasy, Tmedium, Thard;
        ImageButton Tlogoutbtn;
        convertView = LayoutInflater.from(context).inflate(R.layout.userdetailpage, null);

        Tname = convertView.findViewById(R.id.nameview);
//        Temail = convertView.findViewById(R.id.emailview);
        Teasy = convertView.findViewById(R.id.easyscore);
        Tmedium = convertView.findViewById(R.id.mediumscore);
        Thard = convertView.findViewById(R.id.hardscore);
        imageView = convertView.findViewById(R.id.imgview);
        Tlogoutbtn = convertView.findViewById(R.id.logoutbtn);


        Tname.setText(playerInfoArrayList.get(position).users_name);
        Teasy.setText(String.valueOf(playerInfoArrayList.get(position).getEasyScore())+" pts");
        Tmedium.setText(String.valueOf(playerInfoArrayList.get(position).getMediumScore())+" pts");
        Thard.setText(String.valueOf(playerInfoArrayList.get(position).getHardScore())+" pts");
        Glide.with(context).load(playerInfoArrayList.get(position).users_uri)
                .error(R.drawable.ic_person).into(imageView);
        Tlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Login.class);
                view.getContext().startActivity(intent);
                ((Activity)context).finish();
                setDefaults("userId",null, view.getContext());
                setDefaults("Email",null,view.getContext());
                setDefaults("Password",null,view.getContext());
                setDefaults("Difficulty",null,view.getContext());
            }
        });
        return convertView;
    }
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

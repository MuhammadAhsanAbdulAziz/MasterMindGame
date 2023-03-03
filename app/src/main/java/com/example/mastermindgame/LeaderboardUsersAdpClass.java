package com.example.mastermindgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class LeaderboardUsersAdpClass extends RecyclerView.Adapter<LeaderboardUsersAdpClass.ViewHolder> {

    Context context;
    ArrayList<leaderboardUsersClass> leaderboardUsers = new ArrayList<>();
    int Count = 1;


    public LeaderboardUsersAdpClass(Context context, ArrayList<leaderboardUsersClass> leaderboardUsers) {
        this.context = context;
        this.leaderboardUsers = leaderboardUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.leaderboarduserlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String ProfilePicture = leaderboardUsers.get(position).UserProfilePicture;
        String Name = leaderboardUsers.get(position).UserName;
        long Score = leaderboardUsers.get(position).UserScore;


        holder.setData(ProfilePicture, Name, Score,Count);
        Count++;
    }

    @Override
    public int getItemCount() {
        return leaderboardUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView Tname, Tscore,Tnumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Tname = itemView.findViewById(R.id.usernameview);
            Tscore = itemView.findViewById(R.id.userScore);
            imageView = itemView.findViewById(R.id.userimgview);
            Tnumber = itemView.findViewById(R.id.usernumber);
        }

        public void setData(String ProfilePicture, String Name, long Score,int Count) {
            Tnumber.setText(""+Count);
            Tname.setText(Name);
            Tscore.setText(""+Score);
            Glide.with(context).load(ProfilePicture).error(R.drawable.ic_person).dontAnimate().into(imageView);

        }

    }
}

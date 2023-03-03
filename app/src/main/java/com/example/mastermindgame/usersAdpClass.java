package com.example.mastermindgame;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class usersAdpClass extends RecyclerView.Adapter<usersAdpClass.ViewHolder> {
    Context context;
    ArrayList<UsersClass> usersArrayList = new ArrayList<>();

    public usersAdpClass(Context context, ArrayList<UsersClass> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public usersAdpClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull usersAdpClass.ViewHolder holder, int position) {
        String Email = usersArrayList.get(position).userEmail;
        String Name = usersArrayList.get(position).userName;
        String Picture = usersArrayList.get(position).userPicture;
        String UserId = usersArrayList.get(position).userId;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UsersCredentials.class);
                intent.putExtra("Id",UserId);
                view.getContext().startActivity(intent);
            }
        });

        holder.setData(Name, Email, Picture);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView Tname, Temail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Tname = itemView.findViewById(R.id.usernameview);
            Temail = itemView.findViewById(R.id.useremailview);
            imageView = itemView.findViewById(R.id.userimgview);
        }

        public void setData(String name, String email, String image) {
            Tname.setText(name);
            Temail.setText(email);
            Glide.with(context).load(image).error(R.drawable.ic_person).dontAnimate().into(imageView);

        }
    }
}

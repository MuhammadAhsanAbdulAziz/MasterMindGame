package com.example.mastermindgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MediumFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<leaderboardUsersClass> usersArrayList = new ArrayList<>();
    LeaderboardUsersAdpClass adp;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_medium, container, false);
        recyclerView = view.findViewById(R.id.leadermedium);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        showDatainListView();
//        sort();
        usersArrayList.clear();
        showlist();
        return view;
    }

    void showlist() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adp = new LeaderboardUsersAdpClass(getContext(), usersArrayList);
        recyclerView.setAdapter(adp);
    }

    public void showDatainListView() {
        databaseReference.orderByChild("Medium Score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.child("Email").getValue(String.class).equals("maahsan36@gmail.com"))
                    {
                        continue;
                    }
                    String pic = ds.child("Profile Picture").getValue(String.class);
                    String name = ds.child("Name").getValue(String.class);
                    long score = (long) ds.child("Medium Score").getValue();
                    usersArrayList.add(new leaderboardUsersClass(pic,name,score));
                    adp.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
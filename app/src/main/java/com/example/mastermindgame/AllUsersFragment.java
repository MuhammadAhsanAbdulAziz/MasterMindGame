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


public class AllUsersFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<UsersClass> usersArrayList = new ArrayList<>();
    usersAdpClass adp;
    DatabaseReference databaseReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
        recyclerView = view.findViewById(R.id.alluserlist);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        showDatainListView();
        showlist();
        return view;
    }

    void showlist() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adp = new usersAdpClass(getContext(), usersArrayList);
        recyclerView.setAdapter(adp);
    }

    public void showDatainListView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    usersArrayList.add(new UsersClass(ds.child("Name").getValue(String.class), ds.child("Profile Picture").getValue(String.class), ds.child("Email").getValue(String.class),ds.getKey()));
                    adp.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}
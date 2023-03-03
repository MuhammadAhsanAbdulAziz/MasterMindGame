package com.example.mastermindgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Leaderboard extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SimpleFragmentPageAdapter adapter = new SimpleFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

}
package com.example.mastermindgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    Button playbtn, addwordsbtn, alluserbtn, quibtn,leaderboardbtn,helpbtn;
    ImageButton optionsbtn, userbtn;
    DatabaseReference databaseReference;
    ArrayList<playerInfoClass> usersArrayList = new ArrayList<>();
    CardView mainmenuitems;
    playerInfoAdpClass adp;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        playbtn = findViewById(R.id.playbtn);
        optionsbtn = findViewById(R.id.optionsbtn);
        addwordsbtn = findViewById(R.id.addwordsbtn);
        quibtn = findViewById(R.id.quitbtn);
        userbtn = findViewById(R.id.userbtn);
        mainmenuitems = findViewById(R.id.mainmenuitems);
        alluserbtn = findViewById(R.id.alluserbtn);
        leaderboardbtn = findViewById(R.id.leaderboardbtn);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
        helpbtn = findViewById(R.id.helpbtn);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        alluserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changefragment(new AllUsersFragment());
                mainmenuitems.setVisibility(View.INVISIBLE);
            }
        });
        if (getDefaults("Email", this).equals("maahsan36@gmail.com")) {
            addwordsbtn.setVisibility(View.VISIBLE);
            alluserbtn.setVisibility(View.VISIBLE);
        }
        else
        {
            addwordsbtn.setVisibility(View.GONE);
            alluserbtn.setVisibility(View.GONE);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(getDefaults("userId", this));

        showDatainListView();


        playbtn.setOnClickListener(this);
        optionsbtn.setOnClickListener(this);
        addwordsbtn.setOnClickListener(this);
        quibtn.setOnClickListener(this);
        userbtn.setOnClickListener(this);
        leaderboardbtn.setOnClickListener(this);
        helpbtn.setOnClickListener(this);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playbtn:
                Intent intent = new Intent(this,Game.class);
                startActivity(intent);
            break;
            case R.id.helpbtn:
                helpDialog();
                break;
            case R.id.leaderboardbtn:
                Intent intent2 = new Intent(this,Leaderboard.class);
                startActivity(intent2);
                break;
            case R.id.optionsbtn:
                optionDialog();
                break;
            case R.id.addwordsbtn:
                changefragment(new WordCrudFragment());
                mainmenuitems.setVisibility(View.INVISIBLE);
                break;
//            case R.id.userbtn:
//                userDialog(this);
//                break;
            case R.id.quitbtn:
                System.exit(0);
                break;
        }
    }
//
//    public void userDialog(Activity activity) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogbox, viewGroup, false);
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.setView(dialogView);
//        adp = new playerInfoAdpClass(this, usersArrayList);
//        ListView listView = (ListView) dialogView.findViewById(R.id.userdetaillist);
//        listView.setAdapter(adp);
//        alertDialog.show();
//
//    }

    public void optionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_options, viewGroup, false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        RadioButton easyradiobtn = dialogView.findViewById(R.id.eeasyradiobtn);
        RadioButton mediumradiobtn = dialogView.findViewById(R.id.mmediumradiobtn);
        RadioButton hardradiobtn = dialogView.findViewById(R.id.hhardradiobtn);
        Button musicOnOff = dialogView.findViewById(R.id.musicOnOff);
        Button logoutbtn = dialogView.findViewById(R.id.logoutbtnn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                if(mediaPlayer!=null) {
                    mediaPlayer.pause();
                    mediaPlayer = null;
                }
                setDefaults("userId",null,getApplicationContext());
                setDefaults("Email",null,getApplicationContext());
                setDefaults("Password",null,getApplicationContext());
                setDefaults("Difficulty",null,getApplicationContext());
                finish();
            }
        });
        musicOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer!= null)
                {
                    mediaPlayer.pause();
                    mediaPlayer = null;
                }
                else
                {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
//                if(getDefaults("Music",getApplicationContext()).equals("OFF"))
//                {
//                    mediaPlayer.start();
//                }
//                else
//                {
//                    mediaPlayer.pause();
//                    setDefaults("Music","OFF",getApplicationContext());
//                }
            }
        });

        String currentDifficulty = getDefaults("Difficulty", this);
        if (currentDifficulty.equals("Hard")) {
            hardradiobtn.setChecked(true);
        } else if (currentDifficulty.equals("Medium")) {
            mediumradiobtn.setChecked(true);
        } else if (currentDifficulty.equals("Easy")) {
            easyradiobtn.setChecked(true);
        }

        easyradiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaults("Difficulty", "Easy", getApplicationContext());
            }
        });

        mediumradiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaults("Difficulty", "Medium", getApplicationContext());
            }
        });

        hardradiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaults("Difficulty", "Hard", getApplicationContext());
            }
        });
        alertDialog.show();
    }

    public void helpDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.helpdialog, viewGroup, false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void showDatainListView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                usersArrayList.add(new playerInfoClass(snapshot.child("Name").getValue(String.class),
                        snapshot.child("Profile Picture").getValue(String.class), (long) snapshot.child("Easy Score").getValue(),
                        (long) snapshot.child("Medium Score").getValue(), (long) snapshot.child("Hard Score").getValue()));
                Glide.with(getApplicationContext()).load(snapshot.child("Profile Picture").getValue(String.class)).error(R.drawable.ic_person).into(userbtn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getFragmentManager().popBackStack("wordscurdfrag", 0);
        mainmenuitems.setVisibility(View.VISIBLE);
    }

    public void changefragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.frag2, fragment);
        fragmentTransaction.addToBackStack("wordscurdfrag");
        fragmentTransaction.commit();
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
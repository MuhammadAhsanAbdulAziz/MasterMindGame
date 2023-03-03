package com.example.mastermindgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends AppCompatActivity {

    Button clrbtn, changebtn;
    ImageButton pauseBtn;
    RecyclerView recyclerView;
    ArrayList<letterClass> usersArrayList = new ArrayList<>();
    ArrayList<String> wordlist = new ArrayList<>();
    ArrayList<String> alreadyCheckedWords = new ArrayList<>();
    letterAdpClass adp;
    TextView wordbox, timerfield, scorefiled;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    char[] word;
    long maxCounter;
    long diff;
    CountDownTimer yourCountDownTimer;
    long START_TIME_IN_MILLIS = 600000;
    Boolean mTimerRunning;
    long mTimeLeftInMillis;
    String diffuclty;
    long score = 0L;
    String enteredUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        wordbox = findViewById(R.id.wordbox);
        timerfield = findViewById(R.id.timerfield);
        firebaseDatabase = FirebaseDatabase.getInstance();
        clrbtn = findViewById(R.id.clrbtn);
        scorefiled = findViewById(R.id.scorefiled);
        pauseBtn = findViewById(R.id.pauseBtn);
        changebtn = findViewById(R.id.changebtn);
        diffuclty = getDefaults("Difficulty", this);
        if(diffuclty.equals("Easy"))
        {
            START_TIME_IN_MILLIS = 121000;
        }
        else if(diffuclty.equals("Medium"))
        {
            START_TIME_IN_MILLIS = 61000;
        }
        else if(diffuclty.equals("Hard"))
        {
            START_TIME_IN_MILLIS = 31000;
        }
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        enteredUserId = getDefaults("userId", this);
        showDatainListView();
        showlist();
        startTimer();

        wordbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (wordlist.contains(wordbox.getText().toString())) {
                    wordbox.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            score = score + 10;
                            scorefiled.setText("");
                            scorefiled.setText("" + score);
                            wordbox.setText("");
                            usersArrayList.clear();
                            showDatainListView();
                            recyclerView.setAdapter(adp);
                        }
                    }, 1000);

                }
            }
        });
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordbox.setText("");
                usersArrayList.clear();
                showDatainListView();
                recyclerView.setAdapter(adp);

            }
        });
        clrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersArrayList.clear();
                for (char c : word) {
                    usersArrayList.add(new letterClass(Character.toString(c)));
                }
                wordbox.setText("");
                recyclerView.setAdapter(adp);
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseDialog();
            }
        });

    }

    void showlist() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        recyclerView = findViewById(R.id.mylist);
        recyclerView.setLayoutManager(layoutManager);
        adp = new letterAdpClass(Game.this, usersArrayList, new letterAdpClass.RecyclerViewClickListener() {
            @Override
            public void recyclerViewListClicked(View v, int position) {
                wordbox.append("" + word[position]);
                v.setVisibility(View.INVISIBLE);
            }
        });
        recyclerView.setAdapter(adp);
    }

    public void showDatainListView() {
        databaseReference = firebaseDatabase.getReference("Words").child(diffuclty);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String WORD = ds.child("Word").getValue(String.class);
                    wordlist.add(WORD);
                }
                if (alreadyCheckedWords.size() == snapshot.getChildrenCount()) {
                    endingDialog();
                    return;
                }
                String checkedWord = getRandomElement(wordlist);
                while (alreadyCheckedWords.contains(checkedWord)) {
                    wordlist.remove(checkedWord);
                    if (wordlist.isEmpty()) break;
                    checkedWord = getRandomElement(wordlist);
                }
                alreadyCheckedWords.add(checkedWord);
                word = shuffle(checkedWord);
                for (char c : word) {
                    usersArrayList.add(new letterClass(Character.toString(c)));
                    adp.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public char[] shuffle(String word) {
        String shuffledWord = word; // start with original
        int wordSize = word.length();
        int shuffleCount = 10; // let us randomly shuffle letters 10 times
        for (int i = 0; i < shuffleCount; i++) {
            //swap letters in two indexes
            int position1 = ThreadLocalRandom.current().nextInt(0, wordSize);
            int position2 = ThreadLocalRandom.current().nextInt(0, wordSize);
            shuffledWord = swapCharacters(shuffledWord, position1, position2);
        }
        return shuffledWord.toUpperCase().toCharArray();
    }

    public String swapCharacters(String shuffledWord, int position1, int position2) {
        char[] charArray = shuffledWord.toCharArray();
        // Replace with a "swap" function, if desired:
        char temp = charArray[position1];
        charArray[position1] = charArray[position2];
        charArray[position2] = temp;
        return new String(charArray);
    }

    public String getRandomElement(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

//    public void timersettings() {
//        if (diffuclty.equals("Easy")) {
//            maxCounter = 200000;
//            diff = 1000;
//        } else if (diffuclty.equals("Medium")) {
//            maxCounter = 100000;
//            diff = 1000;
//        } else {
//            maxCounter = 30000;
//            diff = 1000;
//        }
//        yourCountDownTimer = new CountDownTimer(maxCounter, diff) {
//
//            public void onTick(long millisUntilFinished) {
//                long diff = maxCounter - millisUntilFinished;
//                String a = String.format("%02d:%02d",
//                        ((diff / 1000) % 3600) / 60, ((diff / 1000) % 60));
//                timerfield.setText("" + a);
//            }
//
//            public void onFinish() {
//                endingDialog();
//            }
//
//        }.start();
//    }

    void updateScore(String id, long score) {
        databaseReference = firebaseDatabase.getReference("Users").child(id).child(diffuclty + " Score");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long SCORE = (long) snapshot.getValue();
                if (score > SCORE) {
                    databaseReference.setValue(score);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }


    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void endingDialog() {
        pauseTimer();
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.endingdialog, viewGroup, false);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        Button nobtn = dialogView.findViewById(R.id.nobtn);
        Button yesbtn = dialogView.findViewById(R.id.yesbtn);

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScore(enteredUserId,score);
                alertDialog.dismiss();
                finish();
            }
        });

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                reload();
            }
        });
        alertDialog.show();
    }

    public void pauseDialog() {
        pauseTimer();
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pausedialog, viewGroup, false);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        Button resumebtn = dialogView.findViewById(R.id.resumebtn);
        alertDialog.setCancelable(false);
        resumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.quitdialog, viewGroup, false);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView);
        Button nobtn = dialogView.findViewById(R.id.nobtn);
        Button yesbtn = dialogView.findViewById(R.id.yesbtn);

        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
                finish();
            }
        });
        alertDialog.show();

    }

    void startTimer()
    {
        yourCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftInMillis = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                endingDialog();
            }
        }.start();
        mTimerRunning = true;
    }
    void pauseTimer()
    {
        yourCountDownTimer.cancel();
        mTimerRunning = false;
    }
    void updateCountDownText()
    {
        String a = String.format(Locale.getDefault(),"%02d:%02d",
                ((mTimeLeftInMillis / 1000) % 3600) / 60, ((mTimeLeftInMillis / 1000) % 60));
        timerfield.setText("" + a);
    }
}
package com.example.mastermindgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoadingScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(LoadingScreen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception ex){
                    Toast.makeText(LoadingScreen.this,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        };
        thread.start();


    }
}
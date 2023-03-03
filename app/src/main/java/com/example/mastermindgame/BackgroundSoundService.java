package com.example.mastermindgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
        mediaPlayer.setLooping(true); // Set looping
    }
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals("PLAY")) {
            if(!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                Toast.makeText(getApplicationContext(), "Music Is Playing", Toast.LENGTH_SHORT).show();

            }
        }
        if (intent.getAction().equals("STOP")) {
            this.stopService(intent);
            Toast.makeText(getApplicationContext(), "Music Stopped",    Toast.LENGTH_SHORT).show();

        }

        return startId;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }

}

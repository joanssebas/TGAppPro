package com.example.tgapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class listenActivity extends AppCompatActivity {

    MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
    }


    public void play(View v){
        if (player == null){
            player = MediaPlayer.create(this, R.raw.sotware_maintenance);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });

        }
        player.start();
    }

    public void stop(View v){
        stopPlayer();

    }

    private void stopPlayer(){
        if (player != null){
            player.release();
            player = null;
            Toast.makeText(this, "Media player stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause(View v){
        if (player != null){
            player.pause();
        }

    }

    @Override
    protected void onStop(){
        super.onStop();
        stopPlayer();
    }


}
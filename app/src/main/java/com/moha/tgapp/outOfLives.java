package com.moha.tgapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class outOfLives extends AppCompatActivity {

    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_of_lives);

        finish = findViewById(R.id.finishBtnL);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(outOfLives.this,MainActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
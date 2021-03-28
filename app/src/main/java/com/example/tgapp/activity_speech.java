package com.example.tgapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class activity_speech extends AppCompatActivity {

    public static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    TextView mTextTv, textToRepeat;
    ImageButton mVoiceBtn;
    Button nextVoice;
    String[] softwareEngineer  ={
            "evaluate",
            "design",
            "software",
            "develop",
            "investigate",
            "install",
            "test",
            "programming in the small",
            "programming in the large",
            "artifacts",
            "investigate",
            "evaluate",
            "bios",
            "operating system",
            "control",
            "manually",
            "operate",
            "windowing system",
            "firmware",
            "hardware",
            "software",
            "device driver",
            "antivirus software",
            "firewalls",
            "web page text",
            "removal",
            "malware",
            "virus",
            "spyware",
            "quarantine",
            "firewall",
            "antivirus software"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        //añadir fragment


        mTextTv = findViewById(R.id.textTv);
        mVoiceBtn = findViewById(R.id.voiceBtn);
        textToRepeat = findViewById(R.id.TextToRepeat);
        nextVoice = findViewById(R.id.NextVoice);


        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        nextVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please repeat the phrase");

        //start

        try{

            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            Toast.makeText(this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if (resultCode == RESULT_OK && null!= data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //
                    mTextTv.setText(result.get(0));

                if(mTextTv.getText().equals(textToRepeat.getText())){
                    Toast.makeText(this, "You did it great!!", Toast.LENGTH_SHORT).show();
                    next();
                }else{
                    Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
                }


                }
                break;
            }
        }
    }


    public void next(){
        final int random = new Random().nextInt(softwareEngineer.length);
        textToRepeat.setText(softwareEngineer[random]);
    }
}
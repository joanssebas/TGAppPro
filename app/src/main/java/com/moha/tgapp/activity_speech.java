package com.moha.tgapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class activity_speech extends AppCompatActivity {

    TextToSpeech textToSpeech;

    public static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageView goBack;
    TextView mTextTv, textToRepeat;
    ImageButton mVoiceBtn,listenBtn;
    Button nextVoice;
    String[] softwareEngineer  ={
            "law of increasing complexity",
            "adaptive maintenance",
            "preventive maintenance",
            "perfective maintenance",
            "corrective maintenance",
            "law of continuing change", //primera categoria
            "investigate",
            "write",
            "install",
            "develop",
            "test",
            "evaluate",//segunda categoria
            "software",
            "design",
            "presentation",
            "artistic design",
            "layer",
            "hci",//tercera categoria
            "groupware",
            "end user",
            "operating system",
            "control",
            "bios",
            "manually",//cuarta categoria
            "windowing system",
            "operate",
            "programming software",
            "program",
            "source code editor",
            "programming language",//quinta categoria
            "interpreter",
            "compiler",
            "call graph",
            "cohesion",
            "information hiding",
            "wicked problem",//sexta categoria
            "stopping rule",
            "accounting",
            "enterprise software",
            "image editing",
            "office suite",
            "spreadsheet",//septima categoria
            "web browser",
            "focus",
            "dedicated",
            "team player",
            "ability",
            "curious",//octava categoria
            "logical",
            "approach",
            "application",
            "problem solving",
            "iterative",
            "synthesis",//novena categoria
            "problem identification",
            "elicitation",
            "specification",
            "customer driven",
            "validation",
            "requirements engineering",//decima categoria
            "market driven"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        //añadir fragment

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    //Select language
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        goBack=findViewById(R.id.goBackSpeak);
        mTextTv = findViewById(R.id.textTv);
        mVoiceBtn = findViewById(R.id.voiceBtn);
        textToRepeat = findViewById(R.id.TextToRepeat);
        nextVoice = findViewById(R.id.NextVoice);
        listenBtn = findViewById(R.id.listenBtn);


        mVoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_speech.this,MainActivity.class));
                finish();
            }
        });

        nextVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();
            }
        });


    }

    public void listen(){


        String s =  textToRepeat.getText().toString();

        //text convert

        int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);



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
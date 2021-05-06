package com.example.tgapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tgapp.databinding.ActivitySpeakQuizC1Binding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class SpeakQuizC1 extends AppCompatActivity {

    ActivitySpeakQuizC1Binding binding;
   /** @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_quiz_c1);
    }**/

    TextToSpeech textToSpeech;

    public static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    TextView mTextTv, textToRepeat;
    ImageButton mVoiceBtn,listenBtn;
    Button nextVoice;

    String[] softwareEngineer;

    int contador = 0;


    int corectasS;
    int totalQuestionsS;
    String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySpeakQuizC1Binding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_speak_quiz_c1);

        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);
        String idCategoria = getIntent().getStringExtra("idCategoria");
        //String[] preguntas = getIntent().getStringArrayExtra("preguntas");
        //String[] respuestas = getIntent().getStringArrayExtra("respuestas");

        corectasS = correctAnswers;
        totalQuestionsS = totalQuestions;
        categoria = idCategoria;

//1 quiz
        if ((idCategoria.equals("IgrYeYE3dOH09xWowpLN")) ||
                (idCategoria.equals("ARIOoTlUYEJttMdZFlsI"))){
            softwareEngineer = new String[]{
                    "law of increasing complexity",
                    "adaptive maintenance",
                    "preventive maintenance",
                    "perfective maintenance",
                    "corrective maintenance",
                    "law of continuing change"
            };
        }




//2
        if (idCategoria.equals("M4y7fZMP4lRFErb1Twqx") || (idCategoria.equals("ZZUBFu3QpzPBPgAt5nsv"))){
            softwareEngineer = new String[]{
                    "investigate",
                    "write",
                    "install",
                    "develop",
                    "test",
                    "evaluate"
            };

        }
        if (idCategoria.equals("SwBcKRSkImkxhQ88sDYu") || (idCategoria.equals("J3ZF71NREYDBWQGyYZXg"))){
            softwareEngineer = new String[]{
                    "software",
                    "design",
                    "presentation",
                    "artistic design",
                    "layer",
                    "hci"
            };

        }
        //cuarta

        if (idCategoria.equals("awoYs5mP62cMxeJ76ufA") || (idCategoria.equals("sCaYZtbwCnbEhiVmA4v5"))){
            softwareEngineer = new String[]{
                    "groupware",
                    "end user",
                    "operating system",
                    "control",
                    "bios",
                    "manually"
            };

        }

        //quinta

        if (idCategoria.equals("cMjsz4zqerixBPmDhnlB") || (idCategoria.equals("RRJsMivORjwXBkCN9GJ6"))){
            softwareEngineer = new String[]{
                    "windowing system",
                    "operate",
                    "programming software",
                    "program",
                    "source code editor",
                    "programming language"
            };

        }

        //sexta
        if (idCategoria.equals("h1HSzgzRO3r4NHBDEqo0") || (idCategoria.equals("8YPoiyQo26jvN0FvLZcl"))){
            softwareEngineer = new String[]{
                    "interpreter",
                    "compiler",
                    "call graph",
                    "cohesion",
                    "information hiding",
                    "wicked problem"
            };

        }

        //septima
        if (idCategoria.equals("iSXtyNPiLJYJ0VGqGYx7") || (idCategoria.equals("QoTQXOVB0N9vtqPsnboB"))){
            softwareEngineer = new String[]{
                    "stopping rule",
                    "accounting",
                    "enterprise software",
                    "image editing",
                    "office suite",
                    "spreadsheet"
            };

        }
        //octava
        if (idCategoria.equals("nlepdI00gg2XwLydqHtg") || (idCategoria.equals("Yu3VvnXgf00jPlxl6Ju5"))){
            softwareEngineer = new String[]{
                    "web browser",
                    "focus",
                    "dedicated",
                    "team player",
                    "ability",
                    "curious"
            };

        }
        //novena
        if (idCategoria.equals("sKnGqyNM1dEeIyWsenTo") || (idCategoria.equals("QEeBtnAtYYbTCGDNyrFB"))){
            softwareEngineer = new String[]{
                    "logical",
                    "approach",
                    "application",
                    "problem solving",
                    "iterative",
                    "synthesis"
            };

        }
        //decima
        if (idCategoria.equals("t04t6uRkjIWtDHfBmoj2") || (idCategoria.equals("u639v3PARsZI3ZDL6Gy2"))){
            softwareEngineer = new String[]{
                    "problem identification",
                    "elicitation",
                    "specification",
                    "customer driven",
                    "validation",
                    "requirements engineering"
            };

        }



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


        mTextTv = findViewById(R.id.textTv2);
        mVoiceBtn = findViewById(R.id.voiceBtn2);
        textToRepeat = findViewById(R.id.textToRepeat);
        nextVoice = findViewById(R.id.nextVoice);
        listenBtn = findViewById(R.id.listenBtn2);


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
                        Toast.makeText(this, "Try again...", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            }
        }
    }


    public void next(){
        if (contador < softwareEngineer.length){
            textToRepeat.setText(softwareEngineer[contador]);
            contador++;
        }

        else {
            Intent intent = new Intent(SpeakQuizC1.this,HangmanQuiz.class);
            intent.putExtra("correct", corectasS);
            intent.putExtra("total",totalQuestionsS);
            intent.putExtra("idCategoria",categoria);


            startActivity(intent);
        }


        //final int random = new Random().nextInt(softwareEngineer.length);
        //textToRepeat.setText(softwareEngineer[random]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
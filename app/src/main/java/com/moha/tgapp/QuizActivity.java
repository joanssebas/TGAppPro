package com.moha.tgapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moha.tgapp.databinding.ActivityQuizBinding;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ImageView heart1,heart2,heart3,heart4,heart5;
    MediaPlayer player;

    TextToSpeech textToSpeech;

    int contador = 0;
    String triesLeft = " X X X X X";


    //creamos el question text
    TextView questionText;
    Button quizBtn;

    final Handler handler = new Handler(Looper.getMainLooper());

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    int index =0;
    Question question;
    CountDownTimer timer;

    FirebaseFirestore database;
    int correctAnswers = 0;
    //
    int nQuestions = 9;
//variables para speak

    String idCategoria;
    String[] respuestas;
    String[] preguntas;

    int contadorPreguntas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        heart1 = findViewById(R.id.heart1q);
        heart2 = findViewById(R.id.heart2q);
        heart3 = findViewById(R.id.heart3q);
        heart4 = findViewById(R.id.heart4q);
        heart5 = findViewById(R.id.heart5q);
        //text to speech
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    //Select language
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        final String catId = getIntent().getStringExtra("catId");
        idCategoria = catId;
        Random random = new Random();
        final int rand = random.nextInt(8);

        //lo sacamos de la vista
        questionText = findViewById(R.id.question);

        quizBtn = findViewById(R.id.quizBtn);

        questions = new ArrayList<>();
        //Toast.makeText(this, "Questions " + questions, Toast.LENGTH_SHORT).show();
        database = FirebaseFirestore.getInstance();

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    //Select language
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =  questionText.getText().toString();

                //text convert

                int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizActivity.this,MainActivity.class));
            }
        });




        database.collection("categories")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", nQuestions)
                .orderBy("index")
                .limit(nQuestions).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size() < nQuestions){
                    database.collection("categories")
                            .document(catId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", nQuestions)
                            .orderBy("index")
                            .limit(nQuestions).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                    Question question = snapshot.toObject(Question.class);
                                    questions.add(question);
                                }
                            setNextQuestion();

                        }
                    });
                }
                else{
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }
            }
        });

        resetTimer();



    }

    void checkAnswer(TextView textView){



        String selectedAnswer = textView.getText().toString();



        if(selectedAnswer.equals(question.getAnswer())){
            respuestas = ArrayUtils.appendToArray(respuestas,question.getAnswer());
            //Toast.makeText(this, "respuesta " + respuestas[contadorPreguntas], Toast.LENGTH_SHORT).show();
            //contadorPreguntas++;
            correctAnswers++;
            int speech = textToSpeech.speak("",TextToSpeech.QUEUE_FLUSH,null);
            //MediaPlayer player;
            player = MediaPlayer.create(this,R.raw.right_answer);
            player.start();
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if ((index +1) < questions.size()) {

                        index++;
                        reset();
                        setNextQuestion();

                    }
                    /**
                    else {
                        Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                        intent.putExtra("correct",correctAnswers);
                        intent.putExtra("total",questions.size());
                        startActivity(intent);


                        //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                    }
**/
                     else{
                         Intent intent = new Intent(QuizActivity.this,SpeakQuizC1.class);
                         intent.putExtra("idCategoria",idCategoria);
                         intent.putExtra("preguntas", preguntas);
                         intent.putExtra("respuestas",respuestas);
                        intent.putExtra("correct",correctAnswers);
                        intent.putExtra("total",questions.size());
                        startActivity(intent);
                    }
                }
            }, 200);



        }else{
            respuestas = ArrayUtils.appendToArray(respuestas,question.getAnswer());
            //Toast.makeText(this, "respuesta " + respuestas[contadorPreguntas], Toast.LENGTH_SHORT).show();
            //contadorPreguntas++;
            player = MediaPlayer.create(this,R.raw.wrong_answer);
            player.start();
            showAnswer();

            int speech = textToSpeech.speak("",TextToSpeech.QUEUE_FLUSH,null);
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            decreaseAndDisplayTriesLeft();
            if (contador == 5){
                player = MediaPlayer.create(this,R.raw.game_over_loser);
                player.start();
                startActivity(new Intent(QuizActivity.this,outOfLives.class));

            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((index + 1)  < questions.size()) {
                        preguntas = ArrayUtils.appendToArray(preguntas,question.getQuestion());
                        //Toast.makeText(QuizActivity.this, "pregunta " + preguntas[index], Toast.LENGTH_SHORT).show();
                        index++;
                        reset();
                        setNextQuestion();
                    }else {

                        contador = nQuestions;

                        Intent intent = new Intent(QuizActivity.this,SpeakQuizC1.class);
                        intent.putExtra("correct",correctAnswers);
                        intent.putExtra("total",questions.size());
                        intent.putExtra("idCategoria",idCategoria);
                        intent.putExtra("preguntas", preguntas);
                        intent.putExtra("respuestas",respuestas);

                        startActivity(intent);


                        //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                    }
                    //Do something after 100ms

                }
            }, 200);

        }
    }

    void setNextQuestion(){
        if(timer != null)
            timer.cancel();


        timer.start();
        if (index < questions.size()){
            binding.questionCounter.setText(String.format("%d/%d", (index + 1), questions.size()));
            question = questions.get(index);


            binding.question.setText(question.getQuestion());

            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());

        }
    }




    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.color.options));
        binding.option2.setBackground(getResources().getDrawable(R.color.options));
        binding.option3.setBackground(getResources().getDrawable(R.color.options));
        binding.option4.setBackground(getResources().getDrawable(R.color.options));
    }

    void resetTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void showAnswer(){



        if(question.getAnswer().equals(binding.option1.getText().toString())){
            //String s = binding.option1.getText().toString();
            //int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
            setNextQuestion();
        }


        else if(question.getAnswer().equals(binding.option2.getText().toString())){
            //String s = binding.option2.getText().toString();
            //int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
            setNextQuestion();
        }


        else if(question.getAnswer().equals(binding.option3.getText().toString())){
            //String s = binding.option3.getText().toString();
            //int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
            setNextQuestion();
        }



        else if(question.getAnswer().equals(binding.option4.getText().toString())){
            //String s = binding.option4.getText().toString();
            //int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
            setNextQuestion();
        }



    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;
/**
            case R.id.nextBtn:
                reset();
                if (index < questions.size()) {
                    index++;
                    setNextQuestion();

                }else {

                    Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                    intent.putExtra("correct",correctAnswers);
                    intent.putExtra("total",questions.size());
                    startActivity(intent);


                    //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;
 **/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    void decreaseAndDisplayTriesLeft(){
        if (!triesLeft.isEmpty()){

            //Toast.makeText(this, "contador " + contador, Toast.LENGTH_SHORT).show();
            //take out the last 2 chacracters from the string
            triesLeft = triesLeft.substring(0, triesLeft.length() -2);
            contador = contador + 1;
            if (contador == 1){
                heart5.setImageResource(R.drawable.broken_heart);
            }
            if (contador == 2){
                heart4.setImageResource(R.drawable.broken_heart);

            }
            if (contador == 3){
                heart3.setImageResource(R.drawable.broken_heart);

            }
            if (contador == 4){
                heart2.setImageResource(R.drawable.broken_heart);

            }
            if (contador == 5){
                heart1.setImageResource(R.drawable.broken_heart);

            }


        }
    }
}
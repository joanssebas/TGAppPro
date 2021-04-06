package com.example.tgapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tgapp.databinding.ActivityPracticeBinding;
import com.example.tgapp.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class QuizActivityP extends AppCompatActivity {

    TextToSpeech textToSpeech;

    TextView questionText;

    final Handler handler = new Handler(Looper.getMainLooper());


    ActivityPracticeBinding binding;
    ArrayList<QuestionP> questions;
    int index =0;
    QuestionP question;
    CountDownTimer timer;

    FirebaseFirestore database;
    int correctAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        Random random = new Random();
        final int rand = random.nextInt(3);

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        //lo sacamos de la vista
        questionText = findViewById(R.id.question);

        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =  questionText.getText().toString();

                //text convert

                int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        database.collection("categoriesP")
                .document(catId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", 16)
                .orderBy("index")
                .limit(16).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size() < 16){
                    database.collection("categoriesP")
                            .document(catId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", 16)
                            .orderBy("index")
                            .limit(16).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                    QuestionP question = snapshot.toObject(QuestionP.class);
                                    questions.add(question);
                                }
                            setNextQuestion();

                        }
                    });
                }
                else{
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        QuestionP question = snapshot.toObject(QuestionP.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }
            }
        });

        resetTimer();


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


        }
    }

    void checkAnswer(TextView textView){



        String selectedAnswer = textView.getText().toString();



        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            int speech = textToSpeech.speak(selectedAnswer,TextToSpeech.QUEUE_FLUSH,null);
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if ((index +1) < questions.size()) {
                        index++;
                        reset();
                        setNextQuestion();

                    }else {
                        Intent intent = new Intent(QuizActivityP.this,ResultActivity.class);
                        intent.putExtra("correct",correctAnswers);
                        intent.putExtra("total",questions.size());
                        startActivity(intent);


                        //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                    }

                }
            }, 500);



        }else{
            showAnswer();

            int speech = textToSpeech.speak(selectedAnswer,TextToSpeech.QUEUE_FLUSH,null);
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((index + 1)  < questions.size()) {
                        index++;
                        reset();
                        setNextQuestion();

                    }else {
                        Intent intent = new Intent(QuizActivityP.this,ResultActivity.class);
                        intent.putExtra("correct",correctAnswers);
                        intent.putExtra("total",questions.size());
                        startActivity(intent);


                        //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                    }
                    //Do something after 100ms

                }
            }, 200);

        }
    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));

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






    }

    public void onClick(View view){
        switch (view.getId()){

            case R.id.option_1:
            case R.id.option_2:

                if(timer != null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);

                break;

            case R.id.nextBtn:
                reset();
                if (index < questions.size()) {
                    index++;
                    setNextQuestion();

                }else {
                    Intent intent = new Intent(QuizActivityP.this,ResultActivity.class);
                    intent.putExtra("correct",correctAnswers);
                    intent.putExtra("total",questions.size());
                    startActivity(intent);


                    //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
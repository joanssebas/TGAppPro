package com.example.tgapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tgapp.databinding.ActivityListenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;

public class listenActivity extends AppCompatActivity {


    ImageView goBack;
    TextView questionText;

    final Handler handler = new Handler(Looper.getMainLooper());

    ActivityListenBinding binding;
    ArrayList<QuestionListen> questions;
    int index = 0;
    QuestionListen question;

    FirebaseFirestore database;



    String[] audios  ={
"https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/softwareEngineer(1).mp3?alt=media&token=b38754ce-d1a0-48c9-9003-531e8ef06318",
"https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/softwareEngineer%20(2).mp3?alt=media&token=ddf5bf53-01c3-46b4-a947-4f84eff61b02",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/softwareEngineer%20(3).mp3?alt=media&token=daeab326-faf2-4ca7-9747-650fc42ec1da",
"https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/Hacker_Noon_Podcast_-_Problem_Solving%20(4).mp3?alt=media&token=01366641-3e78-412c-a4d1-2ee5f06d798e",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/open-source%20(5).mp3?alt=media&token=9fc56c5f-f39e-428e-ae2a-f9c40ba1cc3f",
"https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/open-source%20(6).mp3?alt=media&token=feebde09-4029-4838-ab8d-deba34031de0",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/postgres%20(7).mp3?alt=media&token=179637d5-4309-43fb-8167-afdcbbb83c81",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/The%20intersection%20of%20coding(8).mp3?alt=media&token=0854e301-7f4f-4b33-9c84-93d4f23e0355",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/The%20intersection%20of%20coding%20(9).mp3?alt=media&token=e33d4a72-e8d9-4f6c-a05f-6b4281fb8109",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/ONE%20thing%20every%20dev%20(10).mp3?alt=media&token=f68f0afb-e81d-4d43-bf4d-3a51945e30c2",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/js-party-163%20(11).mp3?alt=media&token=7991f998-dde2-4f51-8c7b-6bdf328e34e9",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/how%20to%20desig%20a%20great%20api%20(12).mp3?alt=media&token=27122773-2c39-44d2-9297-e96d0f61dce3",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/how%20to%20desig%20a%20great%20api%20(13).mp3?alt=media&token=f5fab510-9c63-4c4d-bd3d-1eda4f017266",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/What%20even%20is%20a%20micro%20frontend%20(14).mp3?alt=media&token=769d0939-aa73-431b-abe9-ec0e0d6a32d6",
            "https://firebasestorage.googleapis.com/v0/b/tgapp-d76b7.appspot.com/o/What%20even%20is%20a%20micro%20frontend%20(15).mp3?alt=media&token=16d5761c-1e5a-4cb2-b6f2-d7c7baf5f7dd"
    };

    int nListen =audios.length;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questionText = findViewById(R.id.question);
        goBack = findViewById(R.id.goBackListen);
        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        database.collection("listen")
                .whereGreaterThanOrEqualTo("index",nListen)
                .orderBy("index")
                .limit(nListen).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size() < nListen){
                    database.collection("listen")
                            .whereLessThanOrEqualTo("index",nListen)
                            .orderBy("index")
                            .limit(nListen).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                                QuestionListen question = snapshot.toObject(QuestionListen.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    });
                }
                else {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        QuestionListen question = snapshot.toObject(QuestionListen.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(listenActivity.this,MainActivity.class));
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();

        if (selectedAnswer.equals(question.getAnswer())){
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((index +1) < questions.size()) {
                        index++;
                        reset();
                        setNextQuestion();

                    }else {
                        Intent intent = new Intent(listenActivity.this,ResultListenActivity.class);
                        startActivity(intent);
                    }
                }
            }, 200);

        }
        else{
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if ((index + 1)  < questions.size()) {
                        index++;
                        reset();
                        setNextQuestion();
                }else{
                        Intent intent = new Intent(listenActivity.this,ResultListenActivity.class);
                        startActivity(intent);
                    }
            }
        },200);
    }

    }



        void setNextQuestion(){
            if (index < questions.size()){
                question = questions.get(index);
                binding.questionListen.setText(question.getQuestion());
                binding.option1.setText(question.getOption1());
                binding.option2.setText(question.getOption2());

            }

    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.color.options));
        binding.option2.setBackground(getResources().getDrawable(R.color.options));
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

            case R.id.option1:
            case R.id.option2:

                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;

           /**case R.id.nextBtn2:
             //   reset();
               // if (index < questions.size()) {
                    index++;

                    setNextQuestion();


                }else {
                    Intent intent = new Intent(listenActivity.this,ResultListenActivity.class);
                    startActivity(intent);


                    //Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;
            **/
        }
    }

/**
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

 public void pause(View v) {
 if (mediaPlayer != null) {
 mediaPlayer.pause();
 }
 }

**/

/**
    private void stopPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();

            Toast.makeText(this, "Media player stopped", Toast.LENGTH_SHORT).show();
        }
    }
 **/
/**
    public void pause(View v){
        if (player != null){
            player.pause();
        }

    }
**/

/**
    @Override
    protected void onStop(){
        super.onStop();
        stopPlayer();
    }
**/



/**


 public void pause(View v) {
 if (mediaPlayer != null) {
 mediaPlayer.pause();
 }
 }

 public void stop(){
 mediaPlayer.stop();

 }
**/
public void play(View v){

/**
    try {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audios[index]);
        mediaPlayer.prepare();
        mediaPlayer.start();
    } catch (IOException e) {
        e.printStackTrace();
    }

**/
    MediaPlayer mediaPlayer = new MediaPlayer();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
    }
    try {
        mediaPlayer.setDataSource(audios[index]);
        mediaPlayer.prepare();
        mediaPlayer.start();
        //mediaPlayer.release();
    } catch (IOException e) {
        e.printStackTrace();
    }






}
/**
    public void pause(View v) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop(View v){
        stopPlayer();
    }

    public void stopPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer=null;

        }

    }

    @Override
    protected void onStop(){
    super.onStop();
    stopPlayer();
    }

**/





}
package com.example.tgapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Hangman extends AppCompatActivity {

    ImageView heart1,heart2,heart3,heart4,heart5;
    private static MediaPlayer player;

    int contador = 0;


    Fade mFade;

    //pop up
    Dialog myDialog;

    long animationDutarion = 1000;

    //declaracion de variables

    TextView txtWordToBeGuessed, txtLettersTried, txtTriesLeft;
    String wordToBeGuessed;
    String wordDisplayedString;
    char[] wordDisplayedCharArray;
    ArrayList<String> myListOfWords;
    EditText edtInput;
    String lettersTried;

    ImageView hangmanImg;

    TextView meaning;

    String meaningString;

    Button btnReset;

    final String messageWithLettersTried ="Letters tried -> ";
    String triesLeft;
    final String WinningMessage = "You have done it!";
    final String LossingMessage = "Try again :(";
    Animation rotateAnimation;
    Animation scaleAnimation;
    Animation scaleAndRotateAnimation;

    String words[] = {
            "law-of-increasing-complexity",
            "adaptive-maintenance",
            "preventive-maintenance",
            "perfective-maintenance",
            "corrective-maintenance",
            "law-of-continuing-change", //primera categoria
            "investigate",
            "write",
            "install",
            "develop",
            "test",
            "evaluate",//segunda categoria
            "software",
            "design",
            "presentation",
            "artistic-design",
            "layer",
            "hci",//tercera categoria
            "groupware",
            "end-user",
            "operating-system",
            "control",
            "bios",
            "manually",//cuarta categoria
            "windowing-system",
            "operate",
            "programming-software",
            "program",
            "source-code-editor",
            "programming-language",//quinta categoria
            "interpreter",
            "compiler",
            "call-graph",
            "cohesion",
            "information-hiding",
            "wicked-problem",//sexta categoria
            "stopping-rule",
            "accounting",
            "enterprise-software",
            "image-editing",
            "office-suite",
            "spreadsheet",//septima categoria
            "web-browser",
            "focus",
            "dedicated",
            "team-player",
            "ability",
            "curious",//octava categoria
            "logical",
            "approach",
            "application",
            "problem-solving",
            "iterative",
            "synthesis",//novena categoria
            "problem-identification",
            "elicitation",
            "specification",
            "customer-driven",
            "validation",
            "requirements-engineering",//decima categoria
            "market-driven"
    };

    String answers[] ={
            "states that a system should undergo modification until it is no longer cost-effective",
            "the practice of updating software according to changes in environment ",
            "the practice of making systems easier to maintain ",
            "the process of fixing faults and making improvements in software ",
            "the practice of repairing software faults ",
            "states that a structure becomes more complex with every change", //primera categoria
            "To get more information about something",
            "To form letters and words into sentences or instructions",
            "To operate something into the place where it will function ",
            "To bring something from initial conception to action or implementation ",
            "To operate something to see whether it works",
            "To carefully study something and assess its qualities",//segunda categoria
            "The programs that perform particular functions on a computer",
            "To plan the way that something will be created ",
            "a way of evaluating a complex system ",
            "the practice of using graphic design in user interfaces ",
            "a level of system operation ",
            "the study and design of interactions between computers and users",//tercera
            "software that assists groups in working towards a common goal ",
            "the consumer who will ultimately use a product ",
            "a user interface that organizes information into visual boxes ",
            "to have power over the way something functions ",
            "a set of instructions in firmware ",
            "done directly by a person, without automatic functions",//cuarta
            "programs that manage a computer’s hardware and applications ",
            "to function in a specific manner according to instructions or software ",
            "software used to enter lines of coded text",
            "a series of operations that control the functions of a computer ",
            "any software that supports the development of new applications ",
            "codes used to write commands to a computer ",//quinta
            "something that reads and executes other programs ",
            "an application that decodes instructions written in other languages ",
            "shows the basic structure of how a system will work. ",
            "it is the connection between modules in a system.",
            "Modules conceal information from each other in a process called",
            "can have multiple causes and may be difficult to solve.",//sexta
            "A problem without a(n)________ may be difficult or impossible to solve. ",
            "this software records and manages transactions.  ",
            "Many large corporations use __ to maintain consistency in all their systems.  ",
            "software can be used to retouch photographs.   ",
            "A(n) __ usually includes a word processer. ",
            "Large sets of data can be organized into a __ .   ",//septima
            "Users often check their email using a(n)__ .",
            "to watch closely",
            "enthusiastic about a task or cause ",
            "someone who takes actions that benefit a group ",
            "the skill to do something ",
            "wanting to know more about something ",//octava
            "based on evidence and reason ",
            "a way of viewing or dealing with something ",
            "the action of putting something into operation ",
            "the ability to eliminate problems ",
            "intended to be updated continually ",
            "a combination of multiple elements or things ",//novena
            "the act of analyzing and describing problems",
            "the process of becoming apparent or realized ",
            "the act of checking that requirements are stated correctly ",
            "designed in response to specific needs of potential users ",
            "the act of checking that requirements are correct ",
            "the practice of specifying the necessary features and functions of software",//decima
            "designed for broad purposes "
    };




    void revealLetterInWord(char letter){
        int indexOfLetter = wordToBeGuessed.indexOf(letter);

        //loop if index is positive or zero
        while (indexOfLetter >= 0){
            wordDisplayedCharArray[indexOfLetter] = wordToBeGuessed.charAt(indexOfLetter);
            indexOfLetter = wordToBeGuessed.indexOf(letter,indexOfLetter + 1);

        }

        //update the string
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);
        edtInput.setText("");


    }

    void diplayWordOnScreen(){
        String formattedString = "";
        for (char character : wordDisplayedCharArray){
            formattedString += character + " ";
        }
        txtWordToBeGuessed.setText(formattedString);
    }

    void initializeGame(){
        //1.WORD
        //shuffle arraylist and get the first element
        heart1.setImageResource(R.drawable.heart);
        heart2.setImageResource(R.drawable.heart);
        heart3.setImageResource(R.drawable.heart);
        heart4.setImageResource(R.drawable.heart);
        heart5.setImageResource(R.drawable.heart);
        hangmanImg.setImageResource(R.drawable.hangman);
        /**ObjectAnimator animator = ObjectAnimator.ofFloat(hangmanImg,"x",400f);
        animator.setDuration(animationDutarion);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();**/
        Collections.shuffle(myListOfWords);
        wordToBeGuessed = myListOfWords.get(0);



        //significado de la palabra en juego
        for (int i = 0; i < words.length; i++){
            if(wordToBeGuessed.equals(words[i])){
                meaning.setText("meaning: " + answers[i]);
                //Toast.makeText(this, answers[i], Toast.LENGTH_SHORT).show();
            }
        }




        myListOfWords.remove(0);

        //initialize char array
        wordDisplayedCharArray = wordToBeGuessed.toCharArray();
        //add underscores

        for (int i = 0; i < wordDisplayedCharArray.length -1; i++){
            wordDisplayedCharArray[i] = '_';
        }
        //reveal all occurences of first character
        revealLetterInWord(wordDisplayedCharArray[0]);

        //reveal all ocurrences of last character
        revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);

        //initialize a string from this char
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        diplayWordOnScreen();

        edtInput.setText("");


        //letters tried
        lettersTried = " ";

        //display on screen
        txtLettersTried.setText(messageWithLettersTried);

        //tries left
        triesLeft = " X X X X X";
        txtTriesLeft.setText(triesLeft);



    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);

        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        heart4 = findViewById(R.id.heart4);
        heart5 = findViewById(R.id.heart5);




        btnReset = findViewById(R.id.btnReset);

        meaning = findViewById(R.id.meaning);

        hangmanImg =findViewById(R.id.hangmanImg);

        meaning.setText("meaning: " + meaningString);




        //pop up
        myDialog = new Dialog(this);



        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGame();
                contador = 0;
            }
        });

        myListOfWords = new ArrayList<String>();
        txtWordToBeGuessed = findViewById(R.id.txtWordToBeGuessed);
        edtInput = findViewById(R.id.edtInput);
        txtLettersTried = findViewById(R.id.txtLettersTried);
        txtTriesLeft = findViewById(R.id.txtTriesLeft);



        mFade = new Fade(Fade.IN);



        //database file
        InputStream myInputStream = null;
        Scanner in = null;

        String aWord = "";

        try {
            myInputStream = getAssets().open("database.txt");
            in = new Scanner(myInputStream);

            while (in.hasNext()){
                aWord=in.next();
                myListOfWords.add(aWord);



            }

        } catch (IOException e) {
            Toast.makeText(this, "There was a problem openning the file", Toast.LENGTH_SHORT).show();
        }
        finally {
            //close scanner
            if (in != null){
                in.close();
            }
            try {
                if (myInputStream != null){
                    myInputStream.close();

                }
            } catch (IOException e) {
                Toast.makeText(this,
                        e.getClass().getSimpleName() + ": " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

        }




        initializeGame();


        //set up the text changed listener for the edit text
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    checkIfLetterIsInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }







    public void handleAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(hangmanImg,"x",400f);
        animator.setDuration(animationDutarion);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public void showPopUp(View view){
        TextView txtClose;
        myDialog.setContentView(R.layout.pop_up_hangman);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    void checkIfLetterIsInWord(char letter){
        //the letter was found inside the word to be guessed
        if(wordToBeGuessed.indexOf(letter) >= 0){
            //if the letter was not displayed yet
            if (wordDisplayedString.indexOf(letter) < 0){
                //replace the underscore with that letter
                player = MediaPlayer.create(this,R.raw.right_answer);
                player.start();
                revealLetterInWord(letter);

                //update the changes on screen
                diplayWordOnScreen();

                //check if the game is won
                if (!wordDisplayedString.contains("_")){
                    //MediaPlayer player;
                    player = MediaPlayer.create(this,R.raw.good_idea_bell);
                    player.start();
                    hangmanImg.setImageResource(R.drawable.happy);
                    /**ObjectAnimator animator = ObjectAnimator.ofFloat(hangmanImg,"x",420f);
                    animator.setDuration(animationDutarion);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animator);
                    animatorSet.start();**/
                    txtTriesLeft.setText(WinningMessage);
                }


            }
        }
        //otherwise if the letter was not found
        else{
            //decrease the number of tries left
            //MediaPlayer player;
            player = MediaPlayer.create(this,R.raw.wrong_answer);
            player.start();
            decreaseAndDisplayTriesLeft();

            edtInput.setText("");

            //check if the game is lost
            if(triesLeft.isEmpty()){
               // MediaPlayer player;
                player = MediaPlayer.create(this,R.raw.game_over_loser);
                player.start();
                hangmanImg.setImageResource(R.drawable.dead);
                /**ObjectAnimator animator = ObjectAnimator.ofFloat(hangmanImg,"x",420f);
                animator.setDuration(animationDutarion);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator);
                animatorSet.start();**/
                txtTriesLeft.setText(LossingMessage);
                txtWordToBeGuessed.setText(wordToBeGuessed);
            }
        }

        //display the letter that was tried
        if (lettersTried.indexOf(letter) < 0){
            lettersTried += letter + ", ";
            String messageToBeDisplayed = messageWithLettersTried + lettersTried;
            txtLettersTried.setText(messageToBeDisplayed);
        }
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

            txtTriesLeft.setText(triesLeft);
        }
    }
}
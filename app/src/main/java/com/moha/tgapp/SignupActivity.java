package com.moha.tgapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.moha.tgapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        database= FirebaseFirestore.getInstance();

        dialog= new ProgressDialog(this);
        dialog.setMessage("Creating new account...");

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pass, name;

                email= binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();

                name= binding.nameBox.getText().toString();

                if(email.equals("") || pass.equals("") || name.equals("")){
                    Toast.makeText(SignupActivity.this, "ALL FIELDS ARE NECESSARY", Toast.LENGTH_SHORT).show();
                }
                else {

                    CollectionReference userReference = database.collection("users");
                    Query query = userReference.whereEqualTo("name",name);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult()){
                                    String user = documentSnapshot.getString("name");

                                    if (user.equals(name)){
                                        Toast.makeText(SignupActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                            if(task.getResult().size() == 0 ){
                                User user = new User(name,email,pass);

                                dialog.show();

                                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){

                                            String uid = task.getResult().getUser().getUid();
                                            database.collection("users")
                                                    .document(uid)
                                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        dialog.dismiss();
                                                        startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                                        finish();
                                                    }else{
                                                        Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });



                                        }else {
                                            dialog.dismiss();
                                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }
                    });
//

                }





            }
        });

        binding.goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
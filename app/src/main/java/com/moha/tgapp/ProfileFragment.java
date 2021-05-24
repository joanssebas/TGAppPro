package com.moha.tgapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.moha.tgapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;

import io.perfmark.Tag;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;

    FirebaseFirestore database;


    public ProfileFragment() {
        // Required empty public constructor
    }

    FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        mAuth = FirebaseAuth.getInstance();

       database= FirebaseFirestore.getInstance();

       database.collection("users")
               .document(FirebaseAuth.getInstance().getUid())
               .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
               User user = documentSnapshot.toObject(User.class);
               binding.profileEmail.setText(String.valueOf(user.getEmail()));
               binding.profileName.setText(String.valueOf(user.getName()));
           }
       });


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);


        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                signOutUser();
            }
        });


        return binding.getRoot();
    }

    private void signOutUser() {
        startActivity(new Intent(getActivity(),LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}
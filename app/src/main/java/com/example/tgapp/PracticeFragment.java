package com.example.tgapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tgapp.databinding.FragmentHomeBinding;
import com.example.tgapp.databinding.FragmentPracticeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PracticeFragment extends Fragment {

    public PracticeFragment() {
        // Required empty public constructor
    }

    FragmentPracticeBinding binding;
    FirebaseFirestore database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPracticeBinding.inflate(inflater, container,false);

        database = FirebaseFirestore.getInstance();

        ArrayList<CategoryPModel> categories = new ArrayList<>();


//navegar fragment a activity



        binding.SpeakBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),activity_speech.class));

            }
        });


        CategoryPAdapter adapter = new CategoryPAdapter(getContext(), categories);

        database.collection("categoriesP")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            CategoryPModel model = snapshot.toObject(CategoryPModel.class);
                            model.setCategoryId(snapshot.getId());

                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);

        return binding.getRoot();
    }
}
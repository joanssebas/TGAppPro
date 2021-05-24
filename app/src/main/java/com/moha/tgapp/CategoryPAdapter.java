package com.moha.tgapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryPAdapter extends RecyclerView.Adapter<CategoryPAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<CategoryPModel> categoryPModels;

    public CategoryPAdapter(Context context, ArrayList<CategoryPModel> categoryPModels) {
        this.context = context;
        this.categoryPModels=categoryPModels;
    }


    public void CategoryPModel(Context context, ArrayList<CategoryPModel> categoryPModels){
        this.context = context;
        this.categoryPModels=categoryPModels;
    }

    @NonNull
    @Override
    public CategoryPAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryPAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryPModel model = categoryPModels.get(position);

        holder.textView.setText(model.getCategoryName());
        Glide.with(context)
                .load(model.getCategoryImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,QuizActivityP.class);
                intent.putExtra("catId", model.getCategoryId());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoryPModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView){
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
            textView= itemView.findViewById(R.id.category);
        }
    }
}

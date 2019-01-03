package com.example.michelleooi.donateapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.example.michelleooi.donateapp.Adapters.AdapterComment;
import com.example.michelleooi.donateapp.Models.ModelComment;
import com.example.michelleooi.donateapp.R;

import java.util.ArrayList;

public class PopComment extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ModelComment> modelCommentArrayList = new ArrayList();
    AdapterComment adapterComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popcomment);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.95), (int)(height*0.95));
        recyclerView = findViewById(R.id.commentrecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterComment = new AdapterComment(this, modelCommentArrayList);
        recyclerView.setAdapter(adapterComment);

        populateRecyclerView();
    }


    public void populateRecyclerView() {

        ModelComment modelComment = new ModelComment(1, 9, R.drawable.ic_propic1,
                "Sajin Maharjan", "2 hrs", "The cars we drive say a lot about us.");
        modelCommentArrayList.add(modelComment);
        modelComment = new ModelComment(2, 26, R.drawable.ic_propic2,
                "Karun Shrestha", "9 hrs", "Don't be afraid of your fears. They're not there to scare you. They're there to \n" +
                "let you know that something is worth it.");
        modelCommentArrayList.add(modelComment);
        modelComment = new ModelComment(3, 17, R.drawable.ic_propic3,
                "Lakshya Ram", "13 hrs", "That reflection!!!");
        modelCommentArrayList.add(modelComment);

        adapterComment.notifyDataSetChanged();
    }
}

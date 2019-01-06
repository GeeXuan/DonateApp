package com.example.michelleooi.donateapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.michelleooi.donateapp.Adapters.AdapterFeedComment;
import com.example.michelleooi.donateapp.Models.ModelFeedComment;
import com.example.michelleooi.donateapp.R;

import java.util.ArrayList;

public class FeedCommentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ModelFeedComment> modelFeedCommentArrayList = new ArrayList();
    AdapterFeedComment adapterFeedComment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedcomment);

        recyclerView = findViewById(R.id.commentRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterFeedComment = new AdapterFeedComment(this, modelFeedCommentArrayList);
        recyclerView.setAdapter(adapterFeedComment);

        populateRecyclerView();
    }


    public void populateRecyclerView() {

        ModelFeedComment modelFeedComment = new ModelFeedComment(1, 9, R.drawable.ic_propic1,
                "Sajin Maharjan", "2 hrs", "The cars we drive say a lot about us.");
        modelFeedCommentArrayList.add(modelFeedComment);
        modelFeedComment = new ModelFeedComment(2, 26, R.drawable.ic_propic2,
                "Karun Shrestha", "9 hrs", "Don't be afraid of your fears. They're not there to scare you. They're there to \n" +
                "let you know that something is worth it.");
        modelFeedCommentArrayList.add(modelFeedComment);
        modelFeedComment = new ModelFeedComment(3, 17, R.drawable.ic_propic3,
                "Lakshya Ram", "13 hrs", "That reflection!!!");
        modelFeedCommentArrayList.add(modelFeedComment);

        adapterFeedComment.notifyDataSetChanged();
    }
}

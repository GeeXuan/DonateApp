package com.example.michelleooi.donateapp.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterFeed;
import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedHistoryTab extends Fragment {

    RecyclerView feedHistoryRecyclerView;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    Button btnPostFeed;
    ProgressBar progress;
    TextView emptyText;
    String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_feed_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
        progress = getActivity().findViewById(R.id.progress_bar);
        userid = getArguments().getString("userid");
        feedHistoryRecyclerView = getActivity().findViewById(R.id.feedHistoryRecyclerView);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        feedHistoryRecyclerView.setLayoutManager(layoutManager);
        emptyText = getActivity().findViewById(R.id.emptyText);

        populateFeedRecyclerView(Query.Direction.DESCENDING);
        btnPostFeed = getActivity().findViewById(R.id.btnPostFeed);
    }

    public void populateFeedRecyclerView(Query.Direction direction) {
        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);
        feedHistoryRecyclerView.setAdapter(adapterFeed);
        modelFeedArrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("Feeds")
                .whereEqualTo("userid", userid)
                .whereEqualTo("status", "Active")
                .orderBy("postTime", direction)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ModelFeed modelFeed = document.toObject(ModelFeed.class);
                            modelFeed.setId(document.getId());
                            modelFeedArrayList.add(modelFeed);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed To Load Feeds", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (modelFeedArrayList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                        }
                        adapterFeed.notifyDataSetChanged();
                        feedHistoryRecyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}
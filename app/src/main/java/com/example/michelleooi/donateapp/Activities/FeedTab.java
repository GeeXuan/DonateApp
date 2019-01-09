package com.example.michelleooi.donateapp.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedTab extends Fragment {

    RecyclerView feedRecyclerView;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    SwipeRefreshLayout swipeLayout;
    ProgressBar progress;
    TextView emptyText;
    String area, direction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateFeedRecyclerView();
            }
        });
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        feedRecyclerView = getActivity().findViewById(R.id.feedRecyclerView);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);
        feedRecyclerView.setAdapter(adapterFeed);
        feedRecyclerView.setLayoutManager(layoutManager);
        emptyText = getActivity().findViewById(R.id.emptyText);
        progress = getActivity().findViewById(R.id.progress_bar);
        area = getArguments().getString("area");
        direction = getArguments().getString("direction");
        populateFeedRecyclerView();
    }

    public void populateFeedRecyclerView() {
        Query.Direction queryDirection;
        if (direction.equals("ascending"))
            queryDirection = Query.Direction.ASCENDING;
        else
            queryDirection = Query.Direction.DESCENDING;
        modelFeedArrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference modelFeedRef = db.collection("Feeds");
        modelFeedRef
                .whereEqualTo("status", "Active")
                .whereEqualTo("area", area)
                .orderBy("postTime", queryDirection)
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
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        adapterFeed.notifyDataSetChanged();
                        feedRecyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}

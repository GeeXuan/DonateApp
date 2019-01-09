package com.example.michelleooi.donateapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterEvent;
import com.example.michelleooi.donateapp.Models.ModelEvent;
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

public class EventFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView eventRecyclerView;
    private AdapterEvent adapterEvent;
    private ArrayList<ModelEvent> modelEventArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEventActivity.class);
                startActivity(intent);
            }
        });
        eventRecyclerView = getActivity().findViewById(R.id.eventRecyclerView);
        adapterEvent = new AdapterEvent(getActivity(), modelEventArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        eventRecyclerView.setAdapter(adapterEvent);
        eventRecyclerView.setLayoutManager(layoutManager);
        populateEventRecyclerView();
    }

    public void populateEventRecyclerView() {
        modelEventArrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference modelEventRef = db.collection("Events");
        modelEventRef
                .whereEqualTo("status", "Active")
                .orderBy("submitDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ModelEvent modelEvent = document.toObject(ModelEvent.class);
                            modelEvent.setId(document.getId());
                            modelEventArrayList.add(modelEvent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed To Load Events", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        adapterEvent.notifyDataSetChanged();
                        eventRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }
}

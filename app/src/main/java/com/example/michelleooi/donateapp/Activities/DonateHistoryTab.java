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
import android.widget.TextView;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterPayment;
import com.example.michelleooi.donateapp.Models.ModelPayment;
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

public class DonateHistoryTab extends Fragment {
    RecyclerView donateHistoryRecyclerView;
    ArrayList<ModelPayment> modelPaymentArrayList = new ArrayList<>();
    AdapterPayment adapterPayment;
    String userid;
    TextView emptyText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_donate_history, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userid = getArguments().getString("userid");
        donateHistoryRecyclerView = getActivity().findViewById(R.id.feedHistoryRecyclerView);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        donateHistoryRecyclerView.setLayoutManager(layoutManager);
        emptyText = getActivity().findViewById(R.id.emptyText);

        populateDonationRecyclerView();
    }

    public void populateDonationRecyclerView() {
        adapterPayment = new AdapterPayment(getActivity(), modelPaymentArrayList);
        donateHistoryRecyclerView.setAdapter(adapterPayment);
        modelPaymentArrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.collection("Payments")
                .whereEqualTo("userid", userid)
                .whereEqualTo("status", "Paid")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ModelPayment modelPayment = document.toObject(ModelPayment.class);
                            modelPayment.setId(document.getId());
                            modelPaymentArrayList.add(modelPayment);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Loading Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (modelPaymentArrayList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                        }
                        adapterPayment.notifyDataSetChanged();
                        donateHistoryRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

}

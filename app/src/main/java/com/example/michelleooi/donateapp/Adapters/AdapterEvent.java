package com.example.michelleooi.donateapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Activities.ApproveEventActivity;
import com.example.michelleooi.donateapp.Activities.EventDetailsActivity;
import com.example.michelleooi.donateapp.Models.ModelEvent;
import com.example.michelleooi.donateapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.MyViewHolder> {
    private Context context;
    private ArrayList<ModelEvent> modelEventArrayList = new ArrayList<>();
    private RequestManager glide;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userid = mAuth.getUid();
    private String operation;

    public AdapterEvent(FragmentActivity context, ArrayList<ModelEvent> modelFeedArrayList, String operation) {
        this.operation = operation;
        this.context = context;
        this.modelEventArrayList = modelFeedArrayList;
        glide = Glide.with(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ModelEvent modelEvent = modelEventArrayList.get(position);
        holder.eventName.setText(modelEvent.getName());
        if (modelEvent.getDescription().length() > 50) {
            holder.eventDescription.setText(modelEvent.getDescription().substring(0, 49) + "...");
        } else {
            holder.eventDescription.setText(modelEvent.getDescription());
        }
        holder.eventCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operation.equals("Donate")) {
                    Intent intent = new Intent(context, EventDetailsActivity.class);
                    intent.putExtra("EventID", modelEvent.getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ApproveEventActivity.class);
                    intent.putExtra("EventID", modelEvent.getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelEventArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView eventCard;
        private TextView eventName, eventDescription;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventCard = itemView.findViewById(R.id.eventCard);
            eventName = itemView.findViewById(R.id.eventName);
            eventDescription = itemView.findViewById(R.id.eventDescription);
        }
    }
}

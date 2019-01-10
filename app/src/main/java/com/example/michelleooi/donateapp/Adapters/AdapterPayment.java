package com.example.michelleooi.donateapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.michelleooi.donateapp.Models.ModelEvent;
import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.Models.ModelPayment;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.MyViewHolder> {

    private ModelEvent modelEvent;
    private Context context;
    private ArrayList<ModelPayment> modelPaymentArrayList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterPayment(FragmentActivity context, ArrayList<ModelPayment> modelPaymentArrayList) {
        this.context = context;
        this.modelPaymentArrayList = modelPaymentArrayList;
    }

    @Override
    public AdapterPayment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment, parent, false);
        AdapterPayment.MyViewHolder viewHolder = new AdapterPayment.MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final ModelPayment modelPayment = modelPaymentArrayList.get(position);
        db.collection("Events").document(modelPayment.getEventid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                modelEvent = documentSnapshot.toObject(ModelEvent.class);
                holder.eventName.setText("Event: " + modelEvent.getName());
                holder.donateDate.setText("Date: " + modelPayment.getDate().toString());
                holder.amount.setText(String.format("Amount: RM%2f", modelPayment.getAmount()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPaymentArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView donateDate, eventName, amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            donateDate = itemView.findViewById(R.id.donateDate);
            eventName = itemView.findViewById(R.id.eventName);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}

package com.example.michelleooi.donateapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterFeedImage;
import com.example.michelleooi.donateapp.Models.ModelEvent;
import com.example.michelleooi.donateapp.Models.ModelPayment;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ApproveEventActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayout sliderDotspanel;
    ViewPager viewPager;
    TextView eventName;
    TextView eventTitle;
    TextView eventDescription;
    Button btnApprove;
    String eventid;
    Uri uri;
    ImageView[] dots;
    int dotscount;
    ModelEvent modelEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_event);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        eventName = findViewById(R.id.eventName);
        eventTitle = findViewById(R.id.eventTitle);
        btnApprove = findViewById(R.id.btnApprove);
        eventDescription = findViewById(R.id.eventDescription);
        eventid = getIntent().getStringExtra("EventID");
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApproveEventActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        db.collection("Events").document(eventid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ModelEvent modelEvent = documentSnapshot.toObject(ModelEvent.class);
                                modelEvent.setStatus("Active");
                                db.collection("Events").document(eventid).set(modelEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        showToast("Approve Successful");
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        db.collection("Events").document(eventid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                modelEvent = documentSnapshot.toObject(ModelEvent.class);
                eventName.setText(modelEvent.getName());
                eventDescription.setText(modelEvent.getDescription());
                eventTitle.setText(String.format("by %s", modelEvent.getDescription()));
                if (modelEvent.getImages() != null && !modelEvent.getImages().isEmpty()) {
                    viewPager.setVisibility(View.VISIBLE);
                    sliderDotspanel.setVisibility(View.VISIBLE);
                    ArrayList<Uri> uriImage = new ArrayList();
                    for (String postpic : modelEvent.getImages()) {
                        uri = Uri.parse(postpic);
                        uriImage.add(uri);
                    }
                    AdapterFeedImage adapterFeedImage = new AdapterFeedImage(ApproveEventActivity.this, uriImage);
                    viewPager.setAdapter(adapterFeedImage);
                    dotscount = adapterFeedImage.getCount();
                    dots = new ImageView[dotscount];
                    for (int i = 0; i < dotscount; i++) {
                        dots[i] = new ImageView(ApproveEventActivity.this);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        params.setMargins(8, 0, 8, 0);
                        sliderDotspanel.addView(dots[i], params);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

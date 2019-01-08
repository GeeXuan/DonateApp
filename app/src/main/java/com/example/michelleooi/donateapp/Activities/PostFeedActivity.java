package com.example.michelleooi.donateapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterFeedImage;
import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PostFeedActivity extends AppCompatActivity implements UploadPhotoDialog.BottomSheetListener {

    ImageButton bUploadImage;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    Button btnPostFeed;
    EditText feedAddPostText;
    ArrayList<Uri> uriArrayList = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postfeed);

        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        feedAddPostText = (EditText) findViewById(R.id.feedAddPostText);

        btnPostFeed = findViewById(R.id.btnPostFeed);
        btnPostFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedAddPostText.getText().toString().equals("") && (uriArrayList == null || uriArrayList.isEmpty())) {
                    Toast.makeText(PostFeedActivity.this, "Nothing to post", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog progress = new ProgressDialog(view.getContext());
                    progress.setTitle("Uploading");
                    progress.setMessage("Submitting your post...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final CollectionReference modelFeedRef = db.collection("Feeds");
                    String text = feedAddPostText.getText().toString();
                    final ModelFeed modelFeed = new ModelFeed(0, 0, 0, "Active", text, mAuth.getUid(), new Date(), null);
                    final ArrayList<String> downloadUrl = new ArrayList<>();
                    if (uriArrayList != null && !uriArrayList.isEmpty()) {
                        final int size = uriArrayList.size();
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageReference = firebaseStorage.getReference();
                        for (Uri uri :
                                uriArrayList) {
                            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                            ref.putFile(uri)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String url = uri.toString();
                                                        downloadUrl.add(url);
                                                        if (downloadUrl.size() == size) {
                                                            modelFeed.setPostPic(downloadUrl);
                                                            modelFeedRef.add(modelFeed);
                                                            progress.dismiss();
                                                            Intent intent = getIntent();
                                                            setResult(RESULT_OK, intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    } else {
                        modelFeedRef.add(modelFeed);
                        progress.dismiss();
                        Intent intent = getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        bUploadImage = findViewById(R.id.imageButton);
        bUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPhotoDialog dialog = new UploadPhotoDialog();
                dialog.show(getSupportFragmentManager(), "UploadPhoto");
            }
        });
    }

    @Override
    public void onButtonClicked(ArrayList<Uri> uriArrayList) {
        if (!uriArrayList.isEmpty()) {
            this.uriArrayList = uriArrayList;
            AdapterFeedImage adapterFeedImage = new AdapterFeedImage(this, uriArrayList);
            viewPager.setAdapter(adapterFeedImage);
            dotscount = adapterFeedImage.getCount();
            dots = new ImageView[dotscount];
            for (int i = 0; i < dotscount; i++) {
                dots[i] = new ImageView(this);
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
}

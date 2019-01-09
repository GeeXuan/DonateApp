package com.example.michelleooi.donateapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Adapters.AdapterFeedImage;
import com.example.michelleooi.donateapp.Models.ModelEvent;
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


public class AddEventActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_MULTIPLE = 1;
    private EditText eventTitle, eventName, eventDescription, eventGoal;
    private Button btn_Submit_Event, btn_Add_Images;
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private String eventTitleString, eventNameString, eventDescriptionString;
    private double eventGoalDouble;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        initializeView();

    }

    private void initializeView() {
        eventTitle = findViewById(R.id.eventTitle);
        eventName = findViewById(R.id.eventName);
        eventDescription = findViewById(R.id.eventDescription);
        eventGoal = findViewById(R.id.eventGoal);
        viewPager = findViewById(R.id.viewPager);
        sliderDotspanel = findViewById(R.id.SliderDots);
        btn_Add_Images = findViewById(R.id.btn_Add_Images);
        btn_Submit_Event = findViewById(R.id.Btn_Submit_Event);
        btn_Add_Images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddEventActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(AddEventActivity.this, "Please accept for required permission !", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                }
            }
        });
        btn_Submit_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventTitleString = eventTitle.getText().toString();
                eventNameString = eventName.getText().toString();
                eventDescriptionString = eventDescription.getText().toString();
                eventGoalDouble = Double.parseDouble(eventGoal.getText().toString());

                final ProgressDialog progress = new ProgressDialog(view.getContext());
                progress.setTitle("Submitting");
                progress.setMessage("Submitting...");
                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                progress.show();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference modelEventRef = db.collection("Events");
                final ModelEvent modelEvent = new ModelEvent(eventTitleString, eventNameString, eventDescriptionString, "Pending", eventGoalDouble, new Date());
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
                                                        modelEvent.setImages(downloadUrl);
                                                        modelEventRef.add(modelEvent);
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
                    modelEventRef.add(modelEvent);
                    progress.dismiss();
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_MULTIPLE) {
            viewPager.setVisibility(View.GONE);
            if (resultCode == RESULT_OK) {
                sliderDotspanel.removeAllViews();
                dotscount = 0;
                dots = null;
                uriArrayList.clear();
                if (data.getData() != null) {
                    Uri mImageUri = data.getData();
                    uriArrayList.add(mImageUri);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            uriArrayList.add(uri);
                        }
                    }

                }
                if (!uriArrayList.isEmpty()) {
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
                    viewPager.setVisibility(View.VISIBLE);
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
        super.onActivityResult(requestCode, resultCode, data);
    }
}

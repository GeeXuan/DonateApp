package com.example.michelleooi.donateapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michelleooi.donateapp.Models.ModelUser;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {

    private Button updateBtn;
    private ImageView updateUserPhoto;
    private EditText updateName;
    private TextView updateMail;

    static int PReqCode = 1;
    static int REQUESTCODE = 2;
    private final static int UPDATEPROFILE = 5;
    Uri pickedImgUri;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateBtn = findViewById(R.id.updateBtn);
        updateUserPhoto = findViewById(R.id.updateUserPhoto);
        updateName = findViewById(R.id.updateName);
        updateMail = findViewById(R.id.updateMail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        updateMail.setText(currentUser.getEmail());

        updateUserPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = updateName.getText().toString();
                final FirebaseUser user = mAuth.getCurrentUser();
                updateProfile(name, pickedImgUri, mAuth.getCurrentUser());
            }
        });

    }

    private void updateProfile(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Updating");
        progress.setMessage("Updating your profile...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            db.collection("Users").whereEqualTo("id", currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    ModelUser modelUser = queryDocumentSnapshots.getDocuments().get(0).toObject(ModelUser.class);
                                                    modelUser.setName(name);
                                                    modelUser.setProPic(uri.toString());
                                                    db.collection("Users").document(currentUser.getUid()).set(modelUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progress.dismiss();
                                                            showMessage("Update Successful !");
                                                            updateUI();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(UpdateActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(UpdateActivity.this, "Please accept for required permission !", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else {
            openGallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {
            pickedImgUri = data.getData();
            updateUserPhoto.setImageURI(pickedImgUri);
        }
    }

}

package com.example.michelleooi.donateapp.Activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Adapters.AdapterFeedComment;
import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.Models.ModelFeedComment;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FeedCommentActivity extends AppCompatActivity implements CommentUploadPhotoDialog.BottomSheetListener {

    RecyclerView commentRecyclerView;
    ArrayList<ModelFeedComment> modelFeedCommentArrayList = new ArrayList();
    AdapterFeedComment adapterFeedComment;
    EditText commentInsertText;
    ImageButton commentSubmit, commentInsertPhoto;
    ImageView commentInsertPhotoPreview;
    String FeedID;
    Uri uri;
    TextView emptyText;
    SwipeRefreshLayout swipeLayout;
    ProgressBar progress;
    RequestManager glide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedcomment);
        glide = Glide.with(this);
        progress = this.findViewById(R.id.progress_bar);
        swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateRecyclerView();
            }
        });
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        FeedID = getIntent().getStringExtra("FeedID");

        commentRecyclerView = findViewById(R.id.commentRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentInsertText = findViewById(R.id.commentInsertText);
        commentSubmit = findViewById(R.id.commentSubmit);
        commentInsertPhoto = findViewById(R.id.commentInsertPhoto);
        commentInsertPhotoPreview = findViewById(R.id.commentInsertPhotoPreview);
        emptyText = findViewById(R.id.emptyText);
        commentInsertPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentUploadPhotoDialog dialog = new CommentUploadPhotoDialog();
                dialog.show(getSupportFragmentManager(), "UploadPhoto");
            }
        });

        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentInsertText.getText().toString().equals("") && uri == null) {
                    Toast.makeText(FeedCommentActivity.this, "Nothing to post", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog progress = new ProgressDialog(view.getContext());
                    progress.setTitle("Uploading");
                    progress.setMessage("Submitting your comment...");
                    progress.setCancelable(false);
                    progress.show();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference modelFeedDoc = db.collection("Feeds").document(FeedID);
                    final CollectionReference modelFeedCommentRef = modelFeedDoc.collection("Comments");
                    String text = commentInsertText.getText().toString();
                    final ModelFeedComment modelFeedComment = new ModelFeedComment(0, 0, text, mAuth.getUid(), "Active", new Date());
                    if (uri != null) {
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageReference = firebaseStorage.getReference();
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
                                                    modelFeedComment.setPostPic(url);
                                                    modelFeedCommentRef.add(modelFeedComment);
                                                    Task task = modelFeedDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            ModelFeed modelFeed = task.getResult().toObject(ModelFeed.class);
                                                            modelFeed.setCommentNo(modelFeed.getCommentNo() + 1);
                                                            modelFeedDoc.set(modelFeed);
                                                        }
                                                    });
                                                    progress.dismiss();
                                                    populateRecyclerView();
                                                }

                                            });
                                        }
                                    }
                                });
                    } else {
                        modelFeedCommentRef.add(modelFeedComment);
                        Task task = modelFeedDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                ModelFeed modelFeed = task.getResult().toObject(ModelFeed.class);
                                modelFeed.setCommentNo(modelFeed.getCommentNo() + 1);
                                modelFeedDoc.set(modelFeed);
                            }
                        });
                        progress.dismiss();
                        populateRecyclerView();
                    }
                }
            }
        });
        populateRecyclerView();
    }


    public void populateRecyclerView() {
        adapterFeedComment = new AdapterFeedComment(this, modelFeedCommentArrayList, FeedID);
        commentRecyclerView.setAdapter(adapterFeedComment);
        modelFeedCommentArrayList.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference modelFeedRef = db.collection("Feeds").document(FeedID).collection("Comments");
        modelFeedRef.orderBy("postTime", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ModelFeedComment modelFeedComment = document.toObject(ModelFeedComment.class);
                            modelFeedComment.setId(document.getId());
                            modelFeedCommentArrayList.add(modelFeedComment);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FeedCommentActivity.this, "Failed To Load Comments", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (modelFeedCommentArrayList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                        }
                        if (swipeLayout.isRefreshing()) {
                            swipeLayout.setRefreshing(false);
                        }
                        adapterFeedComment.notifyDataSetChanged();
                        commentRecyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                    }
                });
        adapterFeedComment.notifyDataSetChanged();
    }

    @Override
    public void onButtonClicked(Uri uri) {
        this.uri = uri;
        if (uri == null) {
            commentInsertPhotoPreview.setVisibility(View.GONE);
        } else {
            glide.load(uri).into(commentInsertPhotoPreview);
            commentInsertPhotoPreview.setVisibility(View.VISIBLE);
        }
    }

}

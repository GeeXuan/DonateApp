package com.example.michelleooi.donateapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Activities.ProfileViewActivity;
import com.example.michelleooi.donateapp.Models.ModelFeedComment;
import com.example.michelleooi.donateapp.Models.ModelUser;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterFeedComment extends RecyclerView.Adapter<AdapterFeedComment.MyViewHolder> {

    Context context;
    ArrayList<ModelFeedComment> modelFeedCommentArrayList = new ArrayList<>();
    RequestManager glide;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String FeedID;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userid = mAuth.getUid();

    public AdapterFeedComment(FragmentActivity context, ArrayList<ModelFeedComment> modelFeedCommentArrayList, String FeedID) {
        this.context = context;
        this.modelFeedCommentArrayList = modelFeedCommentArrayList;
        this.FeedID = FeedID;
        glide = Glide.with(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feedcomment, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.progressBar.setVisibility(View.VISIBLE);
        final List<String> upvotedusers, downvotedusers;
        final ModelFeedComment modelFeedComment = modelFeedCommentArrayList.get(position);
        if (modelFeedComment.getUpvotedUsers() != null)
            upvotedusers = modelFeedComment.getUpvotedUsers();
        else
            upvotedusers = new ArrayList<>();
        if (modelFeedComment.getDownvotedUsers() != null)
            downvotedusers = modelFeedComment.getDownvotedUsers();
        else
            downvotedusers = new ArrayList<>();

        CollectionReference userRef = db.collection("Users");
        userRef.document(modelFeedComment.getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final ModelUser modelUser = documentSnapshot.toObject(ModelUser.class);
                        modelUser.setId(documentSnapshot.getId());
                        holder.commentPic.setVisibility(View.GONE);

                        holder.commenter.setText(modelUser.getName());
                        holder.commenttime.setText(String.format("%s", new SimpleDateFormat("HH:mm dd-MM-yyyy").format(modelFeedComment.getPostTime())));
                        holder.commentupvoteAmount.setText(String.valueOf(modelFeedComment.getUpvotes()));
                        holder.commentdownvoteAmount.setText(String.valueOf(modelFeedComment.getDownvotes()));
                        if (upvotedusers.contains(userid)) {
                            holder.commentupVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_up_blue_24dp);
                            holder.commentupvoteAmount.setTextColor(Color.parseColor("#00B0FF"));
                        }
                        if (downvotedusers.contains(userid)) {
                            holder.commentdownVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_down_orange_24dp);
                            holder.commentdownvoteAmount.setTextColor(Color.parseColor("#C85D30"));
                        }
                        if (modelFeedComment.getText() != null) {
                            holder.comments.setText(modelFeedComment.getText());
                            holder.comments.setVisibility(View.VISIBLE);
                        } else {
                            holder.comments.setVisibility(View.GONE);
                        }
                        Uri uri = Uri.parse(modelUser.getProPic());
                        glide.load(uri).into(holder.imgView_proPic);
                        holder.imgView_proPic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ProfileViewActivity.class);
                                intent.putExtra("Uid", modelUser.getId());
                                context.startActivity(intent);
                            }
                        });
                        holder.commentupvoteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (upvotedusers.contains(userid)) {
                                    holder.commentupVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                    holder.commentupvoteAmount.setTextColor(Color.parseColor("#000000"));
                                    upvotedusers.remove(userid);
                                    modelFeedComment.setUpvotes(modelFeedComment.getUpvotes() - 1);
                                } else {
                                    holder.commentupVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_up_blue_24dp);
                                    holder.commentupvoteAmount.setTextColor(Color.parseColor("#00B0FF"));
                                    upvotedusers.add(userid);
                                    modelFeedComment.setUpvotes(modelFeedComment.getUpvotes() + 1);
                                    if (downvotedusers.contains(userid)) {
                                        holder.commentdownVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                        holder.commentdownvoteAmount.setTextColor(Color.parseColor("#000000"));
                                        downvotedusers.remove(userid);
                                        modelFeedComment.setDownvotes(modelFeedComment.getDownvotes() - 1);
                                    }
                                }
                                modelFeedComment.setDownvotedUsers(downvotedusers);
                                modelFeedComment.setUpvotedUsers(upvotedusers);
                                holder.commentupvoteAmount.setText(String.valueOf(modelFeedComment.getUpvotes()));
                                holder.commentdownvoteAmount.setText(String.valueOf(modelFeedComment.getDownvotes()));
                                db.collection("Feeds").document(FeedID).collection("Comments").document(modelFeedComment.getId()).set(modelFeedComment);
                            }
                        });
                        holder.commentdownvoteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (downvotedusers.contains(userid)) {
                                    holder.commentdownVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                                    holder.commentdownvoteAmount.setTextColor(Color.parseColor("#000000"));
                                    downvotedusers.remove(userid);
                                    modelFeedComment.setDownvotes(modelFeedComment.getDownvotes() - 1);
                                } else {
                                    holder.commentdownVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_down_orange_24dp);
                                    holder.commentdownvoteAmount.setTextColor(Color.parseColor("#C85D30"));
                                    downvotedusers.add(userid);
                                    modelFeedComment.setDownvotes(modelFeedComment.getDownvotes() + 1);
                                    if (upvotedusers.contains(userid)) {
                                        holder.commentupVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                                        holder.commentupvoteAmount.setTextColor(Color.parseColor("#000000"));
                                        upvotedusers.remove(userid);
                                        modelFeedComment.setUpvotes(modelFeedComment.getUpvotes() - 1);
                                    }
                                }
                                modelFeedComment.setDownvotedUsers(downvotedusers);
                                modelFeedComment.setUpvotedUsers(upvotedusers);
                                holder.commentupvoteAmount.setText(String.valueOf(modelFeedComment.getUpvotes()));
                                holder.commentdownvoteAmount.setText(String.valueOf(modelFeedComment.getDownvotes()));
                                db.collection("Feeds").document(FeedID).collection("Comments").document(modelFeedComment.getId()).set(modelFeedComment);
                            }
                        });
                        if (modelFeedComment.getPostPic() != null) {
                            Uri commentPic = Uri.parse(modelFeedComment.getPostPic());
                            glide.load(commentPic).into(holder.commentPic);
                            holder.commentPic.setVisibility(View.VISIBLE);
                        }
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return modelFeedCommentArrayList.size();
    }

    private void loading(MyViewHolder holder) {
        holder.comments.setText("");
        holder.commenttime.setText("");
        holder.commenter.setText("");
        holder.commentupvoteAmount.setText("");
        holder.commentdownvoteAmount.setText("");
        holder.imgView_proPic.setImageDrawable(null);
        holder.commentupVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
        holder.commentupvoteAmount.setTextColor(Color.parseColor("#000000"));
        holder.commentdownVoteImage.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        holder.commentdownvoteAmount.setTextColor(Color.parseColor("#000000"));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commenter, comments, commentupvoteAmount, commentdownvoteAmount, commenttime;
        ImageView imgView_proPic, commentPic, commentupVoteImage, commentdownVoteImage;
        LinearLayout commentupvoteBtn, commentdownvoteBtn;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgView_proPic = (ImageView) itemView.findViewById(R.id.feedProPic);
            commentPic = (ImageView) itemView.findViewById(R.id.commentPic);
            commentupVoteImage = (ImageView) itemView.findViewById(R.id.commentupVoteImage);
            commentdownVoteImage = (ImageView) itemView.findViewById(R.id.commentdownVoteImage);

            commenter = (TextView) itemView.findViewById(R.id.commenter);
            comments = (TextView) itemView.findViewById(R.id.comments);
            commentupvoteAmount = (TextView) itemView.findViewById(R.id.commentupvoteAmount);
            commentdownvoteAmount = (TextView) itemView.findViewById(R.id.commentdownvoteAmount);
            commenttime = (TextView) itemView.findViewById(R.id.commenttime);
            commentupvoteBtn = (LinearLayout) itemView.findViewById(R.id.commentupvoteBtn);
            commentdownvoteBtn = (LinearLayout) itemView.findViewById(R.id.commentdownvoteBtn);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }

    }
}

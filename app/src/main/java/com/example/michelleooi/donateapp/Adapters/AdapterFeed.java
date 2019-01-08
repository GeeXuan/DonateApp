package com.example.michelleooi.donateapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Activities.FeedCommentActivity;
import com.example.michelleooi.donateapp.Models.ModelFeed;
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


public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private Context context;
    private ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    private RequestManager glide;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userid = mAuth.getUid();

    public AdapterFeed(FragmentActivity context, ArrayList<ModelFeed> modelFeedArrayList) {

        this.context = context;
        this.modelFeedArrayList = modelFeedArrayList;
        glide = Glide.with(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        loading(holder);
        final List<String> upvotedusers, downvotedusers;
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.viewPager.removeAllViews();
        holder.viewPager.setVisibility(View.GONE);
        final ModelFeed modelFeed = modelFeedArrayList.get(position);
        if (modelFeed.getUpvotedUsers() != null)
            upvotedusers = modelFeed.getUpvotedUsers();
        else
            upvotedusers = new ArrayList<>();
        if (modelFeed.getDownvotedUsers() != null)
            downvotedusers = modelFeed.getDownvotedUsers();
        else
            downvotedusers = new ArrayList<>();
        CollectionReference userRef = db.collection("Users");
        userRef.document(modelFeed.getUserid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ModelUser modelUser = documentSnapshot.toObject(ModelUser.class);
                        holder.sliderDotspanel.removeAllViews();

                        holder.feedUserName.setText(modelUser.getName());
                        holder.feedPostDate.setText(String.format("Posted on: %s", new SimpleDateFormat("HH:mm dd-MM-yyyy").format(modelFeed.getPostTime())));
                        holder.feedUpvoteAmount.setText(String.valueOf(modelFeed.getUpvotes()));
                        holder.feedDownvoteAmount.setText(String.valueOf(modelFeed.getDownvotes()));

                        if (upvotedusers.contains(userid)) {
                            holder.feedUpvoteImage.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                            holder.feedUpvoteAmount.setTextColor(Color.parseColor("#00B0FF"));
                        }
                        if (downvotedusers.contains(userid)) {
                            holder.feedDownvoteImage.setImageResource(R.drawable.ic_thumb_down_orange_24dp);
                            holder.feedDownvoteAmount.setTextColor(Color.parseColor("#C85D30"));
                        }
                        holder.feedCommentAmount.setText(String.valueOf(modelFeed.getCommentNo()));
                        if (modelFeed.getText() != null) {
                            holder.feedText.setText(modelFeed.getText());
                            holder.feedText.setVisibility(View.VISIBLE);
                        } else {
                            holder.feedText.setVisibility(View.GONE);
                        }

                        Uri uri = Uri.parse(modelUser.getProPic());
                        glide.load(uri).into(holder.imgView_proPic);
                        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, FeedCommentActivity.class);
                                intent.putExtra("FeedID", modelFeed.getId());
                                context.startActivity(intent);
                            }
                        });
                        holder.upvoteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (upvotedusers.contains(userid)) {
                                    holder.feedUpvoteImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                    holder.feedUpvoteAmount.setTextColor(Color.parseColor("#000000"));
                                    upvotedusers.remove(userid);
                                    modelFeed.setUpvotes(modelFeed.getUpvotes() - 1);
                                } else {
                                    holder.feedUpvoteImage.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                                    holder.feedUpvoteAmount.setTextColor(Color.parseColor("#00B0FF"));
                                    upvotedusers.add(userid);
                                    modelFeed.setUpvotes(modelFeed.getUpvotes() + 1);
                                    if (downvotedusers.contains(userid)) {
                                        holder.feedDownvoteImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                                        holder.feedDownvoteAmount.setTextColor(Color.parseColor("#000000"));
                                        downvotedusers.remove(userid);
                                        modelFeed.setDownvotes(modelFeed.getDownvotes() - 1);
                                    }
                                }
                                modelFeed.setDownvotedUsers(downvotedusers);
                                modelFeed.setUpvotedUsers(upvotedusers);
                                holder.feedUpvoteAmount.setText(String.valueOf(modelFeed.getUpvotes()));
                                holder.feedDownvoteAmount.setText(String.valueOf(modelFeed.getDownvotes()));
                                db.collection("Feeds").document(modelFeed.getId()).set(modelFeed);
                            }
                        });
                        holder.downvoteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (downvotedusers.contains(userid)) {
                                    holder.feedDownvoteImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                                    holder.feedDownvoteAmount.setTextColor(Color.parseColor("#000000"));
                                    downvotedusers.remove(userid);
                                    modelFeed.setDownvotes(modelFeed.getDownvotes() - 1);
                                } else {
                                    holder.feedDownvoteImage.setImageResource(R.drawable.ic_thumb_down_orange_24dp);
                                    holder.feedDownvoteAmount.setTextColor(Color.parseColor("#C85D30"));
                                    downvotedusers.add(userid);
                                    modelFeed.setDownvotes(modelFeed.getDownvotes() + 1);
                                    if (upvotedusers.contains(userid)) {
                                        holder.feedUpvoteImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                                        holder.feedUpvoteAmount.setTextColor(Color.parseColor("#000000"));
                                        upvotedusers.remove(userid);
                                        modelFeed.setUpvotes(modelFeed.getUpvotes() - 1);
                                    }
                                }
                                modelFeed.setDownvotedUsers(downvotedusers);
                                modelFeed.setUpvotedUsers(upvotedusers);
                                holder.feedUpvoteAmount.setText(String.valueOf(modelFeed.getUpvotes()));
                                holder.feedDownvoteAmount.setText(String.valueOf(modelFeed.getDownvotes()));
                                db.collection("Feeds").document(modelFeed.getId()).set(modelFeed);
                            }
                        });
                        if (modelFeed.getPostPic() != null && !modelFeed.getPostPic().isEmpty()) {
                            holder.viewPager.setVisibility(View.VISIBLE);
                            holder.sliderDotspanel.setVisibility(View.VISIBLE);
                            ArrayList<Uri> uriImage = new ArrayList();
                            for (String postpic : modelFeed.getPostPic()) {
                                uri = Uri.parse(postpic);
                                uriImage.add(uri);
                            }
                            AdapterFeedImage adapterFeedImage = new AdapterFeedImage(context, uriImage);
                            holder.viewPager.setAdapter(adapterFeedImage);
                            holder.dotscount = adapterFeedImage.getCount();
                            holder.dots = new ImageView[holder.dotscount];
                            for (int i = 0; i < holder.dotscount; i++) {
                                holder.dots[i] = new ImageView(context);
                                holder.dots[i].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.nonactive_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);
                                holder.sliderDotspanel.addView(holder.dots[i], params);
                            }
                            holder.dots[0].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.active_dot));

                            holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    for (int i = 0; i < holder.dotscount; i++) {
                                        holder.dots[i].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.nonactive_dot));
                                    }

                                    holder.dots[position].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.active_dot));
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void loading(MyViewHolder holder) {
        holder.feedText.setText("");
        holder.feedPostDate.setText("");
        holder.feedUserName.setText("");
        holder.feedUpvoteAmount.setText("");
        holder.feedDownvoteAmount.setText("");
        holder.feedCommentAmount.setText("");
        holder.imgView_proPic.setImageDrawable(null);
        holder.sliderDotspanel.setVisibility(View.GONE);
        holder.feedDownvoteImage.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        holder.feedDownvoteAmount.setTextColor(Color.parseColor("#000000"));
        holder.feedUpvoteImage.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        holder.feedUpvoteAmount.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public int getItemCount() {
        return modelFeedArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView feedUserName, feedPostDate, feedUpvoteAmount, feedDownvoteAmount, feedCommentAmount, feedText;
        ImageView imgView_proPic, feedUpvoteImage, feedDownvoteImage;
        ViewPager viewPager;
        LinearLayout sliderDotspanel;
        int dotscount;
        ImageView[] dots;
        RelativeLayout commentBtn, upvoteBtn, downvoteBtn;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgView_proPic = (ImageView) itemView.findViewById(R.id.feedProPic);
            feedUpvoteImage = (ImageView) itemView.findViewById(R.id.feedUpvoteImage);
            feedDownvoteImage = (ImageView) itemView.findViewById(R.id.feedDownvoteImage);

            viewPager = (ViewPager) itemView.findViewById(R.id.viewPager);
            sliderDotspanel = (LinearLayout) itemView.findViewById(R.id.SliderDots);
            feedUserName = (TextView) itemView.findViewById(R.id.feedUserName);
            feedPostDate = (TextView) itemView.findViewById(R.id.feedPostDate);
            feedUpvoteAmount = (TextView) itemView.findViewById(R.id.feedUpvoteAmount);
            feedDownvoteAmount = (TextView) itemView.findViewById(R.id.feedDownvoteAmount);
            feedCommentAmount = (TextView) itemView.findViewById(R.id.feedCommentAmount);
            feedText = (TextView) itemView.findViewById(R.id.feedText);
            commentBtn = (RelativeLayout) itemView.findViewById(R.id.row_commentBtn);
            upvoteBtn = (RelativeLayout) itemView.findViewById(R.id.row_upvoteBtn);
            downvoteBtn = (RelativeLayout) itemView.findViewById(R.id.row_downvoteBtn);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}

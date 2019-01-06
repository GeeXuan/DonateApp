package com.example.michelleooi.donateapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Models.ModelFeedComment;
import com.example.michelleooi.donateapp.R;

import java.util.ArrayList;

public class AdapterFeedComment extends RecyclerView.Adapter<AdapterFeedComment.MyViewHolder> {

    Context context;
    ArrayList<ModelFeedComment> modelFeedCommentArrayList = new ArrayList<>();
    RequestManager glide;

    public AdapterFeedComment(FragmentActivity context, ArrayList<ModelFeedComment> modelFeedCommentArrayList) {
        this.context = context;
        this.modelFeedCommentArrayList = modelFeedCommentArrayList;
        glide = Glide.with(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feedcomment, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelFeedComment modelFeedComment = modelFeedCommentArrayList.get(position);

        holder.commenter.setText(modelFeedComment.getName());
        holder.comments.setText(modelFeedComment.getComments());
        holder.commentlike.setText("Likes (" + modelFeedComment.getLikes() + ")");
        holder.commenttime.setText(modelFeedComment.getTime());
        glide.load(modelFeedComment.getPropic()).into(holder.imgView_proPic);
    }

    @Override
    public int getItemCount() {
        return modelFeedCommentArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView commenter, comments, commentlike, commenttime;
        ImageView imgView_proPic;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgView_proPic = (ImageView) itemView.findViewById(R.id.imgView_proPic);

            commenter = (TextView) itemView.findViewById(R.id.commenter);
            comments = (TextView) itemView.findViewById(R.id.comments);
            commentlike = (TextView) itemView.findViewById(R.id.commentlike);
            commenttime = (TextView) itemView.findViewById(R.id.commenttime);
        }

    }
}

package com.example.michelleooi.donateapp.Adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.R;

import java.util.ArrayList;

public class AdapterFeedImage extends RecyclerView.Adapter<AdapterFeedImage.MyViewHolder> {

    Context context;
    ArrayList<Bitmap> images = new ArrayList<>();
    RequestManager glide;

    public AdapterFeedImage(FragmentActivity context, ArrayList<Bitmap> images) {

        this.context = context;
        this.images = images;
        glide = Glide.with(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_add_post_image, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterFeedImage.MyViewHolder holder, int position) {
        Bitmap image = images.get(position);

        glide.load(image).into(holder.imageToUpload);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageToUpload;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageToUpload = (ImageView) itemView.findViewById(R.id.imageToUpload);
        }
    }
}

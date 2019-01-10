package com.example.michelleooi.donateapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class AdapterFeedImage extends PagerAdapter {

    private Context mContext;
    private ArrayList<Uri> uriArrayList = new ArrayList();
    RequestManager glide;

    public AdapterFeedImage(Context context, ArrayList<Uri> uriList) {
        this.mContext = context;
        this.uriArrayList = uriList;
        glide = Glide.with(context);
    }

    @Override
    public int getCount() {
        return uriArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        glide = Glide.with(mContext);
        glide.load(uriArrayList.get(position)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}

package com.example.michelleooi.donateapp.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.michelleooi.donateapp.Adapters.AdapterFeed;
import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.R;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    RecyclerView feedRecyclerView;
    ImageButton bUploadImage;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    EditText feedAddPostText;
    Button btnPostFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
        ((HomeActivity) getActivity()).setActionBarTitle("News Feed");

        feedRecyclerView = getActivity().findViewById(R.id.feedRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        feedRecyclerView.setLayoutManager(layoutManager);

        adapterFeed = new AdapterFeed(getActivity(), modelFeedArrayList);
        feedRecyclerView.setAdapter(adapterFeed);

        populateFeedRecyclerView();
        btnPostFeed = getActivity().findViewById(R.id.btnPostFeed);
        feedAddPostText = getActivity().findViewById(R.id.feedAddPostText);
        feedAddPostText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    btnPostFeed.setVisibility(View.VISIBLE);
                } else {
                    btnPostFeed.setVisibility(View.GONE);
                }
            }
        });

        bUploadImage = getActivity().findViewById(R.id.imageButton);
        bUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPhotoDialog dialog = new UploadPhotoDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "exampleBottomSheet");
            }
        });
    }

    public void populateFeedRecyclerView() {

        ModelFeed modelFeed = new ModelFeed(1, 9, 2, R.drawable.ic_propic1, R.drawable.img_post1,
                "Sajin Maharjan", "2 hrs", "The cars we drive say a lot about us.");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(2, 26, 6, R.drawable.ic_propic2, 0,
                "Karun Shrestha", "9 hrs", "Don't be afraid of your fears. They're not there to scare you. They're there to \n" +
                "let you know that something is worth it.");
        modelFeedArrayList.add(modelFeed);
        modelFeed = new ModelFeed(3, 17, 5, R.drawable.ic_propic3, R.drawable.img_post2,
                "Lakshya Ram", "13 hrs", "That reflection!!!");
        modelFeedArrayList.add(modelFeed);

        adapterFeed.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item1 = menu.add(Menu.NONE, Menu.NONE, 1, "Category");
        item1.setIcon(R.drawable.ic_list_white_24dp);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

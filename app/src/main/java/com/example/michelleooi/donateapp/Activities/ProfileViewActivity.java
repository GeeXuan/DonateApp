package com.example.michelleooi.donateapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Models.ModelUser;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewActivity extends AppCompatActivity {

    RequestManager glide;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String Uid;
    private ModelUser modelUser;
    private final static int UPDATEPROFILE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        setTitle("Profile");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Uid = getIntent().getStringExtra("Uid");
        showUser();

        TextView editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileViewActivity.this, UpdateActivity.class);
                startActivityForResult(intent, UPDATEPROFILE);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    private void showUser() {
        if (Uid == null) {
            Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                modelUser = task.getResult().toObject(ModelUser.class);
                Uri userProPic = Uri.parse(modelUser.getProPic());
                String userName = modelUser.getName();
                String userEmail = modelUser.getEmail();

                ImageView mUserProPic = (ImageView) findViewById(R.id.userProPic);
                TextView mUserName = (TextView) findViewById(R.id.userName);
                TextView mUserEmail = (TextView) findViewById(R.id.userEmail);

                glide = Glide.with(ProfileViewActivity.this);
                glide.load(userProPic).into(mUserProPic);
                mUserName.setText(userName);
                mUserEmail.setText(userEmail);
            }
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putString("userid", Uid);
                    FeedHistoryTab feedHistoryTab = new FeedHistoryTab();
                    feedHistoryTab.setArguments(bundle);
                    return feedHistoryTab;
                case 1:
                    bundle.putString("userid", Uid);
                    DonateHistoryTab donateHistoryTab = new DonateHistoryTab();
                    donateHistoryTab.setArguments(bundle);
                    return donateHistoryTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATEPROFILE) {
            if (requestCode == RESULT_OK) {
                showUser();
            }
        }
    }
}

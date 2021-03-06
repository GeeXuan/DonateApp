package com.example.michelleooi.donateapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.michelleooi.donateapp.Models.ModelUser;
import com.example.michelleooi.donateapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private DrawerLayout drawer;
    RequestManager glide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db.collection("Users").whereEqualTo("id", user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ModelUser modelUser = queryDocumentSnapshots.getDocuments().get(0).toObject(ModelUser.class);
                if (modelUser.getRole().equals("Staff")) {
                    Menu menu = navigationView.getMenu();
                    SubMenu subMenu = menu.addSubMenu("Staff Actions");
                    subMenu.add(0, 99, 0, "Approve Event");
                }
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        showUser(navigationView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FeedFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_newsfeed);
        }
    }

    private void showUser(NavigationView navigationView) {
        Uri userProPic = null;
        if (user.getPhotoUrl() != null) {
            userProPic = user.getPhotoUrl();
        }
        String userName = user.getDisplayName();
        String userEmail = user.getEmail();

        View header = navigationView.getHeaderView(0);
        ImageView mUserProPic = (ImageView) header.findViewById(R.id.userProPic);
        TextView mUserName = (TextView) header.findViewById(R.id.userName);
        TextView mUserEmail = (TextView) header.findViewById(R.id.userEmail);

        if (userProPic != null) {
            glide = Glide.with(this);
            glide.load(userProPic).into(mUserProPic);
        }
        mUserName.setText(userName);
        mUserEmail.setText(userEmail);

        mUserProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileViewActivity.class);
                intent.putExtra("userid", user.getUid());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_newsfeed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FeedFragment()).commit();
                drawer.closeDrawers();
                break;
            case R.id.nav_event:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EventFragment()).commit();
                drawer.closeDrawers();
                break;
            case 99:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ManageEventFragment()).commit();
                drawer.closeDrawers();
            default:
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}

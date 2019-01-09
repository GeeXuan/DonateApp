package com.example.michelleooi.donateapp.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.michelleooi.donateapp.Models.ModelFeed;
import com.example.michelleooi.donateapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private final static int POST_ACTIVITY = 5;
    ArrayList<ModelFeed> modelFeedArrayList = new ArrayList<>();
    Button btnPostFeed;
    SwipeRefreshLayout swipeLayout;
    TextView emptyText;
    RelativeLayout container;
    Spinner spinner;


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

        container = getActivity().findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.areas, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("area", adapterView.getItemAtPosition(i).toString());
                bundle.putString("direction", "descending");
                FeedTab feedTab = new FeedTab();
                feedTab.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, feedTab)
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Bundle bundle = new Bundle();
                bundle.putString("area", spinner.getSelectedItem().toString());
                bundle.putString("direction", "descending");
                FeedTab feedTab = new FeedTab();
                feedTab.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, feedTab)
                        .commit();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item1 = menu.add(Menu.NONE, 999, 1, "Add Post");
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item1.setIcon(R.drawable.ic_add_white_24dp);
        MenuItem item2 = menu.add(Menu.NONE, 998, 2, "Sort By");
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item2.setIcon(R.drawable.ic_sort_white_24dp);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_logout:
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case 998:
                final Bundle bundle = new Bundle();
                bundle.putString("area", spinner.getSelectedItem().toString());
                final FeedTab feedTab = new FeedTab();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Sort By");
                builder.setItems(new String[]{"New To Old", "Old To New"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            bundle.putString("direction", "descending");
                        } else {
                            bundle.putString("direction", "ascending");
                        }
                        feedTab.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, feedTab)
                                .commit();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, feedTab)
                                .commit();
                    }
                });
                builder.show();
                break;
            case 999:
                startActivityForResult(new Intent(getActivity(), PostFeedActivity.class), POST_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle.putString("area", spinner.getSelectedItem().toString());
                FeedTab feedTab = new FeedTab();
                bundle.putString("direction", "descending");
                feedTab.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, feedTab)
                        .commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_test2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}

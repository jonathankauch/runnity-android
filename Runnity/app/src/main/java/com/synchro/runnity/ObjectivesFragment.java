package com.synchro.runnity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Achievements;
import com.synchro.runnity.Models.Events;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.Users;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shirco on 29/02/2016.
 */
public class ObjectivesFragment extends Fragment {
    private Achievements achievements;
    @BindView(R.id.newPostButton)
    FloatingActionButton fab;

    @BindView(R.id.kilometersListView)
    ListView kilometersListView;

    @BindView(R.id.kilometersButton)
    Button kilometersButton;

    @BindView(R.id.speedButton)
    Button speedButton;

    @BindView(R.id.speedListView)
    ListView speedListView;

    @BindView(R.id.caloriesButton)
    Button caloriesButton;

    @BindView(R.id.caloriesListView)
    ListView caloriesListView;

    @BindView(R.id.kilometersLayout)
    LinearLayout kilometersListLayout;

    @BindView(R.id.speedListLayout)
    LinearLayout speedListLayout;

    @BindView(R.id.caloriesListLayout)
    LinearLayout caloriesListLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_objectives, container, false);
        ButterKnife.bind(this, rootView);

        kilometersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kilometersListLayout.getVisibility() == View.GONE) {
                    kilometersButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    kilometersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    kilometersListLayout.setVisibility(View.VISIBLE);
                    speedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    speedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    speedListLayout.setVisibility(View.GONE);
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    caloriesListLayout.setVisibility(View.GONE);
                }
            }
        });

        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speedListLayout.getVisibility() == View.GONE) {
                    speedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    speedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    speedListLayout.setVisibility(View.VISIBLE);
                    kilometersButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    kilometersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    kilometersListLayout.setVisibility(View.GONE);
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    caloriesListLayout.setVisibility(View.GONE);
                }
            }
        });

        caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caloriesListLayout.getVisibility() == View.GONE) {
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    caloriesListLayout.setVisibility(View.VISIBLE);
                    speedButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    speedButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    speedListLayout.setVisibility(View.GONE);
                    kilometersButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    kilometersButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    kilometersListLayout.setVisibility(View.GONE);
                }
            }
        });

        achievements = new Achievements();

        kilometersListView.setAdapter(new AchievementsListAdapter(getContext(), achievements.getAchievements()));
        speedListView.setAdapter(new AchievementsListAdapter(getContext(), achievements.getAchievements_done()));
        caloriesListView.setAdapter(new AchievementsListAdapter(getContext(), achievements.getAchievements_fail()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewObjectiveActivity.class);

                startActivity(intent);
            }
        });

        getAchievements();
        return rootView;
    }


    public void getAchievements() {
        String url = getString(R.string.api_url) + "achievements";

        if (!isNetworkAvailable()) {
            return;
        }

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Ion.with(getContext())
                .load(url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            return;
                        }
                        Log.d("ACHIEVEMENTS RESPONSE", result.toString());
                        try {
                            achievements = new Gson().fromJson(result.toString(), Achievements.class);

                            if (achievements.getAchievements() == null) {
                                return;
                            }

                            Log.d("ACHIEVEMENTS SIZE", String.valueOf(achievements.getAchievements().length));

                            ((AchievementsListAdapter)kilometersListView.getAdapter()).notifyDataSetChanged(achievements.getAchievements());
                            ((AchievementsListAdapter)speedListView.getAdapter()).notifyDataSetChanged(achievements.getAchievements_done());
                            ((AchievementsListAdapter)caloriesListView.getAdapter()).notifyDataSetChanged(achievements.getAchievements_fail());
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResume() {
        getAchievements();
        super.onResume();
    }
}

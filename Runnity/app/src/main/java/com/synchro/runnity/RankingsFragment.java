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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Friends;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.Users;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shirco on 29/02/2016.
 */
public class RankingsFragment extends Fragment {
    private Users kilometersUsers;

    private Users speedUsers;

    private Users caloriesUsers;

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
        View rootView = inflater.inflate(R.layout.fragment_rankings, container, false);
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

        kilometersUsers = new Users();
        speedUsers = new Users();
        caloriesUsers = new Users();

        kilometersListView.setAdapter(new RankingListAdapter(getContext(), kilometersUsers, 0));
        speedListView.setAdapter(new RankingListAdapter(getContext(), speedUsers, 1));
        caloriesListView.setAdapter(new RankingListAdapter(getContext(), caloriesUsers, 2));

        getRankings();
        return rootView;
    }

    public void getRankings() {
        if (!isNetworkAvailable()) {
            return;
        }

        String url = getString(R.string.api_url) + "ranking?filter=kilometer";

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
                        Log.d("RANKING RESPONSE", result.toString());
                        try {
                            kilometersUsers = new Gson().fromJson(result.toString(), Users.class);

                            if (kilometersUsers.getFriends() == null) {
                                return;
                            }

                            Log.d("RANKING SIZE", String.valueOf(kilometersUsers.getFriends().length));

                            ((RankingListAdapter)kilometersListView.getAdapter()).notifyDataSetChanged(kilometersUsers);
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });


        url = getString(R.string.api_url) + "ranking?filter=speed";

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
                        Log.d("RANKING RESPONSE", result.toString());
                        try {
                            speedUsers = new Gson().fromJson(result.toString(), Users.class);

                            if (speedUsers.getFriends() == null) {
                                return;
                            }

                            Log.d("RANKING SIZE", String.valueOf(speedUsers.getFriends().length));

                            ((RankingListAdapter)speedListView.getAdapter()).notifyDataSetChanged(speedUsers);
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });


        url = getString(R.string.api_url) + "ranking?filter=calorie";

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
                        Log.d("RANKING RESPONSE", result.toString());
                        try {
                            caloriesUsers = new Gson().fromJson(result.toString(), Users.class);

                            if (caloriesUsers.getFriends() == null) {
                                return;
                            }

                            Log.d("RANKING SIZE", String.valueOf(caloriesUsers.getFriends().length));

                            ((RankingListAdapter)caloriesListView.getAdapter()).notifyDataSetChanged(caloriesUsers);
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




}

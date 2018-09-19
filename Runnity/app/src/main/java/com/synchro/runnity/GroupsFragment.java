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
import com.synchro.runnity.Models.AllUsers;
import com.synchro.runnity.Models.Events;
import com.synchro.runnity.Models.Groups;
import com.synchro.runnity.Models.Notifications;
import com.synchro.runnity.Models.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shirco on 29/02/2016.
 */
public class GroupsFragment extends Fragment {
    private ListView listView;
    private Groups groups;
    private Notifications notifications;
    private AllUsers allUsers;

    @BindView(R.id.newPostButton)
    FloatingActionButton fab;

    @BindView(R.id.invitationsListView)
    ListView invitationsListView;

    @BindView(R.id.eventsButton)
    Button eventsButton;

    @BindView(R.id.invitationsButton)
    Button invitationsButton;

    @BindView(R.id.invitationsLayout)
    LinearLayout invitationsLayout;

    @BindView(R.id.eventsLayout)
    LinearLayout eventsLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, rootView);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewGroupActivity.class);

                startActivity(intent);
            }
        });
        Button button = (Button) rootView.findViewById(R.id.pauseButton);
        listView = (ListView) rootView.findViewById(R.id.eventsListView);

        listView.setAdapter(new GroupsListAdapter(getContext()));

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventsLayout.getVisibility() == View.GONE) {
                    eventsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    eventsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    invitationsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    invitationsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    invitationsLayout.setVisibility(View.GONE);
                    eventsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        invitationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invitationsLayout.getVisibility() == View.GONE) {
                    invitationsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    invitationsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    eventsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    eventsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    eventsLayout.setVisibility(View.GONE);
                    invitationsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        invitationsListView.setAdapter(new GroupsInvitationsListAdapter(getContext(), this));

        getAllUsers();
        getGroups();
        getNotifications();
        return rootView;
    }


    public void getGroups() {
        String url = getString(R.string.api_url) + "groups";

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
                        Log.d("GROUPS RESPONSE", result.toString());
                        try {
                            groups = new Gson().fromJson(result.toString(), Groups.class);

                            if (groups.getGroups() == null) {
                                return;
                            }
                            Singleton.getInstance().setGroups(groups);

                            Log.d("EVENTS SIZE", String.valueOf(groups.getGroups().length));

                            ((GroupsListAdapter)listView.getAdapter()).notifyDataSetChanged();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }


    public void getNotifications() {
        String url = getString(R.string.api_url) + "notifications";

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
                        Log.d("NOTIFICATIONS RESPONSE", result.toString());
                        try {
                            notifications = new Gson().fromJson(result.toString(), Notifications.class);

                            if (notifications.getEvents() == null) {
                                return;
                            }
                            Singleton.getInstance().setNotifications(notifications);

                            Log.d("NOTIFICATIONS SIZE", String.valueOf(notifications.getEvents().length));

                            ((GroupsInvitationsListAdapter)invitationsListView.getAdapter()).notifyDataSetChanged();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    public void getAllUsers() {
        String url = getString(R.string.api_url) + "users";

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
                        Log.d("USERS RESPONSE", result.toString());
                        try {
                            allUsers = new Gson().fromJson(result.toString(), AllUsers.class);

                            if (allUsers.getUsers() == null) {
                                return;
                            }
                            Singleton.getInstance().setAllUsers(allUsers);

                            ((GroupsInvitationsListAdapter)invitationsListView.getAdapter()).notifyDataSetChanged();
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
        getGroups();
        super.onResume();
    }
}

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Friend;
import com.synchro.runnity.Models.Friends;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Singleton;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Shirco on 29/02/2016.
 */
public class FriendsFragment extends Fragment {
    private Friends friends;

    List<String> paths;

    @BindView(R.id.friendsListView)
    ListView friendsListView;

    @BindView(R.id.friendsButton)
    Button friendsButton;

    @BindView(R.id.waitingFriendsButton)
    Button waitingFriendsButton;

    @BindView(R.id.addFriendButton)
    Button addFriendButton;

    @BindView(R.id.waitingFriendsListView)
    ListView waitingListView;

    @BindView(R.id.newFriendButton)
    FloatingActionButton fab;

    @BindView(R.id.newFriendLayout)
    RelativeLayout newFriendLayout;

    @BindView(R.id.friendListLayout)
    LinearLayout friendListLayout;

    @BindView(R.id.waitingFriendListLayout)
    LinearLayout waitingFriendListLayout;

    @BindView(R.id.idNewFriend)
    EditText idNewFriend;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, rootView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newFriendLayout.getVisibility() == View.GONE) {
                    newFriendLayout.setVisibility(View.VISIBLE);
                }
            }
        });



        newFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newFriendLayout.getVisibility() == View.VISIBLE) {
                    newFriendLayout.setVisibility(View.GONE);
                }
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friendListLayout.getVisibility() == View.GONE) {
                    friendsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    friendsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    waitingFriendsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    waitingFriendsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    waitingFriendListLayout.setVisibility(View.GONE);
                    friendListLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        waitingFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waitingFriendListLayout.getVisibility() == View.GONE) {
                    waitingFriendsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    waitingFriendsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    friendsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    friendsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    friendListLayout.setVisibility(View.GONE);
                    waitingFriendListLayout.setVisibility(View.VISIBLE);
                } else {
                    getFriends();
                }
            }
        });


        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFriendLayout.setVisibility(View.GONE);
                if (idNewFriend.getText() != null && idNewFriend.getText().toString().length() > 0) {
                    final JsonObject json = new JsonObject();
                    json.addProperty("user_token", Singleton.getInstance().getToken());
                    json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                    json.addProperty("email", idNewFriend.getText().toString());

                    String url = getString(R.string.api_url) + "friendships";

                    Ion.with(getContext())
                            .load("POST", url)
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e != null || result == null) {

                                        Log.d("INVITE RESPONSE", e.toString());
                                        return;
                                    }
                                    Log.d("INVITE RESPONSE", result.toString());
//                                    Post post = new Gson().fromJson(result.toString(), Post.class);

                                }
                            });
                    idNewFriend.setText("");


                }
            }
        });

        waitingListView.setAdapter(new WaitingFriendsListAdapter(getContext(), this));
        friendsListView.setAdapter(new FriendsListAdapter(getContext()));
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String friendId = friends.getFriends()[position].getId();
                String title = friends.getFriends()[position].getFirstname() + " " + friends.getFriends()[position].getLastname();
                Intent intent = new Intent(getContext(), ConversationActivity.class);

                Log.d("FRIEND ID", friendId);
                intent.putExtra("friend_id", friendId);
                intent.putExtra("title", title);
                getContext().startActivity(intent);
            }
        });

        getFriends();
        return rootView;
    }

    public void getFriends() {
        String url = getString(R.string.api_url) + "friendships";

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
                        Log.d("POSTS RESPONSE", result.toString());
                        try {
                            friends = new Gson().fromJson(result.toString(), Friends.class);

                            if (friends.getFriends() == null) {
                                return;
                            }
                            Singleton.getInstance().setFriends(friends);

                            Log.d("POSTS SIZE", String.valueOf(friends.getFriends().length));

                            ((FriendsListAdapter)friendsListView.getAdapter()).notifyDataSetChanged();
                            ((WaitingFriendsListAdapter)waitingListView.getAdapter()).notifyDataSetChanged();
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

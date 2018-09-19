package com.synchro.runnity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Friends;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Singleton;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class WaitingFriendsListAdapter extends BaseAdapter {
    protected Context mContext;
    protected FriendsFragment mFragment;
    Friends friends;
    public WaitingFriendsListAdapter(Context c, FriendsFragment fragment) {
        this.mContext = c;
        this.mFragment = fragment;
        if (Singleton.getInstance().getPosts() == null) {
            Posts tmpPosts = new Posts();
            tmpPosts.setPosts(new Post[0]);
            Singleton.getInstance().setPosts(tmpPosts);

        }
        this.friends = Singleton.getInstance().getFriends();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name;
        Button accept;
        Button refuse;
    }

    public int getCount() {
        if (friends == null || friends.getRequests() == null)
            return 0;
        return friends.getRequests().length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(LeftMenuActivity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = mInflater.inflate(R.layout.new_friend_cell, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.accept = (Button) convertView.findViewById(R.id.acceptButton);
            holder.refuse = (Button) convertView.findViewById(R.id.refuseButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(friends.getRequests()[position].getFirstname() + " " + friends.getRequests()[position].getLastname());

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mContext.getString(R.string.api_url) + "friendships/" + friends.getRequests()[position].getRequests_id() + "/reject";
                final JsonObject json = new JsonObject();
                json.addProperty("user_token", Singleton.getInstance().getToken());
                json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                json.addProperty("user_id", friends.getRequests()[position].getId());
                Ion.with(mContext)
                        .load("POST", url)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                mFragment.getFriends();
                                if (result == null) {
                                    return;
                                }
                            }
                        });

            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mContext.getString(R.string.api_url) + "friendships/"
                        + friends.getRequests()[position].getRequests_id() + "/accept";
                final JsonObject json = new JsonObject();
                json.addProperty("user_token", Singleton.getInstance().getToken());
                json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                json.addProperty("user_id", friends.getRequests()[position].getId());
                Ion.with(mContext)
                        .load("POST", url)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                mFragment.getFriends();
                                if (result == null) {
                                    return;
                                }
                            }
                        });

            }
        });
       return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        friends = Singleton.getInstance().getFriends();
        super.notifyDataSetChanged();
    }
}
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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Event;
import com.synchro.runnity.Models.Friends;
import com.synchro.runnity.Models.Notifications;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Singleton;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class EventsInvitationsListAdapter extends BaseAdapter {
    protected Context mContext;
    protected EventsFragment mFragment;
    Notifications notifications;
    public EventsInvitationsListAdapter(Context c, EventsFragment fragment) {
        this.mContext = c;
        this.mFragment = fragment;
        this.notifications = Singleton.getInstance().getNotifications();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name;
        Button accept;
        Button refuse;
    }

    public int getCount() {
        if (notifications == null || notifications.getEvents() == null)
            return 0;
        return notifications.getEvents().length;
    }

    public String findEventName(String id) {
        for (int i = 0; i < Singleton.getInstance().getEvents().getEvents().length;i++) {
            if (id.equals(Singleton.getInstance().getEvents().getEvents()[i].get_id())) {
                return Singleton.getInstance().getEvents().getEvents()[i].getName();
            }
        }
        return "";
    }


    public String findUserName(String id) {
        for (int i = 0; i < Singleton.getInstance().getAllUsers().getUsers().length;i++) {
            if (id.equals(Singleton.getInstance().getAllUsers().getUsers()[i].getId())) {
                return Singleton.getInstance().getAllUsers().getUsers()[i].getFirstname() + " " + Singleton.getInstance().getAllUsers().getUsers()[i].getLastname();
            }
        }
        return "";
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


        holder.name.setText(findUserName(notifications.getEvents()[position].getApplicant()) + " souhaite rejoindre : " + findEventName(notifications.getEvents()[position].getTarget_id()));

        holder.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mContext.getString(R.string.api_url) + "member_requests/"
                        + notifications.getEvents()[position].getRequest_id() + "/reject";
                BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
                BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
                Log.d("PTIN DE CHAOHUI URL", url);
                Ion.with(mContext)
                        .load("POST", url)
                        .setHeader(tokenPair, emailPair)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                if (result != null)
                                    Log.d("PTIN DE CHAOHUI", result.toString());
                                else
                                    Log.d("PTIN DE CHAOHUI", e.toString());
                                mFragment.getEvents();
                                mFragment.getNotifications();
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
                String url = mContext.getString(R.string.api_url) + "member_requests/"
                        + (notifications.getEvents()[position].getRequest_id() + "/accept");
                BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
                BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
                Ion.with(mContext)
                        .load("POST", url)
                        .setHeader(tokenPair, emailPair)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                mFragment.getEvents();
                                mFragment.getNotifications();
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
        notifications = Singleton.getInstance().getNotifications();
        super.notifyDataSetChanged();
    }
}
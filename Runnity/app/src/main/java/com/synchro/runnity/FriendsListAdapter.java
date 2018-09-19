package com.synchro.runnity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synchro.runnity.Models.Friends;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class FriendsListAdapter extends BaseAdapter {
    protected Context mContext;
    Friends friends;
    public FriendsListAdapter(Context c) {
        this.mContext = c;
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
    }

    public int getCount() {
        if (friends == null || friends.getFriends() == null)
            return 0;
        return friends.getFriends().length;
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
            convertView = mInflater.inflate(R.layout.friend_cell, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(friends.getFriends()[position].getFirstname() + " " + friends.getFriends()[position].getLastname());

       return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        friends = Singleton.getInstance().getFriends();
        super.notifyDataSetChanged();
    }
}
package com.synchro.runnity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Posts;
import com.synchro.runnity.Models.Post;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class PostsListAdapter extends BaseAdapter {
    protected Context mContext;
    Posts posts;
    public PostsListAdapter(Context c) {
        this.mContext = c;
        if (Singleton.getInstance().getPosts() == null) {
            Posts tmpPosts = new Posts();
            tmpPosts.setPosts(new Post[0]);
            Singleton.getInstance().setPosts(tmpPosts);

        }
        this.posts = Singleton.getInstance().getPosts();
    }

    public PostsListAdapter(Context c, Posts posts) {
        this.mContext = c;
        if (Singleton.getInstance().getPosts() == null) {
            Posts tmpPosts = new Posts();
            tmpPosts.setPosts(new Post[0]);
            Singleton.getInstance().setPosts(tmpPosts);

        }
        this.posts = posts;
    }


    /*private view holder class*/
    private class ViewHolder {
        TextView content;
        TextView date;
        TextView name;
        TextView likes;
        ImageView image;
    }

    public int getCount() {
        return posts.getPosts().length;
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
            convertView = mInflater.inflate(R.layout.post_cell, null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.contentTextView);
            holder.date = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.likes = (TextView) convertView.findViewById(R.id.likesTextView);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (posts.getPosts()[position].getPicture() != null) {
            Ion.with(mContext).load(posts.getPosts()[position].getPicture()).intoImageView(holder.image);
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }

        String string = posts.getPosts()[position].getDate();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        if (string != null)
            try {
                DateFormat format2 = new SimpleDateFormat("\nMMMM d, yyyy", Locale.ENGLISH);
                Date date = format.parse(string);
                String dateString = format2.format(date);
                holder.date.setText(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        holder.name.setText(posts.getPosts()[position].getUser().getFull_name());
        holder.likes.setText("Likes : " + posts.getPosts()[position].getLikes());
        holder.content.setText(posts.getPosts()[position].getContent());

       return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        posts = Singleton.getInstance().getPosts();
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(Posts posts) {
        this.posts = posts;
        super.notifyDataSetChanged();
    }

}
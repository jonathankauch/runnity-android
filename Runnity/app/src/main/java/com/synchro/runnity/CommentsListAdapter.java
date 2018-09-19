package com.synchro.runnity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synchro.runnity.Models.Comment;
import com.synchro.runnity.Models.Comments;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class CommentsListAdapter extends BaseAdapter {
    protected Context mContext;
    Comment[] comments;
    public CommentsListAdapter(Context c, Comment[] comments) {
        this.mContext = c;
        this.comments = comments;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView content;
        TextView date;
        TextView name;
    }

    public int getCount() {
        return comments.length;
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
            convertView = mInflater.inflate(R.layout.comment_cell, null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.contentTextView);
            holder.date = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String string = comments[position].getDate();
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

        holder.content.setText(comments[position].getContent());
        holder.name.setText(comments[position].getUser_fullname());

       return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
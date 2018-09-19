package com.synchro.runnity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Message;
import com.synchro.runnity.Models.Messages;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class MessagesListAdapter extends BaseAdapter {
    protected Context mContext;
    Messages messages;

    public MessagesListAdapter(Context c, Messages messages) {
        this.mContext = c;
        this.messages = messages;
    }


    /*private view holder class*/
    private class ViewHolder {
        LinearLayout yourMessageLayout;
        LinearLayout otherMessageLayout;
        TextView yourMessage;
        TextView otherMessage;
        TextView otherDate;
        TextView yourDate;
    }

    public int getCount() {
        return messages.getMessages().length;
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
            convertView = mInflater.inflate(R.layout.message_cell, null);
            holder = new ViewHolder();
            holder.yourMessage = (TextView) convertView.findViewById(R.id.yourMessage);
            holder.otherMessage = (TextView) convertView.findViewById(R.id.otherMessage);
            holder.yourMessageLayout = (LinearLayout) convertView.findViewById(R.id.yourMessageLayout);
            holder.otherMessageLayout = (LinearLayout) convertView.findViewById(R.id.otherMessageLayout);
            holder.otherDate = (TextView) convertView.findViewById(R.id.otherDate);
            holder.yourDate = (TextView) convertView.findViewById(R.id.yourDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (messages.getMessages()[position].getUser_id().equals(Singleton.getInstance().getId())) {
            holder.otherMessageLayout.setVisibility(View.GONE);
            holder.yourMessageLayout.setVisibility(View.VISIBLE);
            holder.yourMessage.setText(messages.getMessages()[position].getBody());
            String string = messages.getMessages()[position].getDate();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
            if (string != null)
                try {
                    DateFormat format2 = new SimpleDateFormat("'Le' d/MM/yyyy 'à' HH:mm:ss", Locale.ENGLISH);
                    Date date = format.parse(string);
                    String dateString = format2.format(date);
                    holder.yourDate.setText(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        } else {
            holder.otherMessageLayout.setVisibility(View.VISIBLE);
            holder.yourMessageLayout.setVisibility(View.GONE);
            holder.otherMessage.setText(messages.getMessages()[position].getBody());
            String string = messages.getMessages()[position].getDate();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
            if (string != null)
                try {
                    DateFormat format2 = new SimpleDateFormat("'Le' d/MM/yyyy 'à' HH:mm:ss", Locale.ENGLISH);
                    Date date = format.parse(string);
                    String dateString = format2.format(date);
                    holder.otherDate.setText(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }

       return convertView;
    }

    public void notifyDataSetChanged(Messages messages) {
        this.messages = messages;
        super.notifyDataSetChanged();
    }

}
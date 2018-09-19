package com.shirco.calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class EventListAdapter extends BaseAdapter {
    protected Context mContext;
    public EventListAdapter(Context c) {
        this.mContext = c;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView description;
        TextView date;
        ImageButton deleteButton;
        Button modifyButton;
    }

    public int getCount() {
        return Singleton.getInstance().getSearchedEvents().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LinearLayout view;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = mInflater.inflate(R.layout.event_cell, null);
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.descriptionTextView);
            holder.date = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.deleteButton);
            holder.modifyButton = (Button) convertView.findViewById(R.id.modifyButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set event description and date in cell TextViews
        holder.description.setText(Singleton.getInstance().getSearchedEvents().get(position).getDescription());
        holder.date.setText(Singleton.getInstance().getSearchedEvents().get(position).getStrDate());

        // Click listeners
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete event from searched list and main list
                Singleton.getInstance().getEvents()
                        .remove(Singleton.getInstance().getEvents().indexOf(Singleton.getInstance().getSearchedEvents().get(position)));
                Singleton.getInstance().getSearchedEvents().remove(position);

                notifyDataSetChanged();
            }
        });

        holder.modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddEventActivity with id of event so it can modify existing event
                Intent intent = new Intent(mContext, AddEventActivity.class);
                intent.putExtra("id", position);
                mContext.startActivity(intent);
            }
        });

       return convertView;
    }
}
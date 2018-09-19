package com.synchro.runnity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synchro.runnity.Models.Run;
import com.synchro.runnity.Models.Runs;
import com.synchro.runnity.Models.Singleton;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class RunsListAdapter extends BaseAdapter {
    protected Context mContext;
    Runs runs;
    public RunsListAdapter(Context c) {
        this.mContext = c;
        if (Singleton.getInstance().getRuns() == null) {
            Runs tmpRuns = new Runs();
            tmpRuns.setRuns(new Run[0]);
            Singleton.getInstance().setRuns(tmpRuns);

        }
        this.runs = Singleton.getInstance().getRuns();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView title;
        TextView distance;
        TextView speed;
    }

    public int getCount() {
        return runs.getRuns().length;
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
            convertView = mInflater.inflate(R.layout.run_cell, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.titleTextView);
            holder.distance = (TextView) convertView.findViewById(R.id.distanceTextView);
            holder.speed = (TextView) convertView.findViewById(R.id.timeTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.title.setText(runs.getRuns()[position].getName());
        holder.title.setText("Course " + (position + 1));
        String distance = "Distance: " + runs.getRuns()[position].getTotal_distance() + " m";
            holder.distance.setText(distance);
        String speed = "Vitesse max: " + String.format("%.1f", runs.getRuns()[position].getMax_speed()) + " km/h";
        holder.speed.setText(speed);


       return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        runs = Singleton.getInstance().getRuns();
        super.notifyDataSetChanged();
    }
}
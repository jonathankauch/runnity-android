package com.synchro.runnity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synchro.runnity.Models.Achievement;
import com.synchro.runnity.Models.Users;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class AchievementsListAdapter extends BaseAdapter {
    protected Context mContext;
    Achievement[] achievements;

    public AchievementsListAdapter(Context c, Achievement[] a) {
        this.mContext = c;
        this.achievements = a;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView content;
        TextView dueDate;
        TextView startDate;
        TextView value;
    }

    public int getCount() {
        if (achievements == null)
            return 0;
        return achievements.length;
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
            convertView = mInflater.inflate(R.layout.achievement_cell, null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.contentTextView);
            holder.startDate = (TextView) convertView.findViewById(R.id.startDateTextView);
            holder.dueDate = (TextView) convertView.findViewById(R.id.dueDateTextView);
            holder.value = (TextView) convertView.findViewById(R.id.valueTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.content.setText(achievements[position].getContent());

        int type = 0;
        String strinType = achievements[position].getAchievement_type();
        if (strinType != null) {
            if (strinType.equals("KILOMETER"))
                type = 1;
            if (strinType.equals("WEIGHT"))
                type = 2;
            if (strinType.equals("TIME"))
                type = 3;
        }

        switch (type) {
            case 1:
                if (achievements[position].getValue() != null)
                    holder.value.setText("Objectif : " + achievements[position].getValue() + " km");
                else {
                    holder.value.setText("Objectif : 0 km");
                }
                break;
            case 2:
                if (achievements[position].getValue() != null)
                    holder.value.setText("Objectif : " + achievements[position].getValue() + " kg");
                else {
                    holder.value.setText("Objectif : 0 kg");
                }
                break;
            case 3:
                if (achievements[position].getValue() != null)
                    holder.value.setText("Objectif : " + achievements[position].getValue() + " min de course");
                else {
                    holder.value.setText("Objectif : 0 min de course");
                }
                break;
            default:
                holder.value.setText("Objectif : Non défini");
                break;
        }

        String dueDateString = achievements[position].getDue_date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        if (dueDateString != null)
            try {
                DateFormat format2 = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
                Date date = format.parse(dueDateString);
                String dateString = format2.format(date);
                holder.dueDate.setText("Date de fin : " + dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        String startDateString = achievements[position].getStart_date();
        if (startDateString != null)
            try {
                DateFormat format2 = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
                Date date = format.parse(startDateString);
                String dateString = format2.format(date);
                holder.startDate.setText("Date de début : " + dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return convertView;
    }

    public void notifyDataSetChanged(Achievement[] a) {
        achievements = a;
        super.notifyDataSetChanged();
    }
}
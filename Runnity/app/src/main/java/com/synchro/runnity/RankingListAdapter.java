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
import com.synchro.runnity.Models.Users;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class RankingListAdapter extends BaseAdapter {
    protected Context mContext;
    Users friends;
    int mType;

    public RankingListAdapter(Context c, Users f, int type) {
        this.mContext = c;
        this.friends = f;
        mType = type;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name;
        TextView type;
        TextView value;
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
            convertView = mInflater.inflate(R.layout.ranking_cell, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
            holder.type = (TextView) convertView.findViewById(R.id.rankingTypeTextView);
            holder.value = (TextView) convertView.findViewById(R.id.valueTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(friends.getFriends()[position].getFirstname() + " " + friends.getFriends()[position].getLastname());


        switch (mType) {
            case 1:
                holder.type.setText("Vitesse moyenne :");
                if (friends.getFriends()[position].getAverage_speed() != null)
                    holder.value.setText(friends.getFriends()[position].getAverage_speed() + " km/h");
                else {
                    holder.value.setText("0 km/h");
                }
                break;
            case 2:
                holder.type.setText("Calories brulées :");
                if (friends.getFriends()[position].getTotal_calorie() != null)
                    holder.value.setText(friends.getFriends()[position].getTotal_calorie() + " cal");
                else {
                    holder.value.setText("0 cal");
                }
                break;
            default:
                holder.type.setText("Kilomètres parcourus :");
                if (friends.getFriends()[position].getKilometer_done() != null) {
                    holder.value.setText(friends.getFriends()[position].getKilometer_done() + " km");
                }
                else {
                    holder.value.setText("0 km");
                }
                break;
        }

        return convertView;
    }

    public void notifyDataSetChanged(Users f) {
        friends = f;
        super.notifyDataSetChanged();
    }
}
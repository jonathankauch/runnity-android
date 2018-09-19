package com.synchro.runnity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Event;
import com.synchro.runnity.Models.Events;
import com.synchro.runnity.Models.Group;
import com.synchro.runnity.Models.Groups;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class GroupsListAdapter extends BaseAdapter {
    protected Context mContext;
    Groups groups;
    Group selectedGroup;
    public GroupsListAdapter(Context c) {
        this.mContext = c;
        if (Singleton.getInstance().getEvents() == null) {
            Events tmpEvents = new Events();
            tmpEvents.setEvents(new Event[0]);
            Singleton.getInstance().setEvents(tmpEvents);

        }
        this.groups = Singleton.getInstance().getGroups();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView details;
        Button subscribeButton;
    }

    public int getCount() {
        return groups.getGroups().length;
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
            convertView = mInflater.inflate(R.layout.event_cell, null);
            holder = new ViewHolder();
            holder.details = (TextView) convertView.findViewById(R.id.eventDetailsTextView);
            holder.subscribeButton = (Button) convertView.findViewById(R.id.subscribeButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String details = groups.getGroups()[position].getName();
//        String details = groups.getGroups()[position].getName() + "\n" + groups.getGroups()[position].getDescription();

        holder.details.setText(details);

        View.OnClickListener eventClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupWallActivity.class);

                Log.d("GROUP ID", groups.getGroups()[position].getId());
                intent.putExtra("group_id", groups.getGroups()[position].getId());
                mContext.startActivity(intent);
//                getEventDetail(groups.getGroups()[position].get_id());
            }
        };

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedGroup = groups.getGroups()[position];

                if (selectedGroup.getUser_id().equals(Singleton.getInstance().getId()))
                    AskOptionOtherPost().show();
                return false;
            }
        };


        final GroupsListAdapter.ViewHolder finalHolder = holder;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button)v).getText().equals("INSCRIPTION")) {
                    setOff(((Button)v), finalHolder.details, groups.getGroups()[position]);
                } else if (((Button)v).getText().equals("ENVOYER UNE DEMANDE")) {
                    setOff(((Button)v), finalHolder.details, groups.getGroups()[position]);
                    Toast.makeText(mContext, "Votre demande a été envoyée.", Toast.LENGTH_LONG).show();
                } else if (((Button)v).getText().equals("EN ATTENTE")) {
                    Toast.makeText(mContext, "Votre demande est en attente de comfirmation.", Toast.LENGTH_LONG).show();
                } else {
                    setOn(((Button)v), finalHolder.details, groups.getGroups()[position]);
                }
            }
        };

        if (groups.getGroups()[position].getIs_register()) {
            holder.subscribeButton.setText("DÉSINSCRIPTION");
            holder.details.setEnabled(true);
            holder.subscribeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (groups.getGroups()[position].getPrivate_status()) {
            holder.subscribeButton.setText("ENVOYER UNE DEMANDE");
            holder.details.setEnabled(false);
            holder.subscribeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_grey));
        } else {
            holder.subscribeButton.setText("INSCRIPTION");
            holder.details.setEnabled(false);
            holder.subscribeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        }

        holder.subscribeButton.setOnClickListener(clickListener);
        holder.details.setOnClickListener(eventClickListener);
        holder.details.setOnLongClickListener(onLongClickListener);

       return convertView;
    }

    public void setOn(Button button, TextView details, Group group) {
        button.setText("INSCRIPTION");
        details.setEnabled(false);
        button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        String url = mContext.getString(R.string.api_url) + "groups/" + group.getId() + "/leave";
        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Log.d("URL DE MERDE", url);
        Ion.with(mContext)
                .load("POST", url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null || result == null) {
                            Log.d("API ERROR", e.toString());
                            return;
                        }
                        Log.d("API RESPONSE", result.toString());

                    }
                });
    }

    public void setOff(Button button, TextView details, Group group) {
        if (!group.getPrivate_status()) {
            button.setText("DÉSINSCRIPTION");
        } else {
            button.setText("EN ATTENTE");
        }
        details.setEnabled(false);
        button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        String url = mContext.getString(R.string.api_url) + "member_requests";

        JsonObject json = new JsonObject();
        json.addProperty("group_id", group.getId());

        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Ion.with(mContext)
                .load("POST", url)
                .setHeader(tokenPair, emailPair)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (e != null || result == null) {
                            Log.d("API ERROR", e.toString());
                            return;
                        }
                        Log.d("EVENT RESPONSE", result.toString());

                    }
                });
    }



    private AlertDialog AskOptionOtherPost()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
                //set message, title, and icon
                .setTitle("Editer ce groupe ?")

                .setPositiveButton("Editer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(mContext, NewGroupActivity.class);

                        intent.putExtra("group_id", selectedGroup.getId());
                        intent.putExtra("name", selectedGroup.getName());
                        intent.putExtra("description", selectedGroup.getDescription());
                        intent.putExtra("private", selectedGroup.getPrivate_status());

                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }

                })

                .setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })

                .create();
        return myQuittingDialogBox;
    }

    @Override
    public void notifyDataSetChanged() {
        groups = Singleton.getInstance().getGroups();
        super.notifyDataSetChanged();
    }
}
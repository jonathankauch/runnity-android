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
import com.synchro.runnity.Models.Run;
import com.synchro.runnity.Models.Events;
import com.synchro.runnity.Models.Singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by imacalxdr on 18/09/15.
 */
public class EventsListAdapter extends BaseAdapter {
    protected Context mContext;
    Events events;
    Event selectedEvent;
    public EventsListAdapter(Context c) {
        this.mContext = c;
        if (Singleton.getInstance().getEvents() == null) {
            Events tmpEvents = new Events();
            tmpEvents.setEvents(new Event[0]);
            Singleton.getInstance().setEvents(tmpEvents);

        }
        this.events = Singleton.getInstance().getEvents();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView details;
        Button subscribeButton;
    }

    public int getCount() {
        return events.getEvents().length;
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

        String string = events.getEvents()[position].getDate();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        if (string != null) {
            try {
                DateFormat format2 = new SimpleDateFormat("\nMMMM d, yyyy", Locale.ENGLISH);
                Date date = format.parse(string);
                String dateString = format2.format(date);
                String details = events.getEvents()[position].getName() + "\n" + String.format("%.3f", (events.getEvents()[position].getDistance() / 1000)) + " km"
                        + dateString;
                holder.details.setText(details);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        View.OnClickListener eventClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventWallActivity.class);

                Log.d("EVENT ID", events.getEvents()[position].get_id());
                intent.putExtra("event_id", events.getEvents()[position].get_id());
                mContext.startActivity(intent);
//                getEventDetail(events.getEvents()[position].get_id());
            }
        };

        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedEvent = events.getEvents()[position];

                if (selectedEvent.getUser_id().equals(Singleton.getInstance().getId()))
                    AskOptionOtherPost().show();
                return false;
            }
        };

        final ViewHolder finalHolder = holder;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button)v).getText().equals("INSCRIPTION")) {
                    setOff(((Button)v), finalHolder.details, events.getEvents()[position]);
                } else if (((Button)v).getText().equals("ENVOYER UNE DEMANDE")) {
                    setOff(((Button)v), finalHolder.details, events.getEvents()[position]);
                    Toast.makeText(mContext, "Votre demande a été envoyée.", Toast.LENGTH_LONG).show();
                } else if (((Button)v).getText().equals("EN ATTENTE")) {
                    Toast.makeText(mContext, "Votre demande est en attente de comfirmation.", Toast.LENGTH_LONG).show();
                } else {
                    setOn(((Button)v), finalHolder.details, events.getEvents()[position]);
                }
            }
        };

        if (events.getEvents()[position].getIs_register()) {
            holder.subscribeButton.setText("DÉSINSCRIPTION");
            holder.details.setEnabled(true);
            holder.subscribeButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (events.getEvents()[position].getPrivate_status()) {
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


    private AlertDialog AskOptionOtherPost()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
                //set message, title, and icon
                .setTitle("Editer cet événement ?")

                .setPositiveButton("Editer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(mContext, NewEventActivity.class);

                        intent.putExtra("event_id", selectedEvent.getId());
                        intent.putExtra("name", selectedEvent.getName());
                        intent.putExtra("city", selectedEvent.getCity());
                        intent.putExtra("description", selectedEvent.getDescription());
                        intent.putExtra("distance", selectedEvent.getDistance());
                        intent.putExtra("private", selectedEvent.getPrivate_status());

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

    public void setOn(Button button, TextView details, Event event) {
        button.setText("INSCRIPTION");
        details.setEnabled(false);
        button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        String url = mContext.getString(R.string.api_url) + "events/" + event.get_id() + "/dislike";
        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Ion.with(mContext)
                .load(url)
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

    public void setOff(Button button, TextView details, Event event) {
        if (!event.getPrivate_status()) {
            button.setText("DÉSINSCRIPTION");
            details.setEnabled(true);
            button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            String url = mContext.getString(R.string.api_url) + "events/" + event.get_id() + "/like";
            BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
            BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
            Ion.with(mContext)
                    .load(url)
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
        } else {
            button.setText("EN ATTENTE");
            details.setEnabled(false);
            button.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            String url = mContext.getString(R.string.api_url) + "member_requests";

            JsonObject json = new JsonObject();
            json.addProperty("event_id", event.get_id());

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
    }

    @Override
    public void notifyDataSetChanged() {
        events = Singleton.getInstance().getEvents();
        super.notifyDataSetChanged();
    }
}
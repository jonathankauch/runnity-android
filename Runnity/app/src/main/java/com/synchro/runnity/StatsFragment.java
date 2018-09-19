package com.synchro.runnity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Runs;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.Stats;

/**
 * Created by Shirco on 29/02/2016.
 */
public class StatsFragment extends Fragment {
    private Stats stats;

    private TextView totalDistanceValueTextView;
    private TextView footStepsValueTextView;
    private TextView totalTimeValueTextView;
    private TextView caloriesValueTextView;
    private TextView maxSpeedValueTextView;
    private TextView averageSpeedValueTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        totalDistanceValueTextView = (TextView) rootView.findViewById(R.id.totalDistanceValue);
        totalTimeValueTextView = (TextView) rootView.findViewById(R.id.timeValue);
        caloriesValueTextView = (TextView) rootView.findViewById(R.id.caloriesValue);
        averageSpeedValueTextView = (TextView) rootView.findViewById(R.id.averageSpeedValue);
        footStepsValueTextView = (TextView) rootView.findViewById(R.id.footStepsCounter);
        maxSpeedValueTextView = (TextView) rootView.findViewById(R.id.maxSpeedValue);
        getStats();

        return rootView;
    }
    public void getStats() {

        stats = new Stats();

        String url = getString(R.string.api_url) + "stats";

        if (!isNetworkAvailable()) {
            return;
        }



        BasicNameValuePair tokenPair = new BasicNameValuePair("X-User-Token", Singleton.getInstance().getToken());
        BasicNameValuePair emailPair = new BasicNameValuePair("X-User-Email", Singleton.getInstance().getmUser().getEmail());
        Ion.with(getContext())
                .load(url)
                .setHeader(tokenPair, emailPair)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            Log.e("E_RESPONSE", e.getMessage());
                            return;
                        }
                        Log.d("RUNS_RESPONSE", result.toString());
                        try {
                            stats = new Gson().fromJson(result.toString(), Stats.class);

                            if (stats == null)
                                return;

                            setValues();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    private void setValues() {
        totalDistanceValueTextView.setText(stats.getKms() + "km");
        totalTimeValueTextView.setText(stats.getTotal_time_run() + "min");
        caloriesValueTextView.setText(stats.getCalories() + "cal");
        averageSpeedValueTextView.setText(stats.getAverage_speed() + "km/h");
        footStepsValueTextView.setText(stats.getNb_steps() + "");
        maxSpeedValueTextView.setText(stats.getMax_speed() + "km/h");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

package com.synchro.runnity;

import android.content.ContentValues;
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
import com.koushikdutta.async.http.NameValuePair;
import com.synchro.runnity.Models.Coordinate;
import com.synchro.runnity.Models.Run;
import com.synchro.runnity.Models.Runs;
import com.synchro.runnity.Models.Singleton;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shirco on 29/02/2016.
 */
public class RunsFragment extends Fragment {
    private ListView listView;
    private Runs allRuns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_runs, container, false);

        listView = (ListView) rootView.findViewById(R.id.runsListView);

        listView.setAdapter(new RunsListAdapter(getContext()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), OldRunMapActivity.class);

                intent.putExtra("runIndex", position);
                startActivity(intent);
            }
        });
        getRuns();

        return rootView;
    }

    public void getRuns() {

        allRuns = new Runs();

        String url = getString(R.string.api_url) + "runs";

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
                            allRuns = new Gson().fromJson(result.toString(), Runs.class);

                            if (allRuns.getRuns() == null)
                                return;

                            Singleton.getInstance().setRuns(allRuns);

                            ((RunsListAdapter)listView.getAdapter()).notifyDataSetChanged();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

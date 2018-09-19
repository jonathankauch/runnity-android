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

/**
 * Created by Shirco on 29/02/2016.
 */
public class SpotsFragment extends Fragment {
    private ListView listView;
    private Runs allRuns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spots, container, false);

        listView = (ListView) rootView.findViewById(R.id.spotsListView);
//        try {
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        listView.setAdapter(new SpotsListAdapter(getContext()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SpotMapActivity.class);

                intent.putExtra("runIndex", position);
                startActivity(intent);
            }
        });
        getRuns();

        return rootView;
    }

    public void getRuns() {

        allRuns = new Runs();

        String url = getString(R.string.api_url) + "get_spots/";

        if (!isNetworkAvailable()) {
            return;
        }

        Log.d("REQUETE", url);
        Log.d("X-User-Token", Singleton.getInstance().getToken());
        Log.d("X-User-Email", Singleton.getInstance().getmUser().getEmail());

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

//                            for (int i = 0; i < allRuns.getRuns().length; i++) {
//                                allRuns.getRuns()[i].setRealCoordinates(new Gson().fromJson(
//                                        allRuns.getRuns()[i].getCoordinates(), Coordinate[].class));
//                            }

                            Singleton.getInstance().setSpots(allRuns);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((SpotsListAdapter)listView.getAdapter()).notifyDataSetChanged();
                                }
                            });

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

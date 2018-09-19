package com.synchro.runnity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Runs;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.Stats;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shirco on 29/02/2016.
 */
public class ChartsFragment extends Fragment {

    private Stats stats;
    @BindView(R.id.distanceButton)
    Button distanceButton;

    @BindView(R.id.timeButton)
    Button timeButton;

    @BindView(R.id.sessionsButton)
    Button sessionsButton;

    @BindView(R.id.distanceChartLayout)
    LinearLayout distanceChartLayout;

    @BindView(R.id.timeChartLayout)
    LinearLayout timeChartLayout;

    @BindView(R.id.sessionsChartLayout)
    LinearLayout sessionsChartLayout;

    @BindView(R.id.caloriesButton)
    Button caloriesButton;

    @BindView(R.id.caloriessChartLayout)
    LinearLayout caloriesChartLayout;

    LineChart distanceChart;
    LineChart timeChart;
    LineChart sessionsChart;
    LineChart caloriesChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts, container, false);
        ButterKnife.bind(this, rootView);

        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distanceChartLayout.getVisibility() == View.GONE) {
                    distanceButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    distanceButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    sessionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    sessionsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    timeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    timeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    sessionsChartLayout.setVisibility(View.GONE);
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    caloriesChartLayout.setVisibility(View.GONE);
                    timeChartLayout.setVisibility(View.GONE);
                    distanceChartLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeChartLayout.getVisibility() == View.GONE) {
                    timeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    timeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    sessionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    sessionsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    distanceButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    distanceButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    sessionsChartLayout.setVisibility(View.GONE);
                    distanceChartLayout.setVisibility(View.GONE);
                    timeChartLayout.setVisibility(View.VISIBLE);
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    caloriesChartLayout.setVisibility(View.GONE);
                }
            }
        });

        sessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionsChartLayout.getVisibility() == View.GONE) {
                    sessionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    sessionsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    timeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    timeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    distanceButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    distanceButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    timeChartLayout.setVisibility(View.GONE);
                    distanceChartLayout.setVisibility(View.GONE);
                    sessionsChartLayout.setVisibility(View.VISIBLE);
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    caloriesChartLayout.setVisibility(View.GONE);
                }
            }
        });

        caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caloriesChartLayout.getVisibility() == View.GONE) {
                    caloriesButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_active));
                    caloriesButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    timeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    timeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    distanceButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    distanceButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    timeChartLayout.setVisibility(View.GONE);
                    distanceChartLayout.setVisibility(View.GONE);
                    caloriesChartLayout.setVisibility(View.VISIBLE);
                    sessionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button));
                    sessionsButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    sessionsChartLayout.setVisibility(View.GONE);
                }
            }
        });

        List<BubbleEntry> bubbleEntries = new ArrayList<BubbleEntry>();
        List<RadarEntry> radarEntries = new ArrayList<RadarEntry>();

        bubbleEntries.add(new BubbleEntry(1,1,1));
        bubbleEntries.add(new BubbleEntry(2,2,2));
        bubbleEntries.add(new BubbleEntry(3,3,3));
        bubbleEntries.add(new BubbleEntry(4,4,4));
        bubbleEntries.add(new BubbleEntry(5,5,5));

        radarEntries.add(new RadarEntry(1,1));
        radarEntries.add(new RadarEntry(2,2));
        radarEntries.add(new RadarEntry(3,3));
        radarEntries.add(new RadarEntry(4,4));
        radarEntries.add(new RadarEntry(5,5));


        Description description = new Description();
        Description description2 = new Description();
        Description description3 = new Description();
        Description description4 = new Description();

        description.setText("Distance en kilometre par mois");
        description2.setText("Temps en minute par mois");
        description3.setText("Nombre de sessions par mois");
        description4.setText("Nombre de calories brulées par mois");

        distanceChart = (LineChart) rootView.findViewById(R.id.distanceChart);
        distanceChart.setDescription(description);
        timeChart = (LineChart) rootView.findViewById(R.id.timeChart);
        timeChart.setDescription(description2);
        sessionsChart = (LineChart) rootView.findViewById(R.id.sessionsChart);
        sessionsChart.setDescription(description3);
        caloriesChart = (LineChart) rootView.findViewById(R.id.caloriesChart);
        caloriesChart.setDescription(description4);

        BubbleDataSet bubbleDataSet = new BubbleDataSet(bubbleEntries, getString(R.string.average_speed)); // add entries to dataset
        bubbleDataSet.setColor(R.color.green);
        bubbleDataSet.setValueTextColor(R.color.blue);

        BubbleData bubbleData = new BubbleData(bubbleDataSet);

        RadarDataSet radarDataSet = new RadarDataSet(radarEntries, "Label"); // add entries to dataset
        radarDataSet.setColor(R.color.green);
        radarDataSet.setValueTextColor(R.color.blue);

        RadarData radarData = new RadarData(radarDataSet);

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

                            setCharts();
                        } catch (JsonParseException err) {
                            err.printStackTrace();
                        }

                    }
                });

    }

    private void setCharts() {
        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> entries2 = new ArrayList<Entry>();
        List<Entry> barEntries = new ArrayList<Entry>();
        List<Entry> entries4 = new ArrayList<Entry>();

        for (int i = 0; i < stats.getDistance_by_month().length; i++) {
            entries.add(new Entry(i+1,stats.getDistance_by_month()[i]));
        }

        for (int i = 0; i < stats.getTime_run_by_month().length; i++) {
            entries2.add(new Entry(i+1,stats.getTime_run_by_month()[i]));
        }

        for (int i = 0; i < stats.getRuns_by_month().length; i++) {
            barEntries.add(new Entry(i+1,stats.getRuns_by_month()[i]));
        }

        for (int i = 0; i < stats.getCalories_by_month().length; i++) {
            entries4.add(new Entry(i+1,stats.getCalories_by_month()[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.runs)); // add entries to dataset
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));

        LineData lineData = new LineData(dataSet);

        LineDataSet dataSet2 = new LineDataSet(entries2, getString(R.string.average_time)); // add entries to dataset
        dataSet2.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet2.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));

        LineData lineData2 = new LineData(dataSet2);

        LineDataSet barDataSet = new LineDataSet(barEntries, getString(R.string.average_distance)); // add entries to dataset
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        barDataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));

        LineData barData = new LineData(barDataSet);


        LineDataSet dataSet4 = new LineDataSet(entries4, "Calories"); // add entries to dataset
        dataSet4.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        dataSet4.setValueTextColor(ContextCompat.getColor(getContext(), R.color.black));

        LineData lineData4 = new LineData(dataSet4);

        distanceChart.setData(lineData);
        distanceChart.invalidate();
        timeChart.setData(lineData2);
        timeChart.getPaint(Chart.PAINT_DESCRIPTION).setColor(ContextCompat.getColor(getContext(), R.color.black));
        timeChart.invalidate();
        sessionsChart.setData(barData);
        sessionsChart.getPaint(Chart.PAINT_DESCRIPTION).setColor(ContextCompat.getColor(getContext(), R.color.black));
        sessionsChart.invalidate();
        caloriesChart.setData(lineData4);
        caloriesChart.getPaint(Chart.PAINT_DESCRIPTION).setColor(ContextCompat.getColor(getContext(), R.color.black));
        caloriesChart.invalidate();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

package com.synchro.runnity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Singleton;
import com.synchro.runnity.Models.User;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    @BindView(R.id.startedButton) Button startButton;
    @BindView(R.id.pauseButton) Button pauseButton;
    @BindView(R.id.distanceValue) TextView distanceValueTextView;
//    private TextView isRunningTextView;
    @BindView(R.id.speedValue) TextView speedValueTextView;
    @BindView(R.id.timeValue) TextView timeValueTextView;
//    private TextView lastDistanceValueTextView;
    @BindView(R.id.actualSpeedValue) TextView lastSpeedValueTextView;
//    private TextView lastTimeValueTextView;
    private Boolean firstLocation = false;
    public static SupportMapFragment mMapFragment;
    private Boolean mRequestingLocationUpdates;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    @BindView(R.id.startAndStopLayout) LinearLayout startAndStopLayout;
    public MapsFragment() {

    }
    private double lastLong;
    private double lastLat;
    private long distance;
    private long time;
    private long startedTime;
    private int count;
    private long[] lastFiveTimes;
    private long[] lastFiveDistances;
    private long totalTime;
    private long totalDistance;
    private double bestSpeed;
    private double speed;
    private boolean isRunning;
    private boolean isStarted;
    private boolean isReady;
    private boolean fast;
    private int iterator;
    private ArrayList<Location> listLocsToDraw;
    private ArrayList<Double> listSpeed;
    private Location firstLoc;
    private Location lastLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.maps_fragment_test2, container, false);
        mRequestingLocationUpdates = false;
        ButterKnife.bind(this, rootView);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);

        mMapFragment = new SupportMapFragment();
        setMapAsync();

        getChildFragmentManager().beginTransaction().add(R.id.mapLayout, mMapFragment).commit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mMapFragment.getMapAsync(this);

        totalTime = 0;
        totalDistance = 0;
        bestSpeed = 0;
        startedTime = 0;
        isStarted = false;
        isReady = false;
        speed = 0;
        iterator = 0;
        fast = false;
        count = 0;
        listLocsToDraw = new ArrayList<Location>();
        listSpeed = new ArrayList<Double>();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReady) {
                    if (!isStarted) {
                        AnimatorSet set1 = new AnimatorSet();
                        set1.playTogether(
                                ObjectAnimator.ofFloat(startButton, "width", startButton.getWidth(), 0)
                        );
                        startButton.setText(R.string.stop);

                        startButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
                        set1.setDuration(500).start();
//                        startButton.setVisibility(View.INVISIBLE);
                        isStarted = true;
                    } else {
                        startButton.setText(R.string.sending);
                        startButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey));
                        isStarted = false;
                        final JsonObject json = new JsonObject();
                        JsonArray coordinates = new JsonArray();
//                        json.addProperty("src_latitude", firstLoc.getLatitude());
//                        json.addProperty("src_longitude", firstLoc.getLongitude());
//                        json.addProperty("dest_longitude", lastLoc.getLongitude());
//                        json.addProperty("dest_latitude", lastLoc.getLatitude());
//                        json.addProperty("name", "test");
//                        json.addProperty("dest_name", "test");
                        json.addProperty("started_at", new Date(firstLoc.getTime()).toString());
//                        json.addProperty("user_id", Singleton.getInstance().getId());
                        json.addProperty("user_id", Singleton.getInstance().getmUser().getId());
                        json.addProperty("user_token", Singleton.getInstance().getToken());
                        json.addProperty("user_email", Singleton.getInstance().getmUser().getEmail());
                        json.addProperty("max_speed", bestSpeed);
                        json.addProperty("is_spot", false);
                        json.addProperty("total_distance", totalDistance);
                        json.addProperty("total_time", Math.round((totalTime / 60) + 1));

                        for (int i = 0; i < listLocsToDraw.size(); i++) {
                            JsonObject tmp = new JsonObject();
                            tmp.addProperty("longitude", listLocsToDraw.get(i).getLongitude());
                            tmp.addProperty("latitude", listLocsToDraw.get(i).getLatitude());
                            tmp.addProperty("timestamp", listLocsToDraw.get(i).getTime());
                            coordinates.add(tmp);
                        }
                        json.addProperty("coordinates", coordinates.toString());

                        Log.d("MYJSON", json.toString());

                        String url = getString(R.string.api_url) + "runs";

//                        if (!isNetworkAvailable())
//                            return;
                        Ion.with(getContext())
                                .load(url)
                                .setJsonObjectBody(json)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        startButton.setText(R.string.start);
                                        startButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
                                        if (e != null || result == null) {
                                            Log.d("API RESPONSE", e.toString());
                                            return;
                                        }
                                        Log.d("API RESPONSE", result.toString());
//                                        if (result.get("code").getAsInt() == 200) {
                                            Toast.makeText(getContext(), "La course a bien été envoyée", Toast.LENGTH_SHORT).show();
                                            totalTime = 0;
                                            totalDistance = 0;
                                            bestSpeed = 0;
                                            startedTime = 0;
                                            isStarted = false;
                                            isReady = false;
                                            speed = 0;
                                            iterator = 0;
                                            fast = false;
                                            count = 0;
                                            listLocsToDraw = new ArrayList<Location>();
                                            listSpeed = new ArrayList<Double>();
//                                        } else {

//                                        }

                                    }
                                });

                    }
                }
            }
        });
        return rootView;
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
////        super.onLauch(savedInstanceState, R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(MapsActivity.this, StoreDetailActivity.class);
//                startActivity(intent);
            }
        });

        // Add a marker in Sydney and move the camera

        LatLng myLoc;
        isRunning = false;

        mMap.setMyLocationEnabled(true);

        myLoc = new LatLng(48.8534100, 2.3488000);

        lastFiveDistances = new long[] {0, 0, 0, 0, 0};
        lastFiveTimes = new long[] {0, 0, 0, 0, 0};
        count = 0;
        final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_green);
        final BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_gray);
        BitmapDescriptor icon3 = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_red);
        BitmapDescriptor icon4 = BitmapDescriptorFactory.fromResource(R.mipmap.map_marker_orange);

        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                count++;
                if (!firstLocation) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    firstLocation = true;
                }
                distance = calculateDistance(lastLat, lastLong, location.getLatitude(), location.getLongitude());
                lastLat = location.getLatitude();
                lastLong = location.getLongitude();
                Log.d("LOCATION CHANGED", "distance" + distance);
                Log.d("LOCATION CHANGED", "last time: " + time);
                Log.d("LOCATION CHANGED", "new time : " + location.getTime());
                long diff = location.getTime() - time;
                Log.d("LOCATION CHANGED", "time diff : " + diff);
                time = location.getTime();
                if (count < 4) {
                    return;
                }
                if (!isReady && !isStarted) {
                    startButton.setText(R.string.start);
                    if(getContext() == null)
                        return;
                    startButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
                }
                isReady = true;
                if (!isStarted) {
                    return;
                }

                if (firstLoc == null) {
                    firstLoc = location;
                }

                lastLoc = location;
                listLocsToDraw.add(location);
                if (startedTime == 0) {
                    startedTime = location.getTime();
                }

                String tmp = distance + " m";
//                lastDistanceValueTextView.setText(tmp);
                tmp = diff / 1000 + " s";
//                lastTimeValueTextView.setText(tmp);

                setTotalTime(location);
                if (distance < diff / 71L) {
                    lastFiveDistances[count % 3] = distance;
                    lastFiveTimes[count % 3] = diff;
                    if (count >= 1) {
                        setAndCheckSpeed(location);
                    }
                    listSpeed.add(speed);
                    if (isRunning) {
                        totalDistance += distance;
                        tmp = totalDistance + " m";
                        distanceValueTextView.setText(tmp);
                        setMultiColorPolyline();
//                        drawPrimaryLinePath();
//                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lastLat, lastLong)).title("Point n°" + count).icon(icon2);
//                        mMap.addMarker(markerOptions);
                    }
                } else {
                    listSpeed.add(speed);
                }
                Log.d("LOCATION CHANGED", "count" + count);
                Log.d("LOCATION CHANGED", "" + (distance < 10L));
            }
        };
        mGoogleApiClient.connect();

/*        MarkerOptions markerOptions = new MarkerOptions().position(myLoc).title("Marker in Paris");
        mMap.addMarker(markerOptions);
        markerOptions = new MarkerOptions().position(new LatLng(48.845105, 2.255828)).title("Carrefour Paris Auteuil").snippet("Dernière visite : 15/12/15 \u2795").icon(icon);
        mMap.addMarker(markerOptions);
        markerOptions = new MarkerOptions().position(new LatLng(48.876501, 2.326210)).title("Carrefour Market Paris").snippet("Dernière visite : Inconnue \u2795").icon(icon2);
        mMap.addMarker(markerOptions);
        markerOptions = new MarkerOptions().position(new LatLng(48.780511, 2.280950)).title("Carrefour Sceaux").snippet("Dernière visite : 11/10/15 \u2795").icon(icon3);
        mMap.addMarker(markerOptions);
        markerOptions = new MarkerOptions().position(new LatLng(48.899349, 2.245872)).title("Carrefour Courbevoie").snippet("Dernière visite : 01/11/15 \u2795").icon(icon4);
        mMap.addMarker(markerOptions);*/
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void setAndCheckSpeed(Location location) {
        double totalDistance = 0;
        double totalTime = 0;
        for (int i = 0; i < 3; i++) {
            totalDistance += lastFiveDistances[i];
            totalTime += lastFiveTimes[i];
        }

        speed = (totalDistance / (totalTime / 1000F)) * 3.6;
        String tmp = String.format("%.1f", speed) + " km/h";
        lastSpeedValueTextView.setText(tmp);

        if (speed > bestSpeed) {
            bestSpeed = speed;
            tmp = String.format("%.1f", bestSpeed) + " km/h";
            speedValueTextView.setText(tmp);
        }


        if (speed > 1) {
            if (!isRunning) {
//                isRunningTextView.setText(R.string.yes);
//                isRunningTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            }
            isRunning = true;
        } else {
            if (isRunning) {
//                isRunningTextView.setText(R.string.no);
//                isRunningTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }
            isRunning = false;
        }
    }

    private void setTotalTime(Location location) {
        String tmp = (location.getTime() - startedTime) / 1000 + " s";
        totalTime = (location.getTime() - startedTime) / 1000;
        timeValueTextView.setText(tmp);
    }

    @Override
    public void onPause() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        super.onResume();
    }

    private static long calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        long distanceInMeters = Math.round(6371000 * c);
        return distanceInMeters;
    }

    private void drawPrimaryLinePath()
    {
        if ( mMap == null )
        {
            return;
        }

        if ( listLocsToDraw.size() < 2 )
        {
            return;
        }

        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#CC0091EA" ) );
        options.width( 7 );
        options.visible( true );

        for ( Location locRecorded : listLocsToDraw )
        {
            options.add( new LatLng( locRecorded.getLatitude(),
                    locRecorded.getLongitude() ) );
        }

        mMap.addPolyline( options );

    }

    private void setMultiColorPolyline() {
        int size = listLocsToDraw.size();
        int size2 = listSpeed.size();
        if (size < size2) {
            return;
        }
        PolylineOptions optline = new PolylineOptions();
        PolylineOptions optline2 = new PolylineOptions();
        optline.geodesic(true);
        if (fast) {
            optline.color( Color.parseColor( "#CCBB2222" ) );
        } else {
            optline.color(Color.parseColor("#CC0091EA"));
        }
        optline.width(10);
        optline2.geodesic(true);
        optline2.width(10);
        for (int i = iterator; i < size - 1; i++) {

            Location pointD = listLocsToDraw.get(i);
            Location pointA = listLocsToDraw.get(i + 1);
            if (listSpeed.get(i) >= 10 && !fast) {
                fast = true;
                optline = new PolylineOptions();
                optline.geodesic(true);
                optline.width(10);
                optline.color( Color.parseColor( "#CCBB2222" ) );
            } else if (listSpeed.get(i) < 10 && fast) {
                fast = false;
                optline = new PolylineOptions();
                optline.geodesic(true);
                optline.width(10);
                optline.color( Color.parseColor( "#CC0091EA" ) );
            }
            int green = (int) ((float) 255 - (float) (i / (float) size) * (float) 255);
            int red = (int) ((float) 0 + (float) (i / (float) size) * (float) 255);

            optline.add(new LatLng(pointD.getLatitude(), pointD.getLongitude()), new LatLng(pointA.getLatitude(), pointA.getLongitude()));
            mMap.addPolyline(optline);
            iterator = i;

        }
        if (iterator > 0)
            iterator--;
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("LOCATION UPDATE", "Loc changed gonna start");
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        Log.d("LOCATION UPDATE", "Loc changed started");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                mMap, mMap.getMyLocation(), this);
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub

/*        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, // your activity
                        RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent. Return to the
                // default
                // state and attempt to connect to get an updated
                // ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }*/

    }

    private void setMapAsync() {
        mMapFragment.getMapAsync(this);
    }
}

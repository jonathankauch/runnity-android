package com.synchro.runnity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.ion.Ion;
import com.synchro.runnity.Models.Run;
import com.synchro.runnity.Models.Singleton;

/**
 * The Class AtmActivity.
 */
public class SpotMapActivity extends AppCompatActivity implements ConnectionCallbacks, OnMapReadyCallback,
        OnConnectionFailedListener {
	/** The map. */
	private GoogleMap map;
	private boolean clientConnected;
	private int runIndex;
	private Run run;
	/** The Constant POINTS_KEY. */
	private static final String POINTS_KEY = "POINTS_KEY";

	/** The m google api client. */
	private GoogleApiClient mGoogleApiClient;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.visa.infinite.ui.menu.VisaSlidingFragmentActivity#onCreate(android.os
	 * .Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spot_map_activity);
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		runIndex = getIntent().getIntExtra("runIndex", 0);

		run = Singleton.getInstance().getSpots().getRuns()[runIndex];


		Log.d("MY RUN", run.getRealCoordinates().length + "");
		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (Exception ex) { //NOPMD
			Log.e("maps", ex.getMessage());
		}
	}

	public Bitmap resizeMapIcons(String iconName, int width, int height){
		width = Math.round(width * this.getResources().getDisplayMetrics().density);
		height = Math.round(height * this.getResources().getDisplayMetrics().density);
		Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
		return resizedBitmap;
	}

	/**
	 * Inits the map.
	 */
	private void initMap() {
		MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment));
		mapFragment.getMapAsync(this);
	}

	private void setupMap() {
		this.map.getUiSettings().setMapToolbarEnabled(false);
		if (run.getRealCoordinates() != null && run.getRealCoordinates().length > 0) {
			this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(run.getRealCoordinates()[0].getLatitude(), run.getRealCoordinates()[0].getLongitude()), 16.0f));
			setMultiColorPolyline();
		}
	}

	private void setMultiColorPolyline() {
		PolylineOptions optline = new PolylineOptions();
		optline.geodesic(true);
		optline.color(Color.parseColor("#CC0091EA"));
		optline.width(10);
		for (int i = 0; i < run.getRealCoordinates().length - 1; i++) {

			Location pointD = new Location("");
			pointD.setLatitude(run.getRealCoordinates()[i].getLatitude());
			pointD.setLongitude(run.getRealCoordinates()[i].getLongitude());
			Log.d("MY RUN", pointD.getLatitude() + "");

			Location pointA = new Location("");
			pointA.setLatitude(run.getRealCoordinates()[i + 1].getLatitude());
			pointA.setLongitude(run.getRealCoordinates()[i + 1].getLongitude());
			Log.d("MY RUN", pointA.getLatitude() + "");

			optline = new PolylineOptions();
			optline.geodesic(true);
			optline.width(10);
			optline.color( Color.parseColor( "#CC0091EA" ) );

			optline.add(new LatLng(pointD.getLatitude(), pointD.getLongitude()), new LatLng(pointA.getLatitude(), pointA.getLongitude()));
			map.addPolyline(optline);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;
		setupMap();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.mGoogleApiClient.connect();
	}

	/* (non-Javadoc)
	 * @see fr.visa.infinite.ui.menu.VisaSlidingFragmentActivity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		initMap();
		// showPoints();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	public void onStop() {
		this.mGoogleApiClient.disconnect();
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//showPoints();
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(final Bundle arg0) {
		this.clientConnected = true;
	}


	/* (non-Javadoc)
	 * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
	 */
	@Override
	public void onConnectionSuspended(final int arg0) {
		//DO NOTHING
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
	 * #onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(final ConnectionResult connectionResult) {
		//DO NOTHING
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}


}

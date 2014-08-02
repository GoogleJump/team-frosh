package com.teamfrosh.aux;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

public class LocationUpdaterService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
com.google.android.gms.location.LocationListener {

	private GoogleMap mMap;
	private String[] places;
	private LocationManager locationManager;
	private Location loc;
	private LocationRequest mLocationRequest;
	private LocationClient mLocationClient;
	private boolean mUpdatesRequested;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	public static final String BROWSER_API_KEY = "AIzaSyChWPi9Evjiaj7RyBfwMWMoNQ5fpnSsqGg";
	public static final String ANDROID_API_KEY = "AIzaSyA0HBA4HpGbL2KkeGhIi6hbCUVjfJB1UpQ";
	public static final String LOCATION_UPDATER_SERVICE_TAG = "LOCATION_UPDATER_SERVICE_TAG";
	public static final String ON_ACTIVITY_RESULT_TAG = "ON ACTIVITY RESULT TAG";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 100;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private Handler handler;



	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(LOCATION_UPDATER_SERVICE_TAG, "service - onStartCommand");
		handler = new Handler();
		// onHandleIntent ...
	    showShortToast("Service Started");
		// Create the LocationRequest object
		mLocationRequest = LocationRequest.create();
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		// Set the fastest update interval
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		/*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();

        //Intent myIntent = new Intent(this, MainActivity.class);
        //startActivity(myIntent);

		return START_STICKY;
	}

	/*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.e(LOCATION_UPDATER_SERVICE_TAG, "service - onConnectionFailed " + connectionResult.getErrorCode());
		Toast.makeText(this, connectionResult.getErrorCode(), Toast.LENGTH_LONG)
				.show();
		showLongToast("" + connectionResult.getErrorCode());
	}

	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
	@Override
	public void onConnected(Bundle connectionHint) {
		// Display the connection status
		Log.e(LOCATION_UPDATER_SERVICE_TAG, "service - onConnected");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);

	}

	/*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
	@Override
	public void onDisconnected() {
		// Display the connection status
		showShortToast("Disconnected. Please re-connect.");
	}

	// Define the callback method that receives location updates
	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		Log.e(LOCATION_UPDATER_SERVICE_TAG, "service - onLocationChanged");
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        showShortToast(msg);
        ArrayList<String> places = new ArrayList<String>();
        places.add("movie_theater");
        places.add("atm");
        GetPlaces getPlaces = new GetPlaces(this, places, location);
        getPlaces.execute();
	}

	public void showShortToast(final String msg) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public void showLongToast(final String msg) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}


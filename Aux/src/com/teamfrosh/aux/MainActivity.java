package com.teamfrosh.aux;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends Activity {

	private boolean mUpdatesRequested;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	public static final String BROWSER_API_KEY = "AIzaSyChWPi9Evjiaj7RyBfwMWMoNQ5fpnSsqGg";
	public static final String ANDROID_API_KEY = "AIzaSyA0HBA4HpGbL2KkeGhIi6hbCUVjfJB1UpQ";
	public static final String TAG = "GOOGLE_PLACES_API_TEST";
	public static final String ON_ACTIVITY_RESULT_TAG = "ON ACTIVITY RESULT TAG";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (servicesConnected()) {
			Intent intent = new Intent(this, LocationUpdaterService.class);
			this.startService(intent);

			setContentView(R.layout.activity_main);

			// Open the shared preferences
			mPrefs = getSharedPreferences("SharedPreferences",
					Context.MODE_PRIVATE);
			// Get a SharedPreferences editor
			mEditor = mPrefs.edit();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Save the current setting for updates 
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		/*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
        if (mPrefs.contains("KEY_UPDATES_ON")) {
            mUpdatesRequested =
                    mPrefs.getBoolean("KEY_UPDATES_ON", false);

        // Otherwise, turn off location updates
        } else {
            mEditor.putBoolean("KEY_UPDATES_ON", false);
            mEditor.commit();
        }
	}

    /*
     * Handle results returned to the FragmentActivity by Google Play servies
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	Log.d(ON_ACTIVITY_RESULT_TAG, "onActivityResult");
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            	Log.d(ON_ACTIVITY_RESULT_TAG, "first case");
            	Log.d(ON_ACTIVITY_RESULT_TAG, "result code = " + resultCode);
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(ON_ACTIVITY_RESULT_TAG, "second case");
                        this.recreate();
                        break;
                }
                break;
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (resultCode == ConnectionResult.SUCCESS) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            // Continue
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
                    this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                errorDialog.show();
            }
            return false;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

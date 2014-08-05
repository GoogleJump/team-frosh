package com.teamfrosh.aux;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends Activity {

	private boolean mUpdatesRequested;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	public static final String BROWSER_API_KEY = "AIzaSyChWPi9Evjiaj7RyBfwMWMoNQ5fpnSsqGg";
	public static final String ANDROID_API_KEY = "AIzaSyA0HBA4HpGbL2KkeGhIi6hbCUVjfJB1UpQ";
	public static final String TAG = "AUX_TAG";
	public static final String ACTIVITY_TAG = "AUX_ACTIVITY_TAG";
	public static final String LOCATION_TAG = "AUX_LOCATION_TAG";
	public static final String SETTINGS_TAG = "AUX_SETTINGS_TAG";
	public static final String ON_ACTIVITY_RESULT_TAG = "ON ACTIVITY RESULT TAG";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private ActivityRecognizerReceiver receiver;
    private Switch locationSwitch;
    private Switch activitySwitch;
    private Intent locationIntent;
    private Intent activityRecognizerIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (servicesConnected()) {
			locationIntent = new Intent(this, LocationUpdaterService.class);
			activityRecognizerIntent = new Intent(this, ActivityRecognizerService.class);

			setContentView(R.layout.activity_main);

			// Open the shared preferences
			mPrefs = getSharedPreferences("SharedPreferences",
					Context.MODE_PRIVATE);
			// Get a SharedPreferences editor
			mEditor = mPrefs.edit();

			IntentFilter filter = new IntentFilter(ActivityRecognizerReceiver.ACTIVITY_RECOGNIZED);
			filter.addAction(ActivityRecognizerReceiver.ECHO_LOCATION_INFO);
			filter.addCategory(Intent.CATEGORY_DEFAULT);
			receiver = new ActivityRecognizerReceiver();
			registerReceiver(receiver, filter);
			
			locationSwitch = (Switch) findViewById(R.id.location_switch);
			locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.v(TAG, "In location listener");
					if (isChecked) {
						mEditor.putBoolean(getString(R.string.prefs_key_location_updates), true);
						mEditor.commit();
						startService(locationIntent);
					} else {
						Log.v(TAG, "Kill location");
						mEditor.putBoolean(getString(R.string.prefs_key_location_updates), false);
						mEditor.commit();
						stopService(locationIntent);
					}
				}
			});
			
			activitySwitch = (Switch) findViewById(R.id.activity_switch);
			activitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						mEditor.putBoolean(getString(R.string.prefs_key_activity_updates), true);
						mEditor.commit();
						startService(activityRecognizerIntent);
					} else {
						mEditor.putBoolean(getString(R.string.prefs_key_activity_updates), false);
						mEditor.commit();
						stopService(activityRecognizerIntent);
					}
				}
			});
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
		if (mPrefs.getBoolean(getString(R.string.prefs_key_location_updates), true)) {
			locationSwitch.setChecked(true);
			startService(locationIntent);
		} else {
			locationSwitch.setChecked(false);
			stopService(locationIntent);
		}
		
		if (mPrefs.getBoolean(getString(R.string.prefs_key_activity_updates), true)) {
			activitySwitch.setChecked(true);
			startService(activityRecognizerIntent);
		} else {
			activitySwitch.setChecked(false);
			stopService(activityRecognizerIntent);
		}
		
		
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
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add_profile:
			Toast.makeText(this, "Add Pressed", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_settings:
			Toast.makeText(this, "Settings Pressed", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/* RECEIVER CLASS */
	public class ActivityRecognizerReceiver extends BroadcastReceiver {

		public static final String ACTIVITY_RECOGNIZED = "com.teamfrosh.aux.activity_recognized";
		public static final String ECHO_LOCATION_INFO = "com.teamfrosh.aux.echo_location_info";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String activityName = intent.getStringExtra(getString(R.string.activity_name_tag));
			if (activityName != null && activityName.length() > 0) {
				TextView activityTextView = (TextView) findViewById(R.id.activity_text_view);
				activityTextView.setText(activityName);
			}
			
			String locationInfo = intent.getStringExtra(getString(R.string.echo_location_info));
			if (locationInfo != null && locationInfo.length() > 0) {
				TextView locationInfoTextView = (TextView) findViewById(R.id.echo_location_info_text_view);
				locationInfoTextView.setText(locationInfo);
			}
		}
	}


}
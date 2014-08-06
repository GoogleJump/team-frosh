package com.teamfrosh.aux;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.WindowManager.LayoutParams;

public class AddProfileActivity extends Activity implements
		OnItemSelectedListener {
	private Spinner activitySpinner;
	private Spinner placeSpinner;
	private SeekBar volumeSeekBar;
	private SeekBar brightnessSeekBar;
	private Switch wifiSwitch;
	private Switch bluetoothSwitch;
	private EditText nameTextView;
	private ContentResolver cResolver;
	public static final String TAG = "ADD_PROFILE_TAG";
	private boolean useActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_profile);
		activitySpinner = (Spinner) findViewById(R.id.activity_spinner);
		placeSpinner = (Spinner) findViewById(R.id.place_spinner);
		volumeSeekBar = (SeekBar) findViewById(R.id.volume_seekbar);
		brightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
		wifiSwitch = (Switch) findViewById(R.id.wifi_toggle);
		bluetoothSwitch = (Switch) findViewById(R.id.bluetooth_toggle);
		nameTextView = (EditText) findViewById(R.id.profile_name_edittext);
		cResolver = getContentResolver();
		useActivity = false;
		
		/* Activity spinner setup */
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.activities_array,
				android.R.layout.simple_spinner_dropdown_item);
		activitySpinner.setAdapter(adapter);
		activitySpinner.setOnItemSelectedListener(this);
		
		/* Place spinner setup */
		adapter = ArrayAdapter.createFromResource(this, R.array.places_array,
				android.R.layout.simple_spinner_dropdown_item);
		placeSpinner.setAdapter(adapter);
		placeSpinner.setOnItemSelectedListener(this);
		
		/* Brightness seekbar setup */
		brightnessSeekBar.setMax(255);
		brightnessSeekBar.setKeyProgressIncrement(1);
		int currentBrightness;
		try {
			currentBrightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
            currentBrightness = 100;
		}
		brightnessSeekBar.setProgress(currentBrightness);
		brightnessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//preview brightness changes at this window
                //get the current window attributes
                LayoutParams layoutpars = getWindow().getAttributes();
                //set the brightness of this window
                layoutpars.screenBrightness = progress / (float)255;
                //apply attribute changes to this window
                getWindow().setAttributes(layoutpars);	
			}
		});
		
		/* Volume seekbar setup */
		AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
		volumeSeekBar.setKeyProgressIncrement(1);
		volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
	}
	
	public void addProfile(View view) {
		Toast.makeText(this, "Add Profile", Toast.LENGTH_SHORT).show();
		addProfileToDatabase();
		finish();
	}
	
	public void addProfileToDatabase() {
		DBAdapter myDBAdapter = new DBAdapter(this);
		myDBAdapter.open();
		/* Name */
		String name = nameTextView.getText().toString();
		
		/* Context */
		String activityOrPlaceName;
		String activityOrPlaceKey;
		if (useActivity && activitySpinner.getSelectedItem().toString().length() > 0) {
			activityOrPlaceName = activitySpinner.getSelectedItem().toString().toLowerCase();
			activityOrPlaceKey = DBAdapter.KEY_ACTIVITY;
		} else {
			activityOrPlaceName = placeSpinner.getSelectedItem().toString().toLowerCase();
			activityOrPlaceKey = DBAdapter.KEY_PLACE;
		}
		
		/* Settings */
		String volume = Integer.toString(volumeSeekBar.getProgress());
		String brightness = Integer.toString(brightnessSeekBar.getProgress());
		String wifi;
		if (wifiSwitch.isChecked()) {
			wifi = "on";
		} else {
			wifi = "off";
		}
		String bluetooth;
		if (bluetoothSwitch.isChecked()) {
			bluetooth = "on";
		} else { 
			bluetooth = "off";
		}
		
		String[] columns = {activityOrPlaceKey, DBAdapter.KEY_NAME, DBAdapter.KEY_VOLUME, DBAdapter.KEY_BRIGHTNESS, 
				DBAdapter.KEY_WIFI, DBAdapter.KEY_BLUETOOTH};
		String[] values = {activityOrPlaceName, name, volume, brightness, wifi, bluetooth};
		myDBAdapter.insertProfile(columns, values);
		myDBAdapter.close();
	}
	
	/*** OnItemSelectedListener Methods ***/
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Log.v(TAG, "Spinner selected");
		long parentId = parent.getId();
		if (parentId == R.id.activity_spinner) {
			Log.v(TAG, "Activity spinner selected with id = " + parentId);
			placeSpinner.setAlpha(.5f);
			activitySpinner.setAlpha(1);
			useActivity = true;
		} else if (parentId == R.id.place_spinner) {
			Log.v(TAG, "Place spinner selected with id = " + parentId);
			activitySpinner.setAlpha(.5f);
			placeSpinner.setAlpha(1);
			useActivity = false;
		}
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}
}

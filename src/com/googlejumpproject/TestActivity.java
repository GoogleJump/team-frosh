package com.googlejumpproject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;
import android.provider.Settings.SettingNotFoundException;

public class TestActivity extends ActionBarActivity {
	TextView settingsText; //Textfield that displays status of settings through text
	Button settingsButton; //Button that changes settings to set values on press
	WifiManager wifiManager;
	AudioManager audioManager;
	BluetoothManager btManager;
	BluetoothAdapter btAdapter;
	int initBrightness;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		settingsText = (TextView)findViewById(R.id.settingsText);
		settingsButton = (Button)findViewById(R.id.changeSettingsButton);
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	    audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
	    initBrightness = 0;
	    try{
	    initBrightness  = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
	    }
	    catch(SettingNotFoundException e){
	    	e.printStackTrace();
	    }
		//bluetooth is not supported on Virtual Device and will throw errors
	    //btManager = (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
		//btAdapter = btManager.getAdapter();
		/*
		 * I just created my own onclick listener for the settings button
		 * because it is much easier to read rather than creating an 
		 * anonymous on click listener and including the function call in here
		 * -Taylor
		 */
		settingsButton.setOnClickListener(new SettingsButtonListener());
	    updateSettingsText(); //required in order to display the current settings on activity creation
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void updateSettingsText()
	{  
		//Requires try loop
		int screen_brightness = -1;
		try {
			screen_brightness = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		
		settingsText.setText("Brightness: " + screen_brightness + "\nSound: " + audioManager.getStreamVolume(AudioManager.STREAM_RING)
									+"\nWifi: " + wifiManager.isWifiEnabled());
	}
	
	
	//OnClickListener specifically for the changeSettingsButton 
	private class SettingsButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			
			//screen brightness management
			int curr_screen_brightness = -1;
			try {
				curr_screen_brightness = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
			 
			if(curr_screen_brightness == 255)
			 {
				curr_screen_brightness = initBrightness;
			 }
			 else{
				 curr_screen_brightness = 255;
			 }
			android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, curr_screen_brightness);
			
			//Wifi management
			 if(wifiManager.isWifiEnabled())
			 {
				 wifiManager.setWifiEnabled(false);
			 }
			 else
			 {
				 wifiManager.setWifiEnabled(true);
			 }
			 
			 //sound management (currently only Ring Volume)
			 int maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
			 int currRingVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
			 if(currRingVolume != 0) //silent
			 {
				 currRingVolume = 0;
			 }
			 else{
				 currRingVolume = maxRingVolume;
			 }
			 audioManager.setStreamVolume(AudioManager.STREAM_RING, maxRingVolume, currRingVolume);
			 
			 //Bluetooth Management
			 /*
			 if(btAdapter.isEnabled())
			 {
				 btAdapter.disable();
			 }
			 else
			 {
				 btAdapter.enable();
			 }
			*/
			 updateSettingsText();
		}
	}

}

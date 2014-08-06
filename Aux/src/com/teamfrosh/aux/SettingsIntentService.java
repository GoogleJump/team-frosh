package com.teamfrosh.aux;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.provider.Settings.System;

public class SettingsIntentService extends IntentService {

	public SettingsIntentService(String name) {
		super(name);
	}

	public SettingsIntentService() {
		super("SettingsIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(MainActivity.SETTINGS_TAG, "Settings Intent Service");
		String activityName = intent.getStringExtra(getString(R.string.activity_name_tag));
		String placeType = intent.getStringExtra(getString(R.string.place_type_tag));
		DBAdapter myDBAdapter = new DBAdapter(this);
		myDBAdapter.open();
		Cursor cursor = myDBAdapter.getProfileForActivity(activityName);
		changeSettingsForCursor(cursor);
		cursor = myDBAdapter.getProfileForPlace(placeType);
		changeSettingsForCursor(cursor);
	}

    public void toggleBluetooth(boolean status) {
    	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (status && !bluetoothAdapter.isEnabled()) {
        	bluetoothAdapter.enable();
        }
        else if(!status && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void toggleWifi(boolean status) {
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (status && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		} else if (!status && wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

    public void setVolume(int volume) {
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void setBrightness(int brightness) {
        ContentResolver cResolver = getContentResolver();
        System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
    }
    
    public void changeSettingsForCursor(Cursor cursor) {
    	if (cursor != null && cursor.getCount() > 0) {
			Log.v(MainActivity.SETTINGS_TAG, "Matched, toggling settings");
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String columnName = cursor.getColumnName(i);
				String value = cursor.getString(i);
				if (value != null && value.length() > 0) {
					if (columnName.equals(DBAdapter.KEY_VOLUME)) {
						setVolume(Integer.parseInt(value));
					} else if (columnName.equals(DBAdapter.KEY_BRIGHTNESS)) {
						setBrightness(Integer.parseInt(value));
					} else if (columnName.equals(DBAdapter.KEY_WIFI)) {
						if (value.equalsIgnoreCase("on")) {
							toggleWifi(true);
						} else {
							toggleWifi(false);
						}
					} else if (columnName.equals(DBAdapter.KEY_BLUETOOTH)) {
						if (value.equalsIgnoreCase("on")) {
							toggleBluetooth(true);
						} else {
							toggleBluetooth(false);
						}
					}
				}
			}
		} else {
			// Do nothing
			Log.v(MainActivity.SETTINGS_TAG, "No match");
		}
    }

}

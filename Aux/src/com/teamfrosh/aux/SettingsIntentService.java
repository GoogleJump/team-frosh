package com.teamfrosh.aux;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
		int[] settingsArray = intent.getIntArrayExtra(getString(R.string.settings_array_tag));
		boolean bluetooth;
		boolean wifi;
		int brightness = settingsArray[2];
		int volume = settingsArray[3];
		if (settingsArray[0] == 0) {
			bluetooth = false;
			Log.v(MainActivity.SETTINGS_TAG, "Bluetooth = false");
		} else {
			bluetooth = true;
			Log.v(MainActivity.SETTINGS_TAG, "Bluetooth = true");
		}
		
		if (settingsArray[1] == 0) {
			wifi = false;
			Log.v(MainActivity.SETTINGS_TAG, "Wifi = false");
		} else {
			wifi = true;
			Log.v(MainActivity.SETTINGS_TAG, "Wifi = true");
		}
		Log.v(MainActivity.SETTINGS_TAG, "Volume = " + volume);
		Log.v(MainActivity.SETTINGS_TAG, "Brightness = " + brightness);
		
		toggleBluetooth(bluetooth);
		toggleWifi(wifi);
		setVolume(volume);
		setBrightness(brightness);
	}

    public void toggleBluetooth(boolean status) {
    	Log.e(MainActivity.SETTINGS_TAG, "Bluetooth");
    	BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (status && !bluetoothAdapter.isEnabled()) {
        	bluetoothAdapter.enable();
        }
        else if(!status && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    public void toggleWifi(boolean status) {
    	Log.e(MainActivity.SETTINGS_TAG, "Wifi");
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (status && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		} else if (!status && wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(false);
		}
	}

    public void setVolume(int volume) {
    	Log.e(MainActivity.SETTINGS_TAG, "Volume");
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void setBrightness(int brightness) {
        ContentResolver cResolver = getContentResolver();
        System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
    }

}

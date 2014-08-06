package com.teamfrosh.aux;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ProfileSimpleCursorAdapter extends SimpleCursorAdapter {
	/* General Keys */
	public static String KEY_ROWID;
	public static String KEY_NAME;

	/* Context Keys */
	public static String KEY_ACTIVITY;
	public static String KEY_PLACE;
	public static String KEY_TIME;
	public static String KEY_DAY;
	public static String KEY_LOCATION;

	/* Settings Keys */
	public static String KEY_BLUETOOTH;
	public static String KEY_WIFI;
	public static String KEY_VOLUME;
	public static String KEY_BRIGHTNESS;
	public static String KEY_LOCATION_SERVICES;
	public static String KEY_GPS;
	public static String KEY_AIRPLANE;

	public ProfileSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		Resources res = context.getResources();

		/* General Key */
        KEY_ROWID = res.getString(R.string.db_key_row_id);
        KEY_NAME = res.getString(R.string.db_key_name);

        /* Context Keys */
        KEY_ACTIVITY = res.getString(R.string.db_key_activity);
        KEY_PLACE = res.getString(R.string.db_key_place);
        KEY_TIME = res.getString(R.string.db_key_time);
        KEY_DAY = res.getString(R.string.db_key_day);
        KEY_LOCATION = res.getString(R.string.db_key_location);

        /* Settings Keys */
        KEY_BLUETOOTH = res.getString(R.string.db_key_bluetooth);
        KEY_WIFI = res.getString(R.string.db_key_wifi);
        KEY_VOLUME = res.getString(R.string.db_key_volume);
        KEY_BRIGHTNESS = res.getString(R.string.db_key_brightness);
        KEY_LOCATION_SERVICES = res.getString(R.string.db_key_location_services);
        KEY_GPS = res.getString(R.string.db_key_gps);
        KEY_AIRPLANE = res.getString(R.string.db_key_airplane);

	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (cursor != null) {
			TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
			String profileName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
			nameTextView.setText(profileName);
			
			TextView activityTextView = (TextView) view.findViewById(R.id.activity_textview);
			String activityName = cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY));
			activityTextView.setText("Activity: " + activityName);
			
			TextView placeTextView = (TextView) view.findViewById(R.id.place_textview);
			String placeName = cursor.getString(cursor.getColumnIndex(KEY_PLACE));
			placeTextView.setText("Place: " + placeName);
			
			TextView volumeTextView = (TextView) view.findViewById(R.id.volume_textview);
			String volumeLevel = cursor.getString(cursor.getColumnIndex(KEY_VOLUME));
			volumeTextView.setText("Volume: " + volumeLevel);
			
			TextView brightnessTextView = (TextView) view.findViewById(R.id.brightness_textview);
			String brightnessLevel = cursor.getString(cursor.getColumnIndex(KEY_BRIGHTNESS));
			brightnessTextView.setText("Brightness: " + brightnessLevel);
			
			TextView wifiTextView = (TextView) view.findViewById(R.id.wifi_textview);
			String wifiStatus = cursor.getString(cursor.getColumnIndex(KEY_WIFI));
			wifiTextView.setText("Wifi: " + wifiStatus);
			
			TextView bluetoothTextView = (TextView) view.findViewById(R.id.bluetooth_textview);
			String bluetoothStatus = cursor.getString(cursor.getColumnIndex(KEY_BLUETOOTH));
			bluetoothTextView.setText("Bluetooth: " + bluetoothStatus);
		}
	}

}

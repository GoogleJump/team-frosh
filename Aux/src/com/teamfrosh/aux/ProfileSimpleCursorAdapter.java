package com.teamfrosh.aux;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ProfileSimpleCursorAdapter extends SimpleCursorAdapter {
	public static String KEY_ACTIVITY;
	public static String KEY_PLACE;

	public ProfileSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		Resources res = context.getResources();
		/* Context Keys */
        KEY_ACTIVITY = res.getString(R.string.db_key_activity);
        KEY_PLACE = res.getString(R.string.db_key_place);

	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (cursor != null) {
			TextView activityTextView = (TextView) view.findViewById(R.id.activity_text_view);
			String activityName = cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY));
			activityTextView.setText("Activity: " + activityName);
			
			TextView placeTextView = (TextView) view.findViewById(R.id.place_text_view);
			String placeName = cursor.getString(cursor.getColumnIndex(KEY_PLACE));
			placeTextView.setText("Place: " + placeName);
		}
	}

}

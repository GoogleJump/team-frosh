package com.teamfrosh.aux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.d("BOOT RECEIVER TAG", "boot receiver");
		CharSequence seq = "Boot receiver";
		Toast.makeText(context, seq, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context, LocationUpdaterService.class);
		context.startService(intent);
	}

}

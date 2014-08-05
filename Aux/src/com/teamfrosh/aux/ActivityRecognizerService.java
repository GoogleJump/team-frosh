package com.teamfrosh.aux;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognizerService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {

	// Flag that indicates if a request is underway
	private boolean mInProgress;
	public enum REQUEST_TYPE { START, STOP }
	public REQUEST_TYPE mRequestType;

	// Constants that define the activity detection interval
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int DETECTION_INTERVAL_SECONDS = 5;
    public static final int DETECTION_INTERVAL_MILLISECONDS =
            MILLISECONDS_PER_SECOND * DETECTION_INTERVAL_SECONDS;

    /*
     * Store the PendingIntent used to send activity recognition events
     * back to the app
     */
    private PendingIntent mActivityRecognitionPendingIntent;
    // Store the current activity recognition client
    private ActivityRecognitionClient mActivityRecognitionClient;

    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.v(MainActivity.ACTIVITY_TAG, "Activity Recognizer Service Started");
    	mInProgress = false;

    	/*
         * Instantiate a new activity recognition client. Since the
         * parent Activity implements the connection listener and
         * connection failure listener, the constructor uses "this"
         * to specify the values of those parameters.
         */
        mActivityRecognitionClient =
                new ActivityRecognitionClient(this, this, this);
        /*
         * Create the PendingIntent that Location Services uses
         * to send activity recognition updates back to this app.
         */
        Intent serviceIntent = new Intent(
                this, ActivityRecognitionIntentService.class);
        /*
         * Return a PendingIntent that starts the IntentService.
         */
        mActivityRecognitionPendingIntent =
                PendingIntent.getService(this, 0, serviceIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        startUpdates();


    	return START_STICKY;
    }

    @Override
    public void onDestroy() {
    	stopUpdates();
    }

    public void startUpdates() {
    	mRequestType = REQUEST_TYPE.START;
    	if (!mInProgress) {
    		// Indicate that a request is in progress
    		mInProgress = true;
    		// Request a connection to Location Services
    		mActivityRecognitionClient.connect();
    	} else {
    		// Do nothing
    	}
    }

    public void stopUpdates() {
    	mRequestType = REQUEST_TYPE.STOP;
    	if (!mInProgress) {
    		mInProgress = true;
    		mActivityRecognitionClient.connect();
    	} else {
    		// Do nothing
    	}
    }

 // Implementation of OnConnectionFailedListener.onConnectionFailed
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Turn off the request flag
        mInProgress = false;
    }


	/*
     * Called by Location Services once the location client is connected.
     *
     * Continue by requesting activity updates.
     */
    @Override
    public void onConnected(Bundle dataBundle) {
		switch (mRequestType) {
		case START:
			/*
			 * Request activity recognition updates using the preset detection
			 * interval and PendingIntent. This call is synchronous.
			 */
			mActivityRecognitionClient.requestActivityUpdates(
					DETECTION_INTERVAL_MILLISECONDS,
					mActivityRecognitionPendingIntent);
			break;
		case STOP:
			mActivityRecognitionClient.removeActivityUpdates(mActivityRecognitionPendingIntent);
		default:
			try {
				throw new Exception("Unknown request type in onConnected().");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

        /*
         * Since the preceding call is synchronous, turn off the
         * in progress flag and disconnect the client
         */
        mInProgress = false;
        mActivityRecognitionClient.disconnect();
    }


    /*
     * Called by Location Services once the activity recognition
     * client is disconnected.
     */
    @Override
    public void onDisconnected() {
        // Turn off the request flag
        mInProgress = false;
        // Delete the client
        mActivityRecognitionClient = null;
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.teamfrosh.aux;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.teamfrosh.aux.MainActivity.ActivityRecognizerReceiver;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

/**
 * Service that receives ActivityRecognition updates. It receives
 * updates in the background, even if the main Activity is not visible.
 */
public class ActivityRecognitionIntentService extends IntentService {
    public ActivityRecognitionIntentService() {
    	super("ActivityRecognitionIntentService");
    }

    public ActivityRecognitionIntentService(String name) {
		super(name);
	}

	/**
     * Called when a new activity detection update is available.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
    	// If the incoming intent contains an update
        if (ActivityRecognitionResult.hasResult(intent)) {
            // Get the update
            ActivityRecognitionResult result =
                    ActivityRecognitionResult.extractResult(intent);
            // Get the most probable activity
            DetectedActivity mostProbableActivity =
                    result.getMostProbableActivity();
            /*
             * Get the probability that this activity is the
             * the user's actual activity
             */
            int confidence = mostProbableActivity.getConfidence();
            /*
             * Get an integer describing the type of activity
             */
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);
            /*
             * At this point, you have retrieved all the information
             * for the current update. You can display this
             * information to the user in a notification, or
             * send it to an Activity or Service in a broadcast
             * Intent.
             */
            Log.v(MainActivity.ACTIVITY_TAG, "Activity = " + activityName);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(ActivityRecognizerReceiver.ACTIVITY_RECOGNIZED);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(getString(R.string.activity_name_tag), activityName);
            sendBroadcast(broadcastIntent);

            Resources res = getResources();
            Intent settingsIntent = new Intent(this, SettingsIntentService.class);
            if (activityName.equals("still")) {
            	settingsIntent.putExtra(getString(R.string.settings_array_tag), res.getIntArray(R.array.default_high_settings));
            } else {
            	settingsIntent.putExtra(getString(R.string.settings_array_tag), res.getIntArray(R.array.default_low_settings));
            }
            startService(settingsIntent);

        } else {
            /*
             * This implementation ignores intents that don't contain
             * an activity update. If you wish, you can report them as
             * errors.
             */
        }

    }

    /**
     * Map detected activity types to strings
     *@param activityType The detected activity type
     *@return A user-readable name for the type
     */
    private String getNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }
}

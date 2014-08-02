package com.teamfrosh.aux;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

	public static final String BROWSER_API_KEY = "AIzaSyChWPi9Evjiaj7RyBfwMWMoNQ5fpnSsqGg";
	private ArrayList<String> places;
	private Location loc;
	private Context context;

	public GetPlaces(Context context, ArrayList<String> places, Location location) {
		this.context = context;
		this.places = places;
		this.loc = location;
	}

	@Override
	protected void onPostExecute(ArrayList<Place> result) {
		super.onPostExecute(result);
		float[] distanceResults = new float[1];
		for (int i = 0; i < result.size(); i++) {
			Place place = result.get(i);
			Location.distanceBetween(loc.getLatitude(), loc.getLatitude(), place.getLatitude(), place.getLongitude(), distanceResults);
			Log.v(MainActivity.LOCATION_TAG, "" + place + ", ...with a distance of "
					+ distanceResults[0] + " from location");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected ArrayList<Place> doInBackground(Void... arg0) {
		PlacesService service = new PlacesService(BROWSER_API_KEY);
		ArrayList<Place> findPlaces = service.findPlaces(loc.getLatitude(), // 28.632808
				loc.getLongitude(), places); // 77.218276
		return findPlaces;
	}

}
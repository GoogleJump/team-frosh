package com.teamfrosh.aux;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileLoaderListFragment extends ListFragment {
	// This is the Adapter being used to display the list's data.
	ProfileSimpleCursorAdapter mAdapter;
	
	public static String KEY_ACTIVITY;
	public static String KEY_PLACE;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources res = getActivity().getResources();
		/* Context Keys */
        KEY_ACTIVITY = res.getString(R.string.db_key_activity);
        KEY_PLACE = res.getString(R.string.db_key_place);
		
		// Give some text to display if there is no data.
		setEmptyText(getString(R.string.empty_profile_list));
        
		DBAdapter myDBAdapter = new DBAdapter(getActivity());
		myDBAdapter.open();
		//myDBAdapter.insertProfile(new String[]{KEY_ACTIVITY, KEY_PLACE}, new String[]{"still", "theater"});
		myDBAdapter.deleteAllProfiles();
		Cursor allProfilesCursor = myDBAdapter.getAllProfiles();
		Log.v(MainActivity.LIST_TAG, "Num rows = " + allProfilesCursor.getCount());
		myDBAdapter.close();
		// Create an adapter we will use to display the loaded data
		mAdapter = new ProfileSimpleCursorAdapter(getActivity(), 
				R.layout.profile_list_item, allProfilesCursor, 
				new String[] {KEY_ACTIVITY, KEY_PLACE},
				new int[] {R.id.activity_text_view, R.id.place_text_view}, 0);
		setListAdapter(mAdapter);
		
		// Start out with a progress indicator
		setListShown(true);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		// Insert desired behavior here
		Toast.makeText(getActivity(), "List Item Pressed", Toast.LENGTH_SHORT).show();
	}
}

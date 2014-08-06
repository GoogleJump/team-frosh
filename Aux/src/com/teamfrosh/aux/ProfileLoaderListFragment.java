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
	private DBAdapter myDBAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Give some text to display if there is no data.
		setEmptyText(getString(R.string.empty_profile_list));
        
		myDBAdapter = new DBAdapter(getActivity());
		myDBAdapter.open();
		Cursor allProfilesCursor = myDBAdapter.getAllProfiles();
		myDBAdapter.close();
		// Create an adapter we will use to display the loaded data
		mAdapter = new ProfileSimpleCursorAdapter(getActivity(), 
				R.layout.profile_list_item, allProfilesCursor, 
				new String[] {DBAdapter.KEY_ACTIVITY, DBAdapter.KEY_PLACE},
				new int[] {R.id.activity_text_view, R.id.place_text_view}, 0);
		setListAdapter(mAdapter);
		
		// Start out with a progress indicator
		setListShown(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		myDBAdapter.open();
		Cursor allProfilesCursor = myDBAdapter.getAllProfiles();
		mAdapter.swapCursor(allProfilesCursor);
		myDBAdapter.close();
	}
	
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		// Insert desired behavior here
		Toast.makeText(getActivity(), "List Item Pressed", Toast.LENGTH_SHORT).show();
	}
}

package com.free.base.surfer;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.free.base.surfer.adapters.PropertyValueExpandAdapter;

public class FreebaseTopicPropertyValuesExpand extends ExpandableListActivity {

	private FreebaseApplication mFreebaseApplication;

	private static final String NAME = "NAME";
	private static final String IS_EVEN = "IS_EVEN";

	private PropertyValueExpandAdapter m_adapter;
	private ExpandableListView mExpandableListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayCustomTileBar();

		mFreebaseApplication = (FreebaseApplication) getApplication();
		mExpandableListView = getExpandableListView(); 

		m_adapter = new PropertyValueExpandAdapter(FreebaseTopicPropertyValuesExpand.this, mFreebaseApplication.getTopicPropertyContainer());
		mExpandableListView.setAdapter(m_adapter);

	}


	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_expand_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Topic Property");

	}


}
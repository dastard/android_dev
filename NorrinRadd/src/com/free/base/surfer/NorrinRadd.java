package com.free.base.surfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class NorrinRadd extends Activity {
	
	private FreebaseApplication mFreebaseApplication;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFreebaseApplication = (FreebaseApplication) getApplication();
		mFreebaseApplication.resetAttributes();
		
		displayCustomTileBar();

		/**
		 * Search
		 */
		final Button searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				NorrinRadd.this.onSearchRequested();
			}
		});

		/**
		 * Explore
		 */
		final Button exploreButton = (Button) findViewById(R.id.explorer);
		exploreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(NorrinRadd.this, FreebaseDomain.class);
				startActivityForResult(intent, 20);
			}
		});

		/**
		 * Advanced Search
		 */
		final Button advancedSearchButton = (Button) findViewById(R.id.searchAdvanced);
		advancedSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(NorrinRadd.this, FreebaseComplexSearch.class);
				startActivityForResult(intent, 10);
			}
		});

	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_menu);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Main Menu");

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mFreebaseApplication.resetAttributes();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mFreebaseApplication.resetAttributes();
	}
	
}
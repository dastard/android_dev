package com.free.base.surfer;

import java.util.ArrayList;

import android.app.ListActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NorrinRaddPaging extends ListActivity {

	private final ArrayList<String> cursors = new ArrayList<String>();

	{
		cursors.add("");
	}

	String mCursor = null;

	private Integer mPage = 0;
	private volatile boolean fetchData = false;
	private Integer mMaxPage = 999999999;

	/**
	 * the user has clicked on the Next page control whether or not we should
	 * display the previous/next page button then attempt to retrieve the data
	 * that will be shown on the requested "next" page
	 * 
	 * @param view
	 */
	public void nextPage(final View view) {

		mPage++;
		controlPreviousPage();
		controlNextPage();
		storeCursor();
		fetchData = true;
	}

	protected void controlNextPage() {

		final Button nextPage = (Button) findViewById(R.id.nextPage);
		final Button previousPage = (Button) findViewById(R.id.previousPage);

		if (mPage < mMaxPage) {
			nextPage.setVisibility(View.VISIBLE);
			nextPage.setClickable(true);
		} else {
			nextPage.setVisibility(View.INVISIBLE);
			nextPage.setClickable(false);
		}

		if (mPage == 0) {
			previousPage.setClickable(false);
			previousPage.setVisibility(View.INVISIBLE);
		}

	}

	protected void controlPreviousPage() {
		final Button previousPage = (Button) findViewById(R.id.previousPage);

		if (mPage == 0) {
			previousPage.setClickable(false);
			previousPage.setVisibility(View.INVISIBLE);
		} else {
			previousPage.setClickable(true);
			previousPage.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * the user has clicked on the Previous page button control whether or not
	 * we should display the next/previous page button then attempt to retrieve
	 * the data that will be shown on the requested "previous" page
	 * 
	 * @param view
	 */
	public void previousPage(final View view) {

		if (mPage > 0) {
			mPage--;
			controlNextPage();
			controlPreviousPage();
			fetchData = true;
		}

	}

	protected String getCursor() {
		return cursors.get(mPage);
	}

	protected void storeCursor() {
		this.cursors.add(mCursor);
	}

	protected void setCursor(final String cursor) {
		this.mCursor = cursor;
	}

	protected Integer getmPage() {
		return mPage;
	}

	protected void setmPage(final Integer mPage) {
		this.mPage = mPage;
	}

	protected Integer getmMaxPage() {
		return mMaxPage;
	}

	protected void setmMaxPage(final Integer mMaxPage) {
		this.mMaxPage = mMaxPage;
	}

	protected boolean isFetchData() {
		return fetchData;
	}

	protected void setFetchData(final boolean fetchData) {
		this.fetchData = fetchData;
	}

}

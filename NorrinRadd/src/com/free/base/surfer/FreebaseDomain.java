package com.free.base.surfer;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.free.base.surfer.adapters.DomainAdapter;
import com.free.base.surfer.enums.FreeBaseMQLRead;
import com.free.base.surfer.gson.GsonFreebaseDomain;
import com.free.base.surfer.gson.GsonFreebaseDomains;
import com.free.base.surfer.utility.NorrinRaddUtility;
import com.free.base.surfer.value.objects.WebServiceRequestVO;
import com.free.base.surfer.value.objects.WebServiceResponseVO;
import com.google.gson.Gson;

public class FreebaseDomain extends NorrinRaddPaging {

	private AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseDomain>> mBackgroundTask;
	private DomainAdapter m_adapter;
	private static final String QUERY = "query";
	private static final String CURSOR = "cursor";
	private static final String CURSOR_INITIAL = "";
	private static final String KEY = "key";
	private static final String API_KEY = "AIzaSyA3OMaYZH1JkdwPlHvG0xVcxFmS47AEcHc";
	private static final String QUERY_FREEBASE = "[{ \"id\": null, \"name\": null, \"type\": \"/type/domain\", \"!/freebase/domain_category/domains\": { \"id\": \"/category/commons\" }, \"sort\": \"name\" , \"limit\": 25 }]";
	private String mPageLimit = "25";
	
	private String mCursor = CURSOR_INITIAL;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursor = CURSOR_INITIAL;

		displayCustomTileBar();

		final Button nextPage = (Button) findViewById(R.id.nextPage);
		final Button previousPage = (Button) findViewById(R.id.previousPage);

		nextPage.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {
				FreebaseDomain.super.nextPage(view);
				fetchData();
			}
		});

		previousPage.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View view) {
				FreebaseDomain.super.previousPage(view);
				mCursor = getCursor();
				fetchData();
			}
		});

		mBackgroundTask = new SelectDataTask().execute(buildWebServiceRequestVO());

	}

	private void fetchData() {
		if (isFetchData()) {
			mBackgroundTask = new SelectDataTask().execute(buildWebServiceRequestVO());
			setFetchData(false);
		}
	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Domains");

	}

	private WebServiceRequestVO buildWebServiceRequestVO() {

		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(QUERY, QUERY_FREEBASE));
		nameValuePairs.add(new BasicNameValuePair(CURSOR, mCursor));
		nameValuePairs.add(new BasicNameValuePair(KEY, API_KEY));

		return new WebServiceRequestVO(FreeBaseMQLRead.FREE_BASE_MQL_READ, nameValuePairs);
	}

	@Override
	protected void onListItemClick(final ListView list, final View view, final int position, final long id) {
		super.onListItemClick(list, view, position, id);
		final Intent intent = new Intent(this, FreebaseTypes.class);
		final GsonFreebaseDomain selectedFreeBaseDomain = ((GsonFreebaseDomain) getListAdapter().getItem(position));
		intent.putExtra("FREE BASE DOMAIN", selectedFreeBaseDomain.getId());
		startActivityForResult(intent, 101);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBackgroundTask != null) {
			mBackgroundTask.cancel(true);
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mBackgroundTask != null) {
			mBackgroundTask.cancel(true);
		}
	}

	/**
	 * This inner class is used to retrieve the activities data in a background
	 * thread
	 * 
	 * while the work is being done we show a dialog box to let the user know we
	 * are still working OK
	 * 
	 * @author Muttley
	 * 
	 */
	private class SelectDataTask extends AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseDomain>> {

		private final ProgressDialog dialog = new ProgressDialog(FreebaseDomain.this);

		@Override
		protected void onProgressUpdate(final Void... values) {
			super.onProgressUpdate(values);
		}

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setMessage("Retrieving Freebase Domains...");
			this.dialog.show();
		}

		@Override
		protected TreeSet<GsonFreebaseDomain> doInBackground(final WebServiceRequestVO... webServiceRequestVOs) {

			final TreeSet<GsonFreebaseDomain> emptyResult = new TreeSet<GsonFreebaseDomain>();

			if (isCancelled()) {
				return emptyResult;
			}

			final WebServiceResponseVO webServiceResponseVO = webServiceRequestVOs[0].getWebService().execute(webServiceRequestVOs[0].getNameValuePairs());
			String freeBaseData = null;

			switch (webServiceResponseVO.getHttpStatusCode()) {
			case HttpStatus.SC_OK:
				freeBaseData = webServiceResponseVO.getWebServiceResponseData();
				break;
			case HttpStatus.SC_SERVICE_UNAVAILABLE:
				FreebaseDomain.this.runOnUiThread(NorrinRaddUtility.manageAlertDialog(FreebaseDomain.this, "Internet Connection Error", "Please check you Internet connection."));
				return emptyResult;

			default:
				return emptyResult;
			}

			final Gson geeSon = new Gson();

			final GsonFreebaseDomains domains = geeSon.fromJson(freeBaseData, GsonFreebaseDomains.class);
			final TreeSet<GsonFreebaseDomain> domainList = domains.getResult();
			mCursor = domains.getCursor();
			setCursor(mCursor);			

			if (isCancelled()) {
				return emptyResult;
			}

			return domainList;
		}

		// can use UI thread here
		protected void onPostExecute(final TreeSet<GsonFreebaseDomain> result) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			
			/**
			 * if the returned result is empty we have tried to page past the
			 * available data we now know the maximum number of pages available,
			 * so set the value
			 */
			if (result.isEmpty()) {
				setmMaxPage(getmPage() - 1);
			} else if (result.size() < Integer.parseInt(mPageLimit)) {
				setmMaxPage(getmPage());
			}

			controlNextPage();


			m_adapter = new DomainAdapter(FreebaseDomain.this, R.layout.domain_list, new ArrayList<GsonFreebaseDomain>(result));
			setListAdapter(m_adapter);

		}

	}

}
package com.free.base.surfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.free.base.surfer.adapters.SimpleSearchExpandAdapter;
import com.free.base.surfer.enums.FreeBaseSearch;
import com.free.base.surfer.utility.NorrinRaddUtility;
import com.free.base.surfer.value.objects.FreebaseSearchRequestVO;
import com.free.base.surfer.value.objects.FreebaseSearchResultVO;
import com.free.base.surfer.value.objects.FreebaseSearchVO;
import com.free.base.surfer.value.objects.WebServiceRequestVO;
import com.free.base.surfer.value.objects.WebServiceResponseVO;

public class FreebaseSimpleSearchExpand extends ExpandableListActivity implements ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {

	private static final String FREEBASE_SURFER = "FREEBASE SURFER";
	private static final String KEY = "key";
	private static final String API_KEY = "AIzaSyA3OMaYZH1JkdwPlHvG0xVcxFmS47AEcHc";

	@SuppressWarnings("unused")
	private volatile AsyncTask<WebServiceRequestVO, Void, TreeSet<FreebaseSearchResultVO>> mBackgroundTask;
	private SimpleSearchExpandAdapter m_adapter;
	private static final String ONE = "1";
	private static final String QUERY = "query";
	private static final String PREFIXED = "prefixed";
	private static final String BLACKLIST = "blacklist";
	private static final String FUS = "fus";
	private static final String STEMMED = "stemmed";
	private static final String LIMIT = "limit";
	private static final String TYPE_UC = "TYPE";
	private static final String TYPE = "type";
	private static final String TYPE_STRICT = "type_strict";
	private static final String DOMAIN = "domain";
	private static final String DOMAIN_STRICT = "domain_strict";
	private static final String DOMAINS = "domains";
	private static final String ALL = "all";
	private static final String ANY = "any";
	private static final String SHOULD = "should";
	private String mSearchQuery;
	private ExpandableListView mExpandableListView;
	private FreebaseApplication mFreebaseApplication;
	private FreebaseSearchRequestVO mFreebaseSearchRequestVO;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFreebaseApplication = (FreebaseApplication) getApplication();
		mFreebaseSearchRequestVO = mFreebaseApplication.getFreebaseSearchRequestVO();

		final Intent intent = getIntent();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			mSearchQuery = intent.getStringExtra(SearchManager.QUERY);
		} else if (mFreebaseSearchRequestVO != null) {
			mSearchQuery = mFreebaseSearchRequestVO.getQueryString();
		}

		displayCustomTileBar();
		mExpandableListView = getExpandableListView();
		mBackgroundTask = new SelectDataTask().execute(buildWebServiceRequestVO());

	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_expand_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Search : " + mSearchQuery);

	}

	private WebServiceRequestVO buildWebServiceRequestVO() {

		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		if (mFreebaseSearchRequestVO == null) {
			nameValuePairs.add(new BasicNameValuePair(QUERY, mSearchQuery));
			nameValuePairs.add(new BasicNameValuePair(DOMAINS, ALL));
		} else {
			addComplexSearchParameters(nameValuePairs, mFreebaseSearchRequestVO);
		}

		nameValuePairs.add(new BasicNameValuePair(KEY, API_KEY));

		return new WebServiceRequestVO(FreeBaseSearch.FREE_BASE_SEARCH, nameValuePairs);
	}

	private void addComplexSearchParameters(final ArrayList<NameValuePair> nameValuePairs, final FreebaseSearchRequestVO freebaseSearchRequestVO) {

		nameValuePairs.add(new BasicNameValuePair(QUERY, mSearchQuery));

		final Integer tempLimit = freebaseSearchRequestVO.getQueryLimitInteger();

		if (tempLimit > 0) {
			nameValuePairs.add(new BasicNameValuePair(LIMIT, tempLimit.toString()));
		}

		if (freebaseSearchRequestVO.getBlacklist()) {
			nameValuePairs.add(new BasicNameValuePair(BLACKLIST, FUS));
		}

		if (freebaseSearchRequestVO.getPrefixed()) {
			nameValuePairs.add(new BasicNameValuePair(PREFIXED, ONE));
		}

		if (freebaseSearchRequestVO.getStemmed()) {
			nameValuePairs.add(new BasicNameValuePair(STEMMED, ONE));
		}

		/*
		 * Type list processing
		 */
		final CharSequence[] freebaseTypeIdArray = freebaseSearchRequestVO.getFreebaseTypeIdArray();

		if (freebaseTypeIdArray == null) {

		} else {

			for (final CharSequence typeId : freebaseTypeIdArray) {
				nameValuePairs.add(new BasicNameValuePair(TYPE, typeId.toString()));
			}

			if (freebaseSearchRequestVO.getAllType()) {
				nameValuePairs.add(new BasicNameValuePair(TYPE_STRICT, ALL));
			}

			if (freebaseSearchRequestVO.getAnyType()) {
				nameValuePairs.add(new BasicNameValuePair(TYPE_STRICT, ANY));
			}

			if (freebaseSearchRequestVO.getShouldType()) {
				nameValuePairs.add(new BasicNameValuePair(TYPE_STRICT, SHOULD));
			}
		}

		/*
		 * Domain list processing
		 */
		final CharSequence[] freebaseDomainIdArray = freebaseSearchRequestVO.getFreebaseDomainIdArray();

		if (freebaseDomainIdArray == null) {

		} else {

			for (final CharSequence domainId : freebaseDomainIdArray) {
				nameValuePairs.add(new BasicNameValuePair(DOMAIN, domainId.toString()));
			}

			if (freebaseSearchRequestVO.getAllDomain()) {
				nameValuePairs.add(new BasicNameValuePair(DOMAIN_STRICT, ALL));
			}

			if (freebaseSearchRequestVO.getAnyDomain()) {
				nameValuePairs.add(new BasicNameValuePair(DOMAIN_STRICT, ANY));
			}

			if (freebaseSearchRequestVO.getShouldDomain()) {
				nameValuePairs.add(new BasicNameValuePair(DOMAIN_STRICT, SHOULD));
			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition, final long id) {

		dipslayFreebaseTopic(groupPosition);

		return true;
	}

	private void dipslayFreebaseTopic(final int groupPosition) {

		final Intent intent = new Intent(this, FreebaseTopic.class);
		@SuppressWarnings("unchecked")
		final Map<String, String> groupData = (Map<String, String>) m_adapter.getGroup(groupPosition);
		intent.putExtra("FREE BASE TYPE INSTANCE", groupData.get(TYPE_UC));
		startActivityForResult(intent, 9030);

	}

	@Override
	public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {

		dipslayFreebaseTopic(groupPosition);

		return true;
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
	private class SelectDataTask extends AsyncTask<WebServiceRequestVO, Void, TreeSet<FreebaseSearchResultVO>> {

		private final ProgressDialog dialog = new ProgressDialog(FreebaseSimpleSearchExpand.this);

		@Override
		protected void onProgressUpdate(final Void... values) {
			super.onProgressUpdate(values);
		}

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setMessage("Searching Freebase...");
			this.dialog.show();
		}

		@Override
		protected TreeSet<FreebaseSearchResultVO> doInBackground(final WebServiceRequestVO... webServiceRequestVOs) {

			final TreeSet<FreebaseSearchResultVO> emptyResult = new TreeSet<FreebaseSearchResultVO>();

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
				FreebaseSimpleSearchExpand.this.runOnUiThread(NorrinRaddUtility.manageAlertDialog(FreebaseSimpleSearchExpand.this, "Internet Connection Error", "Please check you Internet connection."));
				return emptyResult;

			default:
				return emptyResult;
			}

			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode rootNode;
			try {
				rootNode = mapper.readTree(freeBaseData);
			} catch (JsonProcessingException exception) {
				Log.e(FREEBASE_SURFER, "JsonProcessingException: " + freeBaseData + " ##>" + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;

			} catch (IOException exception) {
				Log.e(FREEBASE_SURFER, "IOException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;
			}

			final TreeSet<FreebaseSearchResultVO> resultList;
			final JsonNode freeBaseSimpleSearch = rootNode.findParent("result");
			final FreebaseSearchVO freebaseSearchVO;

			try {
				freebaseSearchVO = (FreebaseSearchVO) mapper.treeToValue(freeBaseSimpleSearch, FreebaseSearchVO.class);
				resultList = new TreeSet<FreebaseSearchResultVO>(Arrays.asList(freebaseSearchVO.getResult()));
			} catch (JsonParseException exception) {
				Log.e(FREEBASE_SURFER, "JsonParseException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;
			} catch (JsonMappingException exception) {
				Log.e(FREEBASE_SURFER, "JsonMappingException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;
			} catch (IOException exception) {
				Log.e(FREEBASE_SURFER, "IOException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;
			}

			if (isCancelled()) {
				return emptyResult;
			}

			return resultList;
		}

		// can use UI thread here
		protected void onPostExecute(final TreeSet<FreebaseSearchResultVO> result) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			m_adapter = new SimpleSearchExpandAdapter(FreebaseSimpleSearchExpand.this, result);
			mExpandableListView.setAdapter(m_adapter);

		}
	}

}
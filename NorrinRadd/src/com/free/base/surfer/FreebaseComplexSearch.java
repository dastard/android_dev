package com.free.base.surfer;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.free.base.surfer.enums.FreeBaseMQLRead;
import com.free.base.surfer.gson.GsonFreebaseDomain;
import com.free.base.surfer.gson.GsonFreebaseDomains;
import com.free.base.surfer.gson.GsonFreebaseType;
import com.free.base.surfer.gson.GsonFreebaseTypes;
import com.free.base.surfer.utility.NorrinRaddUtility;
import com.free.base.surfer.value.objects.FreebaseSearchRequestVO;
import com.free.base.surfer.value.objects.WebServiceRequestVO;
import com.free.base.surfer.value.objects.WebServiceResponseVO;
import com.google.gson.Gson;

public class FreebaseComplexSearch extends Activity {

	@SuppressWarnings("unused")
	private AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseDomain>> mBackgroundDomainTask;
	private final TreeSet<GsonFreebaseDomain> m_freebaseDomainList = new TreeSet<GsonFreebaseDomain>();

	@SuppressWarnings("unused")
	private AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseType>> mBackgroundTypeTask;
	private final TreeSet<GsonFreebaseType> m_freebaseTypeList = new TreeSet<GsonFreebaseType>();
	private static final String QUERY = "query";
	private static final String KEY = "key";
	private static final String API_KEY = "AIzaSyA3OMaYZH1JkdwPlHvG0xVcxFmS47AEcHc";
	private static final String QUERY_FREEBASE = "[{ \"id\": null, \"name\": null, \"type\": \"/type/type\", \"domain\": null }]";
	private static final String QUERY_FREEBASE_DOMAIN = "[{ \"id\": null, \"name\": null, \"type\": \"/type/domain\", \"!/freebase/domain_category/domains\": { \"id\": \"/category/commons\" } }]";


	private FreebaseApplication mFreebaseApplication;

	/*
	 * Type selection
	 */
	protected CharSequence[] freebaseTypeArray;
	protected CharSequence[] freebaseTypeIdArray;
	protected boolean[] freebaseTypeArrayselections;

	/*
	 * Domain selection
	 */
	protected CharSequence[] freebaseDomainArray;
	protected CharSequence[] freebaseDomainIdArray;
	protected boolean[] freebaseDomainArrayselections;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFreebaseApplication = (FreebaseApplication) getApplication();

		displayCustomTileBar();

		/**
		 * Execute Search
		 */
		final Button executeSearch = (Button) findViewById(R.id.search);
		executeSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {

				final RelativeLayout complexSeacrhDetail = (RelativeLayout) view.getParent();

				final EditText queryString = (EditText) complexSeacrhDetail.findViewById(R.id.queryString);
				final EditText queryLimit = (EditText) complexSeacrhDetail.findViewById(R.id.limit);
				
				final CheckBox blacklistCB = (CheckBox) complexSeacrhDetail.findViewById(R.id.blacklist);
				
				final RadioGroup prefixedOrStemmed = (RadioGroup) complexSeacrhDetail.findViewById(R.id.prefixedOrStemmed);
				final RadioGroup typeStrict = (RadioGroup) complexSeacrhDetail.findViewById(R.id.typeStrict);
				final RadioGroup domainStrict = (RadioGroup) complexSeacrhDetail.findViewById(R.id.domainStrict);

				if (queryString != null) {
					if (TextUtils.isEmpty(queryString.getText())) {
						final AlertDialog alertDialog = buildAlertDialog("Freebase Search Error", "Please enter a Query String.");
						alertDialog.show();
						return;
					}
				}

				mFreebaseApplication.setFreebaseSearchRequestVO(buildFreebaseSearchRequestVO(queryString, queryLimit, blacklistCB, prefixedOrStemmed, typeStrict, domainStrict));

				final Intent intent = new Intent(FreebaseComplexSearch.this, FreebaseSimpleSearchExpand.class);
				startActivityForResult(intent, 1);
			}
		});

		/**
		 * Display List of types
		 */
		final Button typeList = (Button) findViewById(R.id.typeList);
		typeList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				mBackgroundTypeTask = new SelectTypeDataTask().execute(buildWebServiceRequestVO(QUERY_FREEBASE));
			}
		});

		/**
		 * Display List of domains
		 */
		final Button domainList = (Button) findViewById(R.id.domainList);
		domainList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View view) {
				mBackgroundDomainTask = new SelectDomainDataTask().execute(buildWebServiceRequestVO(QUERY_FREEBASE_DOMAIN));
			}
		});

	}

	protected FreebaseSearchRequestVO buildFreebaseSearchRequestVO(final EditText queryString, final EditText queryLimit, final CheckBox blacklistCB, final RadioGroup prefixedOrStemmed, final RadioGroup typeStrict, final RadioGroup domainStrict) {

		boolean blacklist = false;

		boolean prefixed = false;
		boolean stemmed = false;

		boolean anyType = false;
		boolean allType = false;
		boolean shouldType = false;

		boolean anyDomain = false;
		boolean allDomain = false;
		boolean shouldDomain = false;
		
		/*
		 * Use blacklist, yes or No?
		 */
		blacklist = blacklistCB.isChecked();
		
		/*
		 * Limit number of results returned
		 */
		Integer queryLimitInteger = 0;

		if (queryLimit == null) {

		} else {
			if (queryLimit.getText() == null) {

			} else {
				final String limitString = queryLimit.getText().toString().trim();
				if (TextUtils.isDigitsOnly(limitString)) {

					if (TextUtils.isEmpty(limitString)) {

					} else {
						queryLimitInteger = new Integer(queryLimit.getText().toString().trim());
					}
				}
			}
		}

		/*
		 * Does the user want Stemmed, Prefixed or neither searches
		 */
		final int selectedOption = prefixedOrStemmed.getCheckedRadioButtonId();

		switch (selectedOption) {
		case R.id.Neither:
			break;

		case R.id.prefixed:
			prefixed = true;
			break;

		case R.id.stemmed:
			stemmed = true;
		}

		/*
		 * Has the user filtered the results by Type
		 */
		final int selectedTypeOption = typeStrict.getCheckedRadioButtonId();

		switch (selectedTypeOption) {
		case R.id.typeStrictNone:
			break;

		case R.id.typeStrictAll:
			allType = true;
			break;

		case R.id.typeStrictShould:
			shouldType = true;
			break;

		case R.id.typeStrictAny:
			anyType = true;
		}

		if ((freebaseTypeIdArray == null) || (freebaseTypeIdArray.length == 0)) {
			allType = false;
			anyType = false;
			shouldType = false;
		}

		/*
		 * Has the user filtered the results by domain
		 */
		final int selectedDomainOption = domainStrict.getCheckedRadioButtonId();

		switch (selectedDomainOption) {
		case R.id.domainStrictNone:
			break;

		case R.id.domainStrictAll:
			allDomain = true;
			break;

		case R.id.domainStrictShould:
			shouldDomain = true;
			break;

		case R.id.domainStrictAny:
			anyDomain = true;
		}

		if ((freebaseDomainIdArray == null) || (freebaseDomainIdArray.length == 0)) {
			allDomain = false;
			anyDomain = false;
			shouldDomain = false;
		}

		final FreebaseSearchRequestVO freebaseSearchRequestVO = new FreebaseSearchRequestVO(queryString.getText().toString(), queryLimitInteger, blacklist, prefixed, stemmed, freebaseTypeIdArray, allType, anyType, shouldType, freebaseDomainIdArray, allDomain, anyDomain, shouldDomain);

		return freebaseSearchRequestVO;
	}

	protected AlertDialog buildAlertDialog(final String alertTitle, final String alertMessage) {

		final AlertDialog alertDialog = new AlertDialog.Builder(FreebaseComplexSearch.this).create();
		alertDialog.setTitle(alertTitle);
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});

		return alertDialog;
	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.complex_search);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Complex Search");

	}

	private WebServiceRequestVO buildWebServiceRequestVO(final String queryFreebase) {

		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(QUERY, queryFreebase));
		nameValuePairs.add(new BasicNameValuePair(KEY, API_KEY));

		return new WebServiceRequestVO(FreeBaseMQLRead.FREE_BASE_MQL_READ, nameValuePairs);
	}

	@Override
	protected Dialog onCreateDialog(final int id) {

		switch (id) {
		case 0:
			return new AlertDialog.Builder(this).setTitle("Select Freebase Type(s)").setMultiChoiceItems(freebaseTypeArray, freebaseTypeArrayselections, new DialogSelectionClickHandler()).setPositiveButton("OK", new DialogButtonClickHandler()).create();
		case 1:
			return new AlertDialog.Builder(this).setTitle("Select Freebase Domain(s)").setMultiChoiceItems(freebaseDomainArray, freebaseDomainArrayselections, new DialogSelectionClickHandler()).setPositiveButton("OK", new DialogButtonDomainClickHandler()).create();
		}

		return new AlertDialog.Builder(this).setTitle("Select Freebase Type(s)").setMultiChoiceItems(freebaseTypeArray, freebaseTypeArrayselections, new DialogSelectionClickHandler()).setPositiveButton("OK", new DialogButtonClickHandler()).create();
	}

	public class DialogSelectionClickHandler implements DialogInterface.OnMultiChoiceClickListener {
		public void onClick(DialogInterface dialog, int clicked, boolean selected) {

		}
	}

	public class DialogButtonClickHandler implements DialogInterface.OnClickListener {

		public void onClick(final DialogInterface dialog, final int clicked) {
			switch (clicked) {
			case DialogInterface.BUTTON_POSITIVE:
				freebaseTypeIdArray = extractTypeIds(freebaseTypeArrayselections, m_freebaseTypeList);
				break;
			}
		}
	}

	public class DialogButtonDomainClickHandler implements DialogInterface.OnClickListener {

		public void onClick(final DialogInterface dialog, final int clicked) {
			switch (clicked) {
			case DialogInterface.BUTTON_POSITIVE:
				freebaseDomainIdArray = extractDomainIds(freebaseDomainArrayselections, m_freebaseDomainList);
				break;
			}
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mFreebaseApplication.resetAttributes();
		removeDialog(0);
		removeDialog(1);
	}

	public CharSequence[] extractDomainIds(final boolean[] freebaseDomainArrayselections, final TreeSet<GsonFreebaseDomain> freebaseDomainList) {
		
		final List<CharSequence> domainIdList = new ArrayList<CharSequence>();

		final CharSequence[] domainIds;

		int arrayIndex = 0;

		for (final GsonFreebaseDomain gsonFreebaseDomain : freebaseDomainList) {

			if (freebaseDomainArrayselections[arrayIndex++]) {
				domainIdList.add(gsonFreebaseDomain.getId());
			}
		}

		domainIds = new CharSequence[domainIdList.size()];

		return domainIdList.toArray(domainIds);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mFreebaseApplication.resetAttributes();
		removeDialog(0);
		removeDialog(1);
	}

	/**
	 * This inner class is used to retrieve the activities Type data in a
	 * background thread
	 * 
	 * while the work is being done we show a dialog box to let the user know we
	 * are still working OK
	 * 
	 * @author Muttley
	 * 
	 */
	private class SelectTypeDataTask extends AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseType>> {

		private final ProgressDialog dialog = new ProgressDialog(FreebaseComplexSearch.this);

		@Override
		protected void onProgressUpdate(final Void... values) {
			super.onProgressUpdate(values);
		}

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setMessage("Retrieving Freebase Types...");
			this.dialog.show();
		}

		@Override
		protected TreeSet<GsonFreebaseType> doInBackground(final WebServiceRequestVO... webServiceRequestVOs) {

			final TreeSet<GsonFreebaseType> emptyResult = new TreeSet<GsonFreebaseType>();

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
				FreebaseComplexSearch.this.runOnUiThread(NorrinRaddUtility.manageAlertDialog(FreebaseComplexSearch.this, "Internet Connection Error", "Please check you Internet connection."));
				return emptyResult;

			default:
				return emptyResult;
			}

			final Gson geeSon = new Gson();
			final GsonFreebaseTypes gsonFreebaseTypes = geeSon.fromJson(freeBaseData, GsonFreebaseTypes.class);
			final TreeSet<GsonFreebaseType> gsonFreebaseTypeList = gsonFreebaseTypes.getResult();

			if (isCancelled()) {
				return emptyResult;
			}

			return gsonFreebaseTypeList;
		}

		// can use UI thread here
		protected void onPostExecute(final TreeSet<GsonFreebaseType> result) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			for (final GsonFreebaseType gsonFreebaseType : result) {

				if (gsonFreebaseType.getId().startsWith("/user")) {

				} else {
					m_freebaseTypeList.add(gsonFreebaseType);
				}
			}

			freebaseTypeArrayselections = new boolean[m_freebaseTypeList.size()];
			freebaseTypeArray = extractTypeNames(m_freebaseTypeList);

			showDialog(0);

		}

	}

	public CharSequence[] extractTypeNames(final TreeSet<GsonFreebaseType> freebaseTypeList) {

		final CharSequence[] typeNames = new CharSequence[freebaseTypeList.size()];
		int arrayIndex = 0;

		for (final GsonFreebaseType gsonFreebaseType : freebaseTypeList) {

			typeNames[arrayIndex++] = gsonFreebaseType.getName();

		}

		return typeNames;
	}

	public CharSequence[] extractTypeIds(final boolean[] freebaseTypeArrayselections, final TreeSet<GsonFreebaseType> freebaseTypeList) {

		final List<CharSequence> typeIdList = new ArrayList<CharSequence>();

		final CharSequence[] typeIds;

		int arrayIndex = 0;

		for (final GsonFreebaseType gsonFreebaseType : freebaseTypeList) {

			if (freebaseTypeArrayselections[arrayIndex++]) {
				typeIdList.add(gsonFreebaseType.getId());
			}
		}

		typeIds = new CharSequence[typeIdList.size()];

		return typeIdList.toArray(typeIds);
	}

	/**
	 * This inner class is used to retrieve the activities Domain data in a
	 * background thread
	 * 
	 * while the work is being done we show a dialog box to let the user know we
	 * are still working OK
	 * 
	 * @author Muttley
	 * 
	 */
	private class SelectDomainDataTask extends AsyncTask<WebServiceRequestVO, Void, TreeSet<GsonFreebaseDomain>> {

		private final ProgressDialog dialog = new ProgressDialog(FreebaseComplexSearch.this);

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
				FreebaseComplexSearch.this.runOnUiThread(NorrinRaddUtility.manageAlertDialog(FreebaseComplexSearch.this, "Internet Connection Error", "Please check you Internet connection."));
				return emptyResult;

			default:
				return emptyResult;
			}

			final Gson geeSon = new Gson();

			final GsonFreebaseDomains domains = geeSon.fromJson(freeBaseData, GsonFreebaseDomains.class);
			final TreeSet<GsonFreebaseDomain> domainList = domains.getResult();

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

			m_freebaseDomainList.addAll(result);

			freebaseDomainArrayselections = new boolean[m_freebaseDomainList.size()];
			freebaseDomainArray = extractDomainNames(m_freebaseDomainList);

			showDialog(1);

		}

	}

	public CharSequence[] extractDomainNames(final TreeSet<GsonFreebaseDomain> freebaseDomainList) {

		final CharSequence[] domainNames = new CharSequence[freebaseDomainList.size()];
		int arrayIndex = 0;

		for (final GsonFreebaseDomain gsonFreebaseDomain : freebaseDomainList) {

			domainNames[arrayIndex++] = gsonFreebaseDomain.getName();

		}

		return domainNames;
	}

}
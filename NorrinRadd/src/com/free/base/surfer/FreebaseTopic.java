package com.free.base.surfer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.free.base.surfer.adapters.TopicAdapter;
import com.free.base.surfer.enums.FreeBaseTopicAPI;
import com.free.base.surfer.gson.GsonFreebaseTypeInstance;
import com.free.base.surfer.utility.NorrinRaddUtility;
import com.free.base.surfer.value.objects.FreebaseTopicTypeVO;
import com.free.base.surfer.value.objects.FreebaseTopicVO;
import com.free.base.surfer.value.objects.FreebaseWebpageVO;
import com.free.base.surfer.value.objects.TopicPropertyContainer;
import com.free.base.surfer.value.objects.WebServiceRequestVO;
import com.free.base.surfer.value.objects.WebServiceResponseVO;

public class FreebaseTopic extends ListActivity {

	private static final String FREE_BASE_TYPE_INSTANCE = "FREE BASE TYPE INSTANCE";
	private static final String FREEBASE_SURFER = "FREEBASE SURFER";
	private static final String KEY = "key";
	private static final String API_KEY = "AIzaSyA3OMaYZH1JkdwPlHvG0xVcxFmS47AEcHc";

	@SuppressWarnings("unused")
	private AsyncTask<WebServiceRequestVO, Void, TreeSet<FreebaseTopicVO>> mBackgroundTask;
	private TopicAdapter m_adapter;
	private static final String ID = "id";
	private static final String DOMAINS = "domains";
	private static final String ALL = "all";
	private String mFreeBaseTypeInstance;
	private FreebaseApplication mFreebaseApplication;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFreebaseApplication = (FreebaseApplication) getApplication();

		displayCustomTileBar();

		mFreeBaseTypeInstance = (savedInstanceState == null) ? null : (String) savedInstanceState.getSerializable(FREE_BASE_TYPE_INSTANCE);
		if (mFreeBaseTypeInstance == null) {
			final Bundle extras = getIntent().getExtras();
			mFreeBaseTypeInstance = extras != null ? extras.getString(FREE_BASE_TYPE_INSTANCE) : null;
		}
		
		mBackgroundTask = new SelectDataTask().execute(buildWebServiceRequestVO());

	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Topic");

	}

	private WebServiceRequestVO buildWebServiceRequestVO() {

		final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(ID, mFreeBaseTypeInstance));
		nameValuePairs.add(new BasicNameValuePair(DOMAINS, ALL));
		nameValuePairs.add(new BasicNameValuePair(KEY, API_KEY));

		return new WebServiceRequestVO(FreeBaseTopicAPI.FREE_BASE_TOPIC_STANDARD, nameValuePairs);
	}

	public void displayAlias(final View view) {

		final FreebaseTopicVO freebaseTopicVO = (FreebaseTopicVO) getListAdapter().getItem((Integer) view.getTag());

		AlertDialog dialog;

		final CharSequence[] items = freebaseTopicVO.getAlias();
		final AlertDialog.Builder builder = new AlertDialog.Builder(FreebaseTopic.this);
		builder.setTitle("Alias");
		builder.setItems(items, null);

		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		dialog = builder.create();
		dialog.show();

	}

	public void displayProperties(final View view) {
		final FreebaseTopicVO freebaseTopicVO = (FreebaseTopicVO) getListAdapter().getItem((Integer) view.getTag());

		final Intent intent = new Intent(this, FreebaseTopicProperties.class);
		intent.putExtra("FREE BASE TOPIC TEXT", freebaseTopicVO.getText());
		startActivityForResult(intent, 163);

	}

	public void displayType(final View view) {

		final FreebaseTopicVO freebaseTopicVO = (FreebaseTopicVO) getListAdapter().getItem((Integer) view.getTag());

		AlertDialog dialog;

		final FreebaseTopicTypeVO[] freebaseTopicTypeVOs = freebaseTopicVO.getType();

		final CharSequence[] items = getTopicTypeText(freebaseTopicTypeVOs);

		final AlertDialog.Builder builder = new AlertDialog.Builder(FreebaseTopic.this);
		builder.setTitle("Type");
		builder.setItems(items, null);

		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		dialog = builder.create();
		dialog.show();

	}

	public void displayWebpage(final View view) {

		final FreebaseTopicVO freebaseTopicVO = (FreebaseTopicVO) getListAdapter().getItem((Integer) view.getTag());

		AlertDialog dialog;

		final FreebaseWebpageVO[] freebaseWebpageVOs = freebaseTopicVO.getWebpage();

		final CharSequence[] items = getTopicWebpages(freebaseWebpageVOs);

		final AlertDialog.Builder builder = new AlertDialog.Builder(FreebaseTopic.this);
		builder.setTitle("Web Page(s)");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int pos) {
				final Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(freebaseWebpageVOs[pos].getUrl()));
				startActivityForResult(intent, 909);
			}
		});

		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		dialog = builder.create();
		dialog.show();

	}

	private CharSequence[] getTopicWebpages(final FreebaseWebpageVO[] freebaseWebpageVOs) {
		final Spanned[] topicWebpage = new Spanned[freebaseWebpageVOs.length];
		int index = 0;

		for (final FreebaseWebpageVO freebaseWebpageVO : freebaseWebpageVOs) {
			topicWebpage[index++] = freebaseWebpageVO.getHtmlUrl();
		}
		return topicWebpage;
	}

	private CharSequence[] getTopicTypeText(final FreebaseTopicTypeVO[] freebaseTopicTypeVOs) {
		final String[] topicTypeText = new String[freebaseTopicTypeVOs.length];
		int index = 0;

		for (final FreebaseTopicTypeVO freebaseTopicTypeVO : freebaseTopicTypeVOs) {

			topicTypeText[index++] = freebaseTopicTypeVO.getText();

		}
		return topicTypeText;
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
	private class SelectDataTask extends AsyncTask<WebServiceRequestVO, Void, TreeSet<FreebaseTopicVO>> {

		private final ProgressDialog dialog = new ProgressDialog(FreebaseTopic.this);

		@Override
		protected void onProgressUpdate(final Void... values) {
			super.onProgressUpdate(values);
		}

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setMessage("Retrieving Freebase Topic...");
			this.dialog.show();
		}

		@Override
		protected TreeSet<FreebaseTopicVO> doInBackground(final WebServiceRequestVO... webServiceRequestVOs) {

			final TreeSet<FreebaseTopicVO> emptyResult = new TreeSet<FreebaseTopicVO>();

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
				FreebaseTopic.this.runOnUiThread(NorrinRaddUtility.manageAlertDialog(FreebaseTopic.this, "Internet Connection Error", "Please check you Internet connection."));
				return emptyResult;

			default:
				return emptyResult;
			}

			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode rootNode;
			try {
				rootNode = mapper.readTree(freeBaseData);
			} catch (JsonProcessingException exception) {
				Log.e(FREEBASE_SURFER, "JsonProcessingException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;

			} catch (IOException exception) {
				Log.e(FREEBASE_SURFER, "IOException: " + exception.getMessage());
				exception.printStackTrace();
				return emptyResult;
			}
			final List<JsonNode> freeBaseTopics = rootNode.findParents("result");
			final JsonNode propertiesOfTopic = rootNode.findValue("properties");

			final TreeSet<FreebaseTopicVO> topicList = new TreeSet<FreebaseTopicVO>();
			TreeMap<String, TopicPropertyContainer> topicProperties;

			for (final JsonNode myJsonNode : freeBaseTopics) {

				final JsonNode topic = myJsonNode.findValue("result");

				final FreebaseTopicVO currentTopic;
				try {
					currentTopic = (FreebaseTopicVO) mapper.treeToValue(topic, FreebaseTopicVO.class);
					topicProperties = mapper.readValue(propertiesOfTopic, new TypeReference<TreeMap<String, TopicPropertyContainer>>() {
					});
					currentTopic.setTopicProperties(topicProperties);
					mFreebaseApplication.setTopicProperties(topicProperties);

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

				if (TextUtils.isEmpty(currentTopic.getThumbnail())) {

				} else {
					currentTopic.getBitmapFromURL();
				}

				topicList.add(currentTopic);

			}

			if (isCancelled()) {
				return emptyResult;
			}

			return topicList;
		}

		// can use UI thread here
		protected void onPostExecute(final TreeSet<FreebaseTopicVO> result) {

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			m_adapter = new TopicAdapter(FreebaseTopic.this, R.layout.topic_list, new ArrayList<FreebaseTopicVO>(result));
			setListAdapter(m_adapter);

		}

	}

}
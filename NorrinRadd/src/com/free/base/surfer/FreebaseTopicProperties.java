package com.free.base.surfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.free.base.surfer.adapters.TopicPropertyAdapter;
import com.free.base.surfer.value.objects.TopicPropertyContainer;
import com.free.base.surfer.value.objects.TopicPropertyContainerVO;

public class FreebaseTopicProperties extends ListActivity {

	private static final String FREE_BASE_TOPIC_TEXT = "FREE BASE TOPIC TEXT";
	private TopicPropertyAdapter m_adapter;
	private List<TopicPropertyContainerVO> topicPropertyContainerList;
	private String mFreeBaseTopicText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayCustomTileBar();

		mFreeBaseTopicText = (savedInstanceState == null) ? null : (String) savedInstanceState.getSerializable(FREE_BASE_TOPIC_TEXT);
		if (mFreeBaseTopicText == null) {
			final Bundle extras = getIntent().getExtras();
			mFreeBaseTopicText = extras != null ? extras.getString(FREE_BASE_TOPIC_TEXT) : null;
		}

		final TextView listHeader = (TextView) findViewById(R.id.listHeader);
		listHeader.setText(mFreeBaseTopicText);

		topicPropertyContainerList = extractTopicPropertyContainers((FreebaseApplication) getApplication());

		m_adapter = new TopicPropertyAdapter(FreebaseTopicProperties.this, R.layout.topic_properties_list, topicPropertyContainerList);
		setListAdapter(m_adapter);

	}

	private List<TopicPropertyContainerVO> extractTopicPropertyContainers(final FreebaseApplication freebaseApplication) {

		final List<TopicPropertyContainerVO> topicPropertyContainerVOList = new ArrayList<TopicPropertyContainerVO>();

		for (final Map.Entry<String, TopicPropertyContainer> topicPropertyContainerEntry : freebaseApplication.getTopicProperties().entrySet()) {

			topicPropertyContainerVOList.add(new TopicPropertyContainerVO(topicPropertyContainerEntry.getKey(), topicPropertyContainerEntry.getValue()));

		}

		return topicPropertyContainerVOList;
	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_header_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Topic Properties");

	}
	
	@Override
	protected void onListItemClick(final ListView list, final View view, final int position, final long id) {
		super.onListItemClick(list, view, position, id);
//		final Intent intent = new Intent(this, FreebaseTopicPropertyValues.class);
		final Intent intent = new Intent(this, FreebaseTopicPropertyValuesExpand.class);
		final TopicPropertyContainerVO selectedTopicPropertyContainerVO = ((TopicPropertyContainerVO) getListAdapter().getItem(position));
		final FreebaseApplication freebaseApplication = (FreebaseApplication) getApplication();
		freebaseApplication.setTopicPropertyContainer(selectedTopicPropertyContainerVO.getTopicPropertyContainer());
		startActivityForResult(intent, 502);
	}



}
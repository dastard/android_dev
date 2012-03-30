package com.free.base.surfer;

import java.util.Arrays;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.free.base.surfer.adapters.TopicPropertyValueAdapter;
import com.free.base.surfer.value.objects.TopicPropertyContainer;
import com.free.base.surfer.value.objects.TopicPropertyValueContainer;

public class FreebaseTopicPropertyValues extends ListActivity {

	private FreebaseApplication mFreebaseApplication;
	private TopicPropertyValueAdapter m_adapter;
	private TopicPropertyContainer mTopicPropertyContainer;
	private List<TopicPropertyValueContainer> mTopicPropertyValueContainerList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		displayCustomTileBar();

		mFreebaseApplication = (FreebaseApplication) getApplication();
		mTopicPropertyContainer = mFreebaseApplication.getTopicPropertyContainer();

		final TextView listHeader = (TextView) findViewById(R.id.listHeader);
		listHeader.setText(mTopicPropertyContainer.getText());

		mTopicPropertyValueContainerList = Arrays.asList(mTopicPropertyContainer.getValues());

		m_adapter = new TopicPropertyValueAdapter(FreebaseTopicPropertyValues.this, R.layout.topic_property_value_list, mTopicPropertyValueContainerList);
		setListAdapter(m_adapter);

	}

	private void displayCustomTileBar() {

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.main_header_list);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		final TextView custom_title_center = (TextView) findViewById(R.id.title_centre_text);
		custom_title_center.setText("Freebase Topic Property Values");

	}

}
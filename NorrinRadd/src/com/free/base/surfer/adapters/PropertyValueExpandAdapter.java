package com.free.base.surfer.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.base.surfer.R;
import com.free.base.surfer.value.objects.TopicPropertyContainer;
import com.free.base.surfer.value.objects.TopicPropertyValue;
import com.free.base.surfer.value.objects.TopicPropertyValueContainer;
import com.free.base.surfer.value.objects.TopicPropertyValues;

public class PropertyValueExpandAdapter extends BaseExpandableListAdapter {
	private static final String NAME = "NAME";
	private static final String PROPERTY_VALUE = "PROPERTY_VALUE";
	private static final String PROPERTY_URL = "PROPERTY_URL";

	private final List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	private final List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	private final Context mContext;

	public PropertyValueExpandAdapter(final Context context, final TopicPropertyContainer _topicPropertyContainer) {
		super();

		mContext = context;

		for (final TopicPropertyValueContainer topicPropertyValueContainer : _topicPropertyContainer.getValues()) {

			final Map<String, String> curGroupMap = new HashMap<String, String>();
			groupData.add(curGroupMap);
			curGroupMap.put(NAME, topicPropertyValueContainer.getText());
			curGroupMap.put(PROPERTY_VALUE, topicPropertyValueContainer.getId());
			curGroupMap.put(PROPERTY_URL, topicPropertyValueContainer.getUrl());

			final List<Map<String, String>> children = new ArrayList<Map<String, String>>();

			final Map<String, TopicPropertyValues> topicPropertyValuesMapEntry = topicPropertyValueContainer.getPropertyValues();

			for (final Map.Entry<String, TopicPropertyValues> entry : topicPropertyValuesMapEntry.entrySet()) {

				final TopicPropertyValues topicPropertyContainer = entry.getValue();
				final TopicPropertyValue[] values = topicPropertyContainer.getValues();

				for (final TopicPropertyValue topicPropertyValue : values) {
					final Map<String, String> curChildMap = new HashMap<String, String>();
					children.add(curChildMap);
					curChildMap.put(NAME, topicPropertyValue.getText());
					curChildMap.put(PROPERTY_VALUE, topicPropertyValue.getId());
					curChildMap.put(PROPERTY_URL, topicPropertyValue.getUrl());
				}

			}
			childData.add(children);

		}

	}

	@Override
	public Object getChild(final int groupIndex, final int childIndex) {

		final List<Map<String, String>> selectedChild = childData.get(groupIndex);

		return selectedChild.get(childIndex);
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {

		final List<Map<String, String>> selectedChild = childData.get(groupPosition);

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.property_child_list, null);
		}

		final TextView childPropertyText = (TextView) convertView.findViewById(R.id.childPropertyText);
		final String rawText = selectedChild.get(childPosition).get(NAME);
		final String rawUrl = selectedChild.get(childPosition).get(PROPERTY_URL);

		if (TextUtils.isEmpty(rawUrl)) {
			childPropertyText.setText(rawText);
		} else {
			childPropertyText.setText(Html.fromHtml("<a href=\"" + rawUrl.trim() + "\">" + rawText + "</a>"));
			childPropertyText.setMovementMethod(LinkMovementMethod.getInstance());
		}

		final TextView childPropertyId = (TextView) convertView.findViewById(R.id.childPropertyId);
		childPropertyId.setText(selectedChild.get(childPosition).get(PROPERTY_VALUE));

		if (1 == (groupPosition % 2)) {
			if (1 == (childPosition % 2)) {
				convertView.setBackgroundColor(android.graphics.Color.WHITE);
			} else {
				convertView.setBackgroundColor(0);
			}
		} else {
			if (1 == (childPosition % 2)) {
				convertView.setBackgroundColor(0);
			} else {
				convertView.setBackgroundColor(android.graphics.Color.WHITE);
			}
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		final List<Map<String, String>> selectedChild = childData.get(groupPosition);

		return selectedChild.size();
	}

	@Override
	public Object getGroup(final int groupPosition) {

		return groupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupData.size();
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
		final Map<String, String> selectGroupData = groupData.get(groupPosition);

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.property_group_list, null);
		}

		final View groupIndicator = convertView.findViewById(R.id.explist_indicator);

		if (groupIndicator != null) {
			final ImageView indicator = (ImageView) groupIndicator;
			indicator.setVisibility(View.VISIBLE);
			indicator.setFocusable(false);
			if (getChildrenCount(groupPosition) == 0) {
				indicator.setImageResource(R.drawable.freebase_empty);
			} else {
				if (isExpanded) {
					indicator.setImageResource(R.drawable.freebase_expanded);
				} else {
					indicator.setImageResource(R.drawable.freebase_collapsed);
				}
			}
		}

		final TextView groupPropertyText = (TextView) convertView.findViewById(R.id.groupPropertyText);
		final String rawText = selectGroupData.get(NAME);
		final String rawUrl = selectGroupData.get(PROPERTY_URL);
		final String finalRawText;

		if (TextUtils.isEmpty(rawUrl)) {
			groupPropertyText.setText(rawText);
		} else {
			if (TextUtils.isEmpty(rawText)) {
				finalRawText = "Web Site";				
			}  else{
				finalRawText = rawText;
			}
			groupPropertyText.setText(Html.fromHtml("<a href=\"" + rawUrl.trim() + "\">" + finalRawText + "</a>"));
			groupPropertyText.setMovementMethod(LinkMovementMethod.getInstance());
		}

		if (1 == (groupPosition % 2)) {
			convertView.setBackgroundColor(android.graphics.Color.WHITE);
		} else {
			convertView.setBackgroundColor(0);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition, final int childPosition) {
		return true;
	}

}

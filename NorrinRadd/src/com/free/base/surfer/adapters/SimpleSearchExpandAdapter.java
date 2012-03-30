package com.free.base.surfer.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.base.surfer.R;
import com.free.base.surfer.value.objects.FreebaseSearchResultVO;
import com.free.base.surfer.value.objects.FreebaseSearchTypeVO;

public class SimpleSearchExpandAdapter extends BaseExpandableListAdapter {

	private static final String NAME = "NAME";
	private static final String TYPE = "TYPE";

	private final List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	private final List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
	private final Context mContext;

	public SimpleSearchExpandAdapter(final Context context, final TreeSet<FreebaseSearchResultVO> result) {
		super();

		mContext = context;

		for (final FreebaseSearchResultVO freebaseSearchResultVO : result) {

			final Map<String, String> currentGroup = new HashMap<String, String>();
			groupData.add(currentGroup);
			currentGroup.put(NAME, freebaseSearchResultVO.getName());
			currentGroup.put(TYPE, freebaseSearchResultVO.getId());

			final List<Map<String, String>> currentChild = new ArrayList<Map<String, String>>();

			for (final FreebaseSearchTypeVO freebaseSearchTypeVO : freebaseSearchResultVO.getType()) {

				final Map<String, String> currentChildMap = new HashMap<String, String>();
				currentChild.add(currentChildMap);
				currentChildMap.put(NAME, freebaseSearchTypeVO.getName());
				currentChildMap.put(TYPE, freebaseSearchTypeVO.getId());

			}
			childData.add(currentChild);

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
			convertView = inflater.inflate(R.layout.simple_search_child_list, null);
		}

		final TextView childTypeName = (TextView) convertView.findViewById(R.id.childTypeName);
		childTypeName.setText(selectedChild.get(childPosition).get(NAME));

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
			convertView = inflater.inflate(R.layout.simple_search_group_list, null);
		}

		final View groupIndicator = convertView.findViewById(R.id.explist_indicator);

		if (groupIndicator != null) {
			final ImageView indicator = (ImageView) groupIndicator;
			indicator.setVisibility(View.VISIBLE);
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

		final TextView groupTypeName = (TextView) convertView.findViewById(R.id.groupTypeName);
		groupTypeName.setText(selectGroupData.get(NAME));

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

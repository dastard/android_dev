package com.free.base.surfer.adapters;

import java.util.List;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.free.base.surfer.R;
import com.free.base.surfer.value.objects.FreebaseTopicVO;

public class TopicAdapter extends ArrayAdapter<FreebaseTopicVO> {

	// declaring our ArrayList of items
	private List<FreebaseTopicVO> objects;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public TopicAdapter(final Context context, final int textViewResourceId, final List<FreebaseTopicVO> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	@Override
	public FreebaseTopicVO getItem(final int position) {
		return objects.get(position);
	}

	/*
	 * we are overriding the getView method here - this is what defines how each
	 * list item will look.
	 */
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		// assign the view we are converting to a local variable
		View view = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.topic_list, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		final FreebaseTopicVO freebaseTopic = objects.get(position);

		if (freebaseTopic != null) {

			final TextView topicTextTextView = (TextView) view.findViewById(R.id.topicText);
			if (topicTextTextView != null) {
				topicTextTextView.setText(freebaseTopic.getText());
				topicTextTextView.setTag(position);
				if (freebaseTopic.getAlias() == null) {

				} else {
					if (freebaseTopic.getAlias().length > 1) {
						topicTextTextView.setClickable(true);
					}
				}
			}

			final ImageView topicThumbnailImageView = (ImageView) view.findViewById(R.id.topicThumbnail);
			if (topicThumbnailImageView != null) {
				topicThumbnailImageView.setImageBitmap(freebaseTopic.getThumbnailImage());
			}

			final TextView topicDecriptionTextView = (TextView) view.findViewById(R.id.topicDescription);
			if (topicDecriptionTextView != null) {
				topicDecriptionTextView.setText(freebaseTopic.getDescription());
				topicDecriptionTextView.setTag(position);
				if (freebaseTopic.getWebpage() == null) {

				} else {
					if (freebaseTopic.getWebpage().length > 0) {
						topicDecriptionTextView.setClickable(true);
					}
				}
			}

			final TextView topicUrlTextView = (TextView) view.findViewById(R.id.topicURL);
			if (topicUrlTextView != null) {
				topicUrlTextView.setText(freebaseTopic.getUrl());
				topicUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());
			}

			final Button properties = (Button) view.findViewById(R.id.topicProperties);
			properties.setTag(position);

		}

		return view;

	}

}

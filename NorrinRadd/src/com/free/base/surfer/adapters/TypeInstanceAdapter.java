package com.free.base.surfer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.free.base.surfer.R;
import com.free.base.surfer.gson.GsonFreebaseTypeInstance;

public class TypeInstanceAdapter extends ArrayAdapter<GsonFreebaseTypeInstance> {

	// declaring our ArrayList of items
	private List<GsonFreebaseTypeInstance> objects;

	/*
	 * here we must override the constructor for ArrayAdapter the only variable
	 * we care about now is ArrayList<Item> objects, because it is the list of
	 * objects we want to display.
	 */
	public TypeInstanceAdapter(final Context context, final int textViewResourceId, final List<GsonFreebaseTypeInstance> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	@Override
	public GsonFreebaseTypeInstance getItem(final int position) {
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
			view = inflater.inflate(R.layout.type_instance_list, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		final GsonFreebaseTypeInstance freebaseTypeInstance = objects.get(position);

		if (freebaseTypeInstance != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			final TextView typeInstanceNameTextView = (TextView) view.findViewById(R.id.typeInstanceName);

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (typeInstanceNameTextView != null) {
				typeInstanceNameTextView.setText(freebaseTypeInstance.getName());
			}

			if (1 == (position % 2)) {
				view.setBackgroundColor(android.graphics.Color.WHITE);
			} else {
				view.setBackgroundColor(0);
			}

		}

		// the view must be returned to our activity
		return view;

	}

}

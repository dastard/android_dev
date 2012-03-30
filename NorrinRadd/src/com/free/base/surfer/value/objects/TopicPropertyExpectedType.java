package com.free.base.surfer.value.objects;

import android.text.Html;

public class TopicPropertyExpectedType {
	
	private String id;
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return Html.fromHtml(text).toString();
	}

	public void setText(String text) {
		this.text = text;
	}

}

package com.free.base.surfer.value.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.text.Html;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicPropertyValue {

	private String id;
	private String text;
	private String url;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

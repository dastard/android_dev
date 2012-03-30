package com.free.base.surfer.value.objects;

import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.text.Html;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicPropertyValueContainer {

	private Map<String, TopicPropertyValues> propertyValues = new TreeMap<String, TopicPropertyValues>();
	private String id;
	private String text;
	private String value;
	private String url;

	public Map<String, TopicPropertyValues> getPropertyValues() {
		return propertyValues;
	}

	@JsonAnySetter
	public void setUnknown(final String name, final TopicPropertyValues unknownObject) {

		propertyValues.put(name, unknownObject);
	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}

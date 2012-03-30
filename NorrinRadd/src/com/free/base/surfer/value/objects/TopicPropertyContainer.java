package com.free.base.surfer.value.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.text.Html;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicPropertyContainer {

	private TopicPropertyExpectedType expected_type;
	private TopicProperty[] properties;
	private String text;
	private TopicPropertyValueContainer[] values;

	public TopicPropertyExpectedType getExpected_type() {
		return expected_type;
	}

	public void setExpected_type(TopicPropertyExpectedType expected_type) {
		this.expected_type = expected_type;
	}

	public TopicProperty[] getProperties() {
		return properties;
	}

	public void setProperties(TopicProperty[] properties) {
		this.properties = properties;
	}

	public String getText() {
		return Html.fromHtml(text).toString();
	}

	public void setText(String text) {
		this.text = text;
	}

	public TopicPropertyValueContainer[] getValues() {
		return values;
	}

	public void setValues(TopicPropertyValueContainer[] values) {
		this.values = values;
	}

}

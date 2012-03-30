package com.free.base.surfer.value.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.text.Html;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicPropertyValues {

	private TopicPropertyExpectedType expected_type;
	private String text;
	private TopicPropertyValue[] values;

	public TopicPropertyExpectedType getExpected_type() {
		return expected_type;
	}

	public void setExpected_type(TopicPropertyExpectedType expected_type) {
		this.expected_type = expected_type;
	}

	public String getText() {
		return Html.fromHtml(text).toString();
	}

	public void setText(String text) {
		this.text = text;
	}

	public TopicPropertyValue[] getValues() {
		return values;
	}

	public void setValues(TopicPropertyValue[] values) {
		this.values = values;
	}

}

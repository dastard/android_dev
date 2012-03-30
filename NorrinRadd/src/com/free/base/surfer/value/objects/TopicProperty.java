package com.free.base.surfer.value.objects;

public class TopicProperty {

	private TopicPropertyExpectedType expected_type;
	private String id;
	private String text;
	private String unit;

	public TopicPropertyExpectedType getExpected_type() {
		return expected_type;
	}

	public void setExpected_type(TopicPropertyExpectedType expected_type) {
		this.expected_type = expected_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}

package com.free.base.surfer.value.objects;

public class TopicPropertyContainerVO {

	private final String propertyKey;
	private final TopicPropertyContainer topicPropertyContainer;

	public TopicPropertyContainerVO(final String propertyKey, final TopicPropertyContainer topicPropertyContainer) {
		super();
		this.propertyKey = propertyKey;
		this.topicPropertyContainer = topicPropertyContainer;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public TopicPropertyContainer getTopicPropertyContainer() {
		return topicPropertyContainer;
	}

}

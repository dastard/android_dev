package com.free.base.surfer;

import java.util.TreeMap;

import com.free.base.surfer.value.objects.FreebaseSearchRequestVO;
import com.free.base.surfer.value.objects.TopicPropertyContainer;

import android.app.Application;

public class FreebaseApplication extends Application {

	private TreeMap<String, TopicPropertyContainer> topicProperties;
	private TopicPropertyContainer topicPropertyContainer;

	private FreebaseSearchRequestVO freebaseSearchRequestVO;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public TreeMap<String, TopicPropertyContainer> getTopicProperties() {
		return topicProperties;
	}

	public void setTopicProperties(TreeMap<String, TopicPropertyContainer> topicProperties) {
		this.topicProperties = topicProperties;
	}

	public TopicPropertyContainer getTopicPropertyContainer() {
		return topicPropertyContainer;
	}

	public void setTopicPropertyContainer(TopicPropertyContainer topicPropertyContainer) {
		this.topicPropertyContainer = topicPropertyContainer;
	}

	public FreebaseSearchRequestVO getFreebaseSearchRequestVO() {
		return freebaseSearchRequestVO;
	}

	public void setFreebaseSearchRequestVO(FreebaseSearchRequestVO freebaseSearchRequestVO) {
		this.freebaseSearchRequestVO = freebaseSearchRequestVO;
	}

	public void resetAttributes() {
		topicPropertyContainer = null;
		topicProperties = null;
		freebaseSearchRequestVO = null;

	}

}

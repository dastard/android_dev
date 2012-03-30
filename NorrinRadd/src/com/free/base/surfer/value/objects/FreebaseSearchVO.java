package com.free.base.surfer.value.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FreebaseSearchVO {
	
	private FreebaseSearchResultVO[] result;

	public FreebaseSearchResultVO[] getResult() {
		return result;
	}

	public void setResult(FreebaseSearchResultVO[] result) {
		this.result = result;
	}
	
	
	
	

}

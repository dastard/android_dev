package com.free.base.surfer.value.objects;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.free.base.surfer.enums.WebServiceable;

public class WebServiceRequestVO implements Serializable {

	private static final long serialVersionUID = -7639305843198056242L;

	private final WebServiceable webService;
	private final ArrayList<NameValuePair> nameValuePairs;

	public WebServiceRequestVO(final WebServiceable webService, final ArrayList<NameValuePair> nameValuePairs) {
		super();
		this.webService = webService;
		this.nameValuePairs = nameValuePairs;
	}

	public WebServiceable getWebService() {
		return webService;
	}

	public ArrayList<NameValuePair> getNameValuePairs() {
		return nameValuePairs;
	}

}

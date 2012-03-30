package com.free.base.surfer.value.objects;

import java.io.Serializable;

public class WebServiceResponseVO  implements Serializable {

	private static final long serialVersionUID = -7639305843198056242L;

	private final String webServiceResponseData;
	private final int httpStatusCode;

	public WebServiceResponseVO(final String webServiceResponseData, final int httpStatusCode) {
		super();
		this.webServiceResponseData = webServiceResponseData;
		this.httpStatusCode = httpStatusCode;
	}

	public String getWebServiceResponseData() {
		return webServiceResponseData;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

}

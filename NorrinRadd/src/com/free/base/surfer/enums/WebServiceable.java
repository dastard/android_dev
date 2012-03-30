package com.free.base.surfer.enums;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.free.base.surfer.value.objects.WebServiceResponseVO;

public interface WebServiceable {
	
	public WebServiceResponseVO execute(final ArrayList<NameValuePair> nameValuePairs);

}

package com.free.base.surfer.enums;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.free.base.surfer.value.objects.WebServiceResponseVO;

public enum FreeBaseMQLRead implements WebServiceable {

	FREE_BASE_MQL_READ;

	private static final String HTTPS = "https";
	private static final String URL_GOOGLE_API = "www.googleapis.com/freebase/v1";
	private static final String MQL_READ = "/mqlread";
	private static final String UTF_8 = "UTF-8";

	private static final String FREEBASE_SURFER = "FREEBASE SURFER";

	public WebServiceResponseVO execute(final ArrayList<NameValuePair> nameValuePairs) {

		final WebServiceResponseVO webServiceFailedResponseVO = new WebServiceResponseVO("", HttpStatus.SC_SERVICE_UNAVAILABLE);

		final URI uri;
		try {
			uri = URIUtils.createURI(HTTPS, URL_GOOGLE_API, -1, MQL_READ, URLEncodedUtils.format(nameValuePairs, UTF_8), null);
		} catch (URISyntaxException exception) {
			Log.e(FREEBASE_SURFER, "URISyntaxException: " + exception.getMessage());
			exception.printStackTrace();
			return webServiceFailedResponseVO;
		}
		
		final HttpGet request = new HttpGet(uri);
		BufferedReader in = null;
		final StringBuffer sb = new StringBuffer("");
		HttpResponse response = null;

		String result = null;

		try {
			final DefaultHttpClient client = new DefaultHttpClient();
			response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			final String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			result = sb.toString();
		} catch (ClientProtocolException exception) {
			Log.e(FREEBASE_SURFER, "ClientProtocolException: " + exception.getMessage());
			exception.printStackTrace();
			return webServiceFailedResponseVO;

		} catch (IOException exception) {
			Log.e(FREEBASE_SURFER, "IOException: " + exception.getMessage());
			exception.printStackTrace();
			return webServiceFailedResponseVO;

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException exception) {
					Log.e(FREEBASE_SURFER, "IOException: " + exception.getMessage());
					exception.printStackTrace();
					return webServiceFailedResponseVO;
				}
			}
		}

		return new WebServiceResponseVO(result, response.getStatusLine().getStatusCode());
	}

}

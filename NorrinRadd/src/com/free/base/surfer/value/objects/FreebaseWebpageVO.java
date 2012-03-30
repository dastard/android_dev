package com.free.base.surfer.value.objects;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

public class FreebaseWebpageVO {

	private String text;
	private String url;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		return url.trim();
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Spanned getHtmlUrl() {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		return Html.fromHtml("<a href=\"" + url.trim() + "\">" + getText() + "</a>");
	}


}

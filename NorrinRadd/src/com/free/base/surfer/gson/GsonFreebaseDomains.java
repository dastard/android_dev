package com.free.base.surfer.gson;

import java.util.TreeSet;

public class GsonFreebaseDomains {

	TreeSet<GsonFreebaseDomain> result;
	String cursor;

	public TreeSet<GsonFreebaseDomain> getResult() {
		return result;
	}

	public void setResult(TreeSet<GsonFreebaseDomain> result) {
		this.result = result;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

}

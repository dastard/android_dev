package com.free.base.surfer.gson;

import java.util.TreeSet;

public class GsonFreebaseTypes {

	TreeSet<GsonFreebaseType> result;

	String cursor;

	public TreeSet<GsonFreebaseType> getResult() {
		return result;
	}

	public void setResult(TreeSet<GsonFreebaseType> result) {
		this.result = result;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}

}

package com.free.base.surfer.gson;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

public class GsonFreebaseType implements Comparable<GsonFreebaseType>{

	@SerializedName("domain")
	private String domain;

	@SerializedName("type")
	private String type;

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	public String getDomain() {
		return domain;
	}

	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return Html.fromHtml(name).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GsonFreebaseType other = (GsonFreebaseType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(GsonFreebaseType another) {
		
		return this.getName().compareTo(another.getName());
	}
	
	

}

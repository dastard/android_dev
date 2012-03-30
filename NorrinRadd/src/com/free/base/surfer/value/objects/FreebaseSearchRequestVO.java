package com.free.base.surfer.value.objects;

public class FreebaseSearchRequestVO {

	private final String queryString;
	private final Integer queryLimitInteger;
	
	private final boolean blacklist;
	
	private final boolean prefixed;
	private final boolean stemmed;
	
	private final CharSequence[] freebaseTypeIdArray;
	private final boolean allType;
	private final boolean anyType;
	private final boolean shouldType;

	private final CharSequence[] freebaseDomainIdArray;
	private final boolean allDomain;
	private final boolean anyDomain;
	private final boolean shouldDomain;

	public FreebaseSearchRequestVO(final String queryString, final Integer queryLimitInteger, final boolean blacklist, final boolean prefixed, final boolean stemmed, final CharSequence[] freebaseTypeIdArray, final boolean allType, final boolean anyType, final boolean shouldType, final CharSequence[] freebaseDomainIdArray, final boolean allDomain, final boolean anyDomain, final boolean shouldDomain) {
		super();
		this.queryString = queryString;
		this.queryLimitInteger = queryLimitInteger;
		
		this.blacklist = blacklist;
		
		this.prefixed = prefixed;
		this.stemmed = stemmed;
		
		this.freebaseTypeIdArray = freebaseTypeIdArray;
		this.allType = allType;
		this.anyType = anyType;
		this.shouldType = shouldType;
		
		this.freebaseDomainIdArray = freebaseDomainIdArray;
		this.allDomain = allDomain;
		this.anyDomain = anyDomain;
		this.shouldDomain = shouldDomain;
	}

	public String getQueryString() {
		return queryString;
	}

	public Integer getQueryLimitInteger() {
		return queryLimitInteger;
	}

	public boolean getBlacklist() {
		return blacklist;
	}

	public boolean getPrefixed() {
		return prefixed;
	}

	public boolean getStemmed() {
		return stemmed;
	}

	public CharSequence[] getFreebaseTypeIdArray() {
		return freebaseTypeIdArray;
	}

	public boolean getAllType() {
		return allType;
	}

	public boolean getAnyType() {
		return anyType;
	}
	
	public boolean getShouldType() {
		return shouldType;
	}

	public CharSequence[] getFreebaseDomainIdArray() {
		return freebaseDomainIdArray;
	}

	public boolean getAllDomain() {
		return allDomain;
	}

	public boolean getAnyDomain() {
		return anyDomain;
	}

	public boolean getShouldDomain() {
		return shouldDomain;
	}	
	
}

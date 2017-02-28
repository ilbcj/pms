package com.pms.webservice.model;

public class Item {
	public static final String SORTTYPEASC = "ASC";
	public static final String SORTTYPEDESC = "DESC";
	private String key;
	private String eng;
	private String val;
	private String sort;
	private boolean	hasAccessAuth;
	
	public boolean isHasAccessAuth() {
		return hasAccessAuth;
	}
	public void setHasAccessAuth(boolean hasAccessAuth) {
		this.hasAccessAuth = hasAccessAuth;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getEng() {
		return eng;
	}
	public void setEng(String eng) {
		this.eng = eng;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}

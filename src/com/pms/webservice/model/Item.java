package com.pms.webservice.model;

public class Item {
	private String key;
	private String eng;
	private String val;
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
}

package com.pms.model;

public class AttrDictionary {
	
	private int id;
	private int attrid;
	private String value="";
	private String code;
	private String tstamp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAttrid() {
		return attrid;
	}
	public void setAttrid(int attrid) {
		this.attrid = attrid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
}

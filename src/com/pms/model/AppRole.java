package com.pms.model;

public class AppRole {

	private int id;
	private int appid;
	private String name="";
	private String code="";
	private String memo;
	private String tstamp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	

}

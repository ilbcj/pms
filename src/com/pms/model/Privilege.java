package com.pms.model;

public class Privilege {
	public static final int OWNERTYPEORG = 1;
	public static final int OWNERTYPEUSER = 2;
	public static final int OWNERTYPEUSERGROUP = 3;
	
	private int id;
	private int owner_id;
	private int owner_type;
//	private int app_id;
	private int role_id;
//	private String endtime; 
	private String tstamp;
	
//	public String getEndtime() {
//		return endtime;
//	}
//	public void setEndtime(String endtime) {
//		this.endtime = endtime;
//	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}
	public int getOwner_type() {
		return owner_type;
	}
	public void setOwner_type(int owner_type) {
		this.owner_type = owner_type;
	}
//	public int getApp_id() {
//		return app_id;
//	}
//	public void setApp_id(int app_id) {
//		this.app_id = app_id;
//	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

}

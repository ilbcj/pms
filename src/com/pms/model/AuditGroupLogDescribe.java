package com.pms.model;

import java.sql.Blob;

public class AuditGroupLogDescribe {
	private int id;
	private int logid;
	private Blob describ;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLogid() {
		return logid;
	}
	public void setLogid(int logid) {
		this.logid = logid;
	}
	public Blob getDescrib() {
		return describ;
	}
	public void setDescrib(Blob describ) {
		this.describ = describ;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}
	
}

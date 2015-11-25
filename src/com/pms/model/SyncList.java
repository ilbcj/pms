package com.pms.model;

public class SyncList {
	public final static int STATUS_GENERATED = 0;
	public final static int STATUS_NOTICED = 1;
	public final static int STATUS_REQUIRED = 2;
	public final static int STATUS_SENDOUT = 3;
	public final static int STATUS_FINISH = 4;
	public final static int STATUS_DELETE = 5;
	
	private int id;
	private String GA_DEPARTMENT;
	private String filename;
	private String tstamp;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}

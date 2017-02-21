package com.pms.model;

public class ImportLog {
	public static final int TYPE_RESOURCE_DATA = 1;
	public static final int TYPE_RESOURCE_FUNC = 2;
	public static final int TYPE_RESOURCE_ROLE = 3;
	
	private int id;
	private int type;
	private String message;
	private long ts;
	private String tsStr;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public String getTsStr() {
		return tsStr;
	}
	public void setTsStr(String tsStr) {
		this.tsStr = tsStr;
	}
}

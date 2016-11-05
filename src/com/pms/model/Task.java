package com.pms.model;

public class Task {
	public static final int TASKTYPE_COLUMNCODE = 1;
	
	public static final int TASKSTATUSINIT = 1;
	public static final int TASKSTATUSPROCESSED = 2;
	
	private int id;
	private int type;
	private int status;
	private String message;
	private String detail;
	private String eid;
	
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
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
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}

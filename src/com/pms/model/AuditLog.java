package com.pms.model;

public class AuditLog {
	public final static String LOGFLAGADD = "add";
	public final static String LOGFLAGUPDATE = "update";
	public final static String LOGFLAGDELETE = "delete";
	public final static String LOGDESCRIBSUCCESS = "成功";
	public final static String LOGDESCRIBFAIL = "失败"; 
	public final static int LOGTYPEUSER = 1;
	public final static int LOGTYPERESOURCEDATA = 2;
	public final static int LOGTYPEORG = 3;
	public final static int LOGTYPEROLE = 4;
	
	private int id;
	private int adminId;
	private String ipAddr;
	private int type;
	private String flag;
	private String describ;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDescrib() {
		return describ;
	}
	public void setDescrib(String describ) {
		this.describ = describ;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}	
	
}

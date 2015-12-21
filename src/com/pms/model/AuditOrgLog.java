package com.pms.model;

public class AuditOrgLog {
	public final static String LOGFLAGQUERY = "query";
	public final static String LOGFLAGADD = "add";
	public final static String LOGFLAGUPDATE = "update";
	public final static String LOGFLAGDELETE = "delete";
	public final static String LOGFLAGIMPORT = "import";
	public final static String LOGFLAGEXPORT = "export";
	public final static String LOGRESULTSUCCESS = "成功";
	public final static String LOGRESULTFAIL = "失败"; 
	
	private int id;
	private int adminId;
	private String ipAddr;
	private String flag;
	private String result;
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}	
	
}

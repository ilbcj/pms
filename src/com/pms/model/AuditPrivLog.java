package com.pms.model;

public class AuditPrivLog {
	public final static String LOGFLAGQUERY = "查询";
	public final static String LOGFLAGADD = "添加";
	public final static String LOGFLAGUPDATE = "修改";
	public final static String LOGFLAGDELETE = "删除";
	public final static String LOGFLAGIMPORT = "导入";
	public final static String LOGFLAGEXPORT = "导出";
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

package com.pms.model;

public class Admin {
	public static final int INUSE = 1;
	public static final int FROZEN = 2;
	public static final int DELETE = 3;
	
	private int id;
	private String loginid;
	private String name;
	private String pwd;
	private String unit;
	private int status;
	private String frozentime;
	private int errorcount;
	
	public String getFrozentime() {
		return frozentime;
	}
	public void setFrozentime(String frozentime) {
		this.frozentime = frozentime;
	}
	public int getErrorcount() {
		return errorcount;
	}
	public void setErrorcount(int errorcount) {
		this.errorcount = errorcount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}

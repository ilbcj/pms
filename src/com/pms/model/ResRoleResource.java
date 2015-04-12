package com.pms.model;

public class ResRoleResource {
	public static final int RESTYPEFEATURE = 1;
	public static final int RESTYPEDATA = 2;
	private int id;
	private int roleid;
	private int resid;
	private int restype;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getResid() {
		return resid;
	}
	public void setResid(int resid) {
		this.resid = resid;
	}
	public int getRestype() {
		return restype;
	}
	public void setRestype(int restype) {
		this.restype = restype;
	}	
}

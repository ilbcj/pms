package com.pms.model;

public class AdminAccredit {
	public final static int ACCREDITORGANIZATION = 0;
	public final static int ACCREDITUSER = 1;
	public final static int ACCREDITGROUP = 2;
	public final static int ACCREDITROLE = 3;
	public final static int ACCREDITRESOURCE= 4;
	public final static int ACCREDITPRIVILEGE = 5;
	public final static int ACCREDITSYSTEM = 6;
	public final static int ACCREDITAUDITLOG = 7;
	public final static int ACCREDITADMIN = 8;
	
	private int id;
	private int aid;
	private int pid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
}

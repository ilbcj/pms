package com.pms.dto;

public class PrivUserListItem {
	public static final int PRIVSTATUSYES = 2;
	public static final int PRIVSTATUSNO = 1;
	private int id;
	private String name;
	private int status;
	private String parent_id;
	private String pname;
	private String gname;
	private int priv_status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public int getPriv_status() {
		return priv_status;
	}
	public void setPriv_status(int priv_status) {
		this.priv_status = priv_status;
	}
	
}

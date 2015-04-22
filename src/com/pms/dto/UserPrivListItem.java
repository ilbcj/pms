package com.pms.dto;

import java.util.List;

public class UserPrivListItem {
	public static final String SOURCETYPEUSER = "用户授权";
	public static final String SOURCETYPEORG = "机构授权";
	public static final String SOURCETYPEGROUP = "用户组授权";
	
	private int roleid;
	private String rolename;
	private String rolecode;
	private List<String> source;
	
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	public List<String> getSource() {
		return source;
	}
	public void setSource(List<String> source) {
		this.source = source;
	}
}

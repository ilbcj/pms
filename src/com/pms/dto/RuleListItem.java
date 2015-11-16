package com.pms.dto;


public class RuleListItem {
	private int id;
	private String groupid;
	private int attrid;
	private String rulename = "";
	private String rulevalue;
	private String rulecode;
	private int ruletype;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public int getAttrid() {
		return attrid;
	}
	public void setAttrid(int attrid) {
		this.attrid = attrid;
	}
	public String getRulename() {
		return rulename;
	}
	public void setRulename(String rulename) {
		this.rulename = rulename;
	}
	public String getRulevalue() {
		return rulevalue;
	}
	public void setRulevalue(String rulevalue) {
		this.rulevalue = rulevalue;
	}
	public String getRulecode() {
		return rulecode;
	}
	public void setRulecode(String rulecode) {
		this.rulecode = rulecode;
	}
	public int getRuletype() {
		return ruletype;
	}
	public void setRuletype(int ruletype) {
		this.ruletype = ruletype;
	}
	
	
}

package com.pms.model;

public class Rule {
	public static final int RULETYPEEQUAL = 1;
	public static final int RULETYPEINCLUDE = 2;
	private int id;
	private int groupid;
	private String rulename;
	private String rulevalue;
	private int ruletype;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
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
	public int getRuletype() {
		return ruletype;
	}
	public void setRuletype(int ruletype) {
		this.ruletype = ruletype;
	}
}

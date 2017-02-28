package com.pms.webservice.model;

import java.util.List;

public class Common010121 {
	private String table;
	private String isAsyn;
	private String alias;
	
	private Condition search;
	private List<Item> RETURNITEMS;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getIsAsyn() {
		return isAsyn;
	}
	public void setIsAsyn(String isAsyn) {
		this.isAsyn = isAsyn;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Condition getSearch() {
		return search;
	}
	public void setSearch(Condition search) {
		this.search = search;
	}
	public List<Item> getRETURNITEMS() {
		return RETURNITEMS;
	}
	public void setRETURNITEMS(List<Item> rETURNITEMS) {
		RETURNITEMS = rETURNITEMS;
	}
	

}

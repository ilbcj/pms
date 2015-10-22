package com.pms.webservice.model;

public class Common010123 {
	public final static int JOINTYPEINNER = 0;
	public final static int JOINTYPELEFT = 1;
	public final static int JOINTYPERIGHT = 2;
	private String table;
	private String alias;
	private int join;
	private Condition search;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getJoin() {
		return join;
	}
	public void setJoin(int join) {
		this.join = join;
	}
	public Condition getSearch() {
		return search;
	}
	public void setSearch(Condition search) {
		this.search = search;
	}
}

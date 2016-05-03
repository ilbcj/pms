package com.pms.webservice.model.search;

import com.pms.webservice.model.Condition;

public class Common_010123 {
	public final static int JOINTYPEINNER = 0;
	public final static int JOINTYPELEFT = 1;
	public final static int JOINTYPERIGHT = 2;
	
	private String tableNameJ010002;
	private int joinTypeJ010007;
	private Condition search;
	
	public String getTableNameJ010002() {
		return tableNameJ010002;
	}
	public void setTableNameJ010002(String tableNameJ010002) {
		this.tableNameJ010002 = tableNameJ010002;
	}
	public int getJoinTypeJ010007() {
		return joinTypeJ010007;
	}
	public void setJoinTypeJ010007(int joinTypeJ010007) {
		this.joinTypeJ010007 = joinTypeJ010007;
	}
	public Condition getSearch() {
		return search;
	}
	public void setSearch(Condition search) {
		this.search = search;
	}
}

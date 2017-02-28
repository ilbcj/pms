package com.pms.webservice.model.search;

import java.util.List;

import com.pms.webservice.model.Item;

public class Common_010121 {
	private String tableNameJ010002;
	private String tableAliasJ010015;
	//private Conditions conditions;
	private NestConditions conditions;
	private List<Item> returnColumns;
//	private List<Item> orderColumns;
//	private List<Item> groupColumns;
	private List<Common_010123> common010123;
	
	public String getTableNameJ010002() {
		return tableNameJ010002;
	}
	public void setTableNameJ010002(String tableNameJ010002) {
		this.tableNameJ010002 = tableNameJ010002;
	}
	public String getTableAliasJ010015() {
		return tableAliasJ010015;
	}
	public void setTableAliasJ010015(String tableAliasJ010015) {
		this.tableAliasJ010015 = tableAliasJ010015;
	}
	public NestConditions getConditions() {
		return conditions;
	}
	public void setConditions(NestConditions conditions) {
		this.conditions = conditions;
	}
	public List<Item> getReturnColumns() {
		return returnColumns;
	}
	public void setReturnColumns(List<Item> returnColumns) {
		this.returnColumns = returnColumns;
	}
//	public List<Item> getOrderColumns() {
//		return orderColumns;
//	}
//	public void setOrderColumns(List<Item> orderColumns) {
//		this.orderColumns = orderColumns;
//	}
//	public List<Item> getGroupColumns() {
//		return groupColumns;
//	}
//	public void setGroupColumns(List<Item> groupColumns) {
//		this.groupColumns = groupColumns;
//	}
	public List<Common_010123> getCommon010123() {
		return common010123;
	}
	public void setCommon010123(List<Common_010123> common010123) {
		this.common010123 = common010123;
	}
}

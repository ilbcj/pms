package com.pms.webservice.model.search;

import java.util.List;

import com.pms.webservice.model.Item;

public class ComplexSearchCondition {
	private String tableNameJ010002;
	private int	searchResultMaxNumI010017;
	private String searchAllowAsynI010018;
	private String asyncServiceIDJ010016;
	private String searchIDH010005;
	private int searchResultCountI010019;
	private List<Item> returnColumns;
	private List<Common_010123> common010123;
	private List<Common_010121> common010121;
	private List<Item> orderColumns;
	private List<Item> groupColumns;
	private NestConditions conditions;
	
	public List<Item> getGroupColumns() {
		return groupColumns;
	}
	public void setGroupColumns(List<Item> groupColumns) {
		this.groupColumns = groupColumns;
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
	public List<Common_010123> getCommon010123() {
		return common010123;
	}
	public void setCommon010123(List<Common_010123> common010123) {
		this.common010123 = common010123;
	}
	public List<Common_010121> getCommon010121() {
		return common010121;
	}
	public void setCommon010121(List<Common_010121> common010121) {
		this.common010121 = common010121;
	}
	public List<Item> getOrderColumns() {
		return orderColumns;
	}
	public void setOrderColumns(List<Item> orderColumns) {
		this.orderColumns = orderColumns;
	}
	public String getTableNameJ010002() {
		return tableNameJ010002;
	}
	public void setTableNameJ010002(String tableNameJ010002) {
		this.tableNameJ010002 = tableNameJ010002;
	}
	public int getSearchResultMaxNumI010017() {
		return searchResultMaxNumI010017;
	}
	public void setSearchResultMaxNumI010017(int searchResultMaxNumI010017) {
		this.searchResultMaxNumI010017 = searchResultMaxNumI010017;
	}
	public String getSearchAllowAsynI010018() {
		return searchAllowAsynI010018;
	}
	public void setSearchAllowAsynI010018(String searchAllowAsynI010018) {
		this.searchAllowAsynI010018 = searchAllowAsynI010018;
	}
	public String getAsyncServiceIDJ010016() {
		return asyncServiceIDJ010016;
	}
	public void setAsyncServiceIDJ010016(String asyncServiceIDJ010016) {
		this.asyncServiceIDJ010016 = asyncServiceIDJ010016;
	}
	public String getSearchIDH010005() {
		return searchIDH010005;
	}
	public void setSearchIDH010005(String searchIDH010005) {
		this.searchIDH010005 = searchIDH010005;
	}
	public int getSearchResultCountI010019() {
		return searchResultCountI010019;
	}
	public void setSearchResultCountI010019(int searchResultCountI010019) {
		this.searchResultCountI010019 = searchResultCountI010019;
	}
}

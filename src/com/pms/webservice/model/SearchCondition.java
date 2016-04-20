package com.pms.webservice.model;

import java.util.List;

public class SearchCondition {
	public static final int CONNECT_TYPE_NO = 0;
	public static final int CONNECT_TYPE_010117 = 1;
	public static final int CONNECT_TYPE_010121 = 2;
	private String tableName;
	private String totalNum;
	private String onceNum;
	private String isAsyn;
	private String searchId;
	private String CONDITION;
	private List<Item> CONDITIONITEMS;
	private List<Item> RETURNITEMS;
	
	private String CONDITION_START;
	private List<Item> STARTITEMS;
	private String CONDITION_CONNECT;
	private List<Item> CONNECTITEMS;
	
	private int CONNECTTYPE;
	
	//join conditions
	private List<Common010123> common010123;
	private List<Common010121> common010121;
	private String tableAlias;
	
	public List<Common010121> getCommon010121() {
		return common010121;
	}
	public void setCommon010121(List<Common010121> common010121) {
		this.common010121 = common010121;
	}
	public String getSearchId() {
		return searchId;
	}
	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public List<Common010123> getCommon010123() {
		return common010123;
	}
	public void setCommon010123(List<Common010123> common010123) {
		this.common010123 = common010123;
	}
	public int getCONNECTTYPE() {
		return CONNECTTYPE;
	}
	public void setCONNECTTYPE(int cONNECTTYPE) {
		CONNECTTYPE = cONNECTTYPE;
	}
	public String getCONDITION_START() {
		return CONDITION_START;
	}
	public void setCONDITION_START(String cONDITION_START) {
		CONDITION_START = cONDITION_START;
	}
	public List<Item> getSTARTITEMS() {
		return STARTITEMS;
	}
	public void setSTARTITEMS(List<Item> sTARTITEMS) {
		STARTITEMS = sTARTITEMS;
	}
	public String getCONDITION_CONNECT() {
		return CONDITION_CONNECT;
	}
	public void setCONDITION_CONNECT(String cONDITION_CONNECT) {
		CONDITION_CONNECT = cONDITION_CONNECT;
	}
	public List<Item> getCONNECTITEMS() {
		return CONNECTITEMS;
	}
	public void setCONNECTITEMS(List<Item> cONNECTITEMS) {
		CONNECTITEMS = cONNECTITEMS;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getOnceNum() {
		return onceNum;
	}
	public void setOnceNum(String onceNum) {
		this.onceNum = onceNum;
	}
	public String getIsAsyn() {
		return isAsyn;
	}
	public void setIsAsyn(String isAsyn) {
		this.isAsyn = isAsyn;
	}
	public String getCONDITION() {
		return CONDITION;
	}
	public void setCONDITION(String cONDITION) {
		CONDITION = cONDITION;
	}
	public List<Item> getCONDITIONITEMS() {
		return CONDITIONITEMS;
	}
	public void setCONDITIONITEMS(List<Item> cONDITIONITEMS) {
		CONDITIONITEMS = cONDITIONITEMS;
	}
	public List<Item> getRETURNITEMS() {
		return RETURNITEMS;
	}
	public void setRETURNITEMS(List<Item> rETURNITEMS) {
		RETURNITEMS = rETURNITEMS;
	}
}

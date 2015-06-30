package com.pms.webservice.model;

import java.util.List;

public class SearchCondition {
	private String tableName;
	private String totalNum;
	private String onceNum;
	private String isAsyn;
	private String CONDITION;
	private List<Condition> CONDITIONITEMS;
	private List<Condition> RETURNITEMS;
	
	private String CONDITION_START;
	private List<Condition> STARTITEMS;
	private String CONDITION_CONNECT;
	private List<Condition> CONNECTITEMS;
	
	public String getCONDITION_START() {
		return CONDITION_START;
	}
	public void setCONDITION_START(String cONDITION_START) {
		CONDITION_START = cONDITION_START;
	}
	public List<Condition> getSTARTITEMS() {
		return STARTITEMS;
	}
	public void setSTARTITEMS(List<Condition> sTARTITEMS) {
		STARTITEMS = sTARTITEMS;
	}
	public String getCONDITION_CONNECT() {
		return CONDITION_CONNECT;
	}
	public void setCONDITION_CONNECT(String cONDITION_CONNECT) {
		CONDITION_CONNECT = cONDITION_CONNECT;
	}
	public List<Condition> getCONNECTITEMS() {
		return CONNECTITEMS;
	}
	public void setCONNECTITEMS(List<Condition> cONNECTITEMS) {
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
	public List<Condition> getCONDITIONITEMS() {
		return CONDITIONITEMS;
	}
	public void setCONDITIONITEMS(List<Condition> cONDITIONITEMS) {
		CONDITIONITEMS = cONDITIONITEMS;
	}
	public List<Condition> getRETURNITEMS() {
		return RETURNITEMS;
	}
	public void setRETURNITEMS(List<Condition> rETURNITEMS) {
		RETURNITEMS = rETURNITEMS;
	}
}

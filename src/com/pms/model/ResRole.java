package com.pms.model;

public class ResRole {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	public final static int RESROLETYPEPUBLIC = 1;
	public final static int RESROLETYPELOCAL = 2;
	public final static int RESROLETYPEBILATERAL = 3;
	
	private int id;
	private String BUSINESS_ROLE;
	private int BUSINESS_ROLE_TYPE;
	private String BUSINESS_ROLE_NAME;
	private String SYSTEM_TYPE;
	private String CLUE_SRC_SYS;
	private String ROLE_DESC;
//	private String clue_dst_sys;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBUSINESS_ROLE() {
		return BUSINESS_ROLE;
	}
	public void setBUSINESS_ROLE(String bUSINESS_ROLE) {
		BUSINESS_ROLE = bUSINESS_ROLE;
	}
	public int getBUSINESS_ROLE_TYPE() {
		return BUSINESS_ROLE_TYPE;
	}
	public void setBUSINESS_ROLE_TYPE(int bUSINESS_ROLE_TYPE) {
		BUSINESS_ROLE_TYPE = bUSINESS_ROLE_TYPE;
	}
	public String getBUSINESS_ROLE_NAME() {
		return BUSINESS_ROLE_NAME;
	}
	public void setBUSINESS_ROLE_NAME(String bUSINESS_ROLE_NAME) {
		BUSINESS_ROLE_NAME = bUSINESS_ROLE_NAME;
	}
	public String getSYSTEM_TYPE() {
		return SYSTEM_TYPE;
	}
	public void setSYSTEM_TYPE(String sYSTEM_TYPE) {
		SYSTEM_TYPE = sYSTEM_TYPE;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getROLE_DESC() {
		return ROLE_DESC;
	}
	public void setROLE_DESC(String rOLE_DESC) {
		ROLE_DESC = rOLE_DESC;
	}
	public int getDELETE_STATUS() {
		return DELETE_STATUS;
	}
	public void setDELETE_STATUS(int dELETE_STATUS) {
		DELETE_STATUS = dELETE_STATUS;
	}
	public int getDATA_VERSION() {
		return DATA_VERSION;
	}
	public void setDATA_VERSION(int dATA_VERSION) {
		DATA_VERSION = dATA_VERSION;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}
}

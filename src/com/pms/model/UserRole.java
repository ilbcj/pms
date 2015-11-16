package com.pms.model;

public class UserRole {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String CERTIFICATE_CODE_MD5;
	private String BUSINESS_ROLE;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	public String getBUSINESS_ROLE() {
		return BUSINESS_ROLE;
	}
	public void setBUSINESS_ROLE(String bUSINESS_ROLE) {
		BUSINESS_ROLE = bUSINESS_ROLE;
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

package com.pms.model;

public class User {
	public final static int USERSTATUSINUSE = 0;
	public final static int USERSTATUSREVOKE = 1;
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String NAME="";//name
	private String CERTIFICATE_CODE_MD5;//idnum
	private String CERTIFICATE_CODE_SUFFIX;
	private String SEXCODE;//sex
	private String GA_DEPARTMENT;//parent_id
	private String UNIT;
	private int ORG_LEVEL;
	private String POLICE_SORT;//police_type
	private String POLICE_NO;//police_num
	private String SENSITIVE_LEVEL;//max_sensitive_level
	private String BUSINESS_TYPE;//unit
	private String TAKE_OFFICE;//title
	private int USER_STATUS;//status
	private String position;
	private String dept;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;//tstamp
	
	public int getUSER_STATUS() {
		return USER_STATUS;
	}
	public void setUSER_STATUS(int uSER_STATUS) {
		USER_STATUS = uSER_STATUS;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	public String getCERTIFICATE_CODE_SUFFIX() {
		return CERTIFICATE_CODE_SUFFIX;
	}
	public void setCERTIFICATE_CODE_SUFFIX(String cERTIFICATE_CODE_SUFFIX) {
		CERTIFICATE_CODE_SUFFIX = cERTIFICATE_CODE_SUFFIX;
	}
	public String getSEXCODE() {
		return SEXCODE;
	}
	public void setSEXCODE(String sEXCODE) {
		SEXCODE = sEXCODE;
	}
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	public int getORG_LEVEL() {
		return ORG_LEVEL;
	}
	public void setORG_LEVEL(int oRG_LEVEL) {
		ORG_LEVEL = oRG_LEVEL;
	}
	public String getPOLICE_SORT() {
		return POLICE_SORT;
	}
	public void setPOLICE_SORT(String pOLICE_SORT) {
		POLICE_SORT = pOLICE_SORT;
	}
	public String getPOLICE_NO() {
		return POLICE_NO;
	}
	public void setPOLICE_NO(String pOLICE_NO) {
		POLICE_NO = pOLICE_NO;
	}
	public String getSENSITIVE_LEVEL() {
		return SENSITIVE_LEVEL;
	}
	public void setSENSITIVE_LEVEL(String sENSITIVE_LEVEL) {
		SENSITIVE_LEVEL = sENSITIVE_LEVEL;
	}
	public String getBUSINESS_TYPE() {
		return BUSINESS_TYPE;
	}
	public void setBUSINESS_TYPE(String bUSINESS_TYPE) {
		BUSINESS_TYPE = bUSINESS_TYPE;
	}
	public String getTAKE_OFFICE() {
		return TAKE_OFFICE;
	}
	public void setTAKE_OFFICE(String tAKE_OFFICE) {
		TAKE_OFFICE = tAKE_OFFICE;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
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

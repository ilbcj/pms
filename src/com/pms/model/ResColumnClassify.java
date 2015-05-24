package com.pms.model;

public class ResColumnClassify {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String SECTION_CLASS;
	private String CLUE_SRC_SYS;
	private String CLASSIFY_NAME;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getSECTION_CLASS() {
		return SECTION_CLASS;
	}
	public void setSECTION_CLASS(String sECTION_CLASS) {
		SECTION_CLASS = sECTION_CLASS;
	}
	public String getCLASSIFY_NAME() {
		return CLASSIFY_NAME;
	}
	public void setCLASSIFY_NAME(String cLASSIFY_NAME) {
		CLASSIFY_NAME = cLASSIFY_NAME;
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

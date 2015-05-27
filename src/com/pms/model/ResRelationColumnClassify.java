package com.pms.model;

public class ResRelationColumnClassify {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String SECTION_RELATIOIN_CLASS;
	private String CLUE_SRC_SYS;
	private String SRC_CLASS_CODE;
	private String DST_CLASS_CODE;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSECTION_RELATIOIN_CLASS() {
		return SECTION_RELATIOIN_CLASS;
	}
	public void setSECTION_RELATIOIN_CLASS(String sECTION_RELATIOIN_CLASS) {
		SECTION_RELATIOIN_CLASS = sECTION_RELATIOIN_CLASS;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getSRC_CLASS_CODE() {
		return SRC_CLASS_CODE;
	}
	public void setSRC_CLASS_CODE(String sRC_CLASS_CODE) {
		SRC_CLASS_CODE = sRC_CLASS_CODE;
	}
	public String getDST_CLASS_CODE() {
		return DST_CLASS_CODE;
	}
	public void setDST_CLASS_CODE(String dST_CLASS_CODE) {
		DST_CLASS_CODE = dST_CLASS_CODE;
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
	
	public boolean isValid() {
		if( (SECTION_RELATIOIN_CLASS == null || SECTION_RELATIOIN_CLASS.isEmpty()) 
				&& (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				&& (SRC_CLASS_CODE == null || SRC_CLASS_CODE.isEmpty())
				&& (DST_CLASS_CODE == null || DST_CLASS_CODE.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

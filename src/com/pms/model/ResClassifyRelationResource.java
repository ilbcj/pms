package com.pms.model;

public class ResClassifyRelationResource {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String DATA_SET;
	private String SECTION_RELATIOIN_CLASS;
	private String SECTION_RELATIOIN_CLASS_NAME;
	private String CLUE_SRC_SYS;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDATA_SET() {
		return DATA_SET;
	}
	public void setDATA_SET(String dATA_SET) {
		DATA_SET = dATA_SET;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getSECTION_RELATIOIN_CLASS() {
		return SECTION_RELATIOIN_CLASS;
	}
	public void setSECTION_RELATIOIN_CLASS(String sECTION_RELATIOIN_CLASS) {
		SECTION_RELATIOIN_CLASS = sECTION_RELATIOIN_CLASS;
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
	public String getSECTION_RELATIOIN_CLASS_NAME() {
		return SECTION_RELATIOIN_CLASS_NAME;
	}
	public void setSECTION_RELATIOIN_CLASS_NAME(String sECTION_RELATIOIN_CLASS_NAME) {
		SECTION_RELATIOIN_CLASS_NAME = sECTION_RELATIOIN_CLASS_NAME;
	}
	
	public boolean isValid() {
		if( (DATA_SET == null || DATA_SET.isEmpty()) 
			//|| (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty())
			|| (SECTION_RELATIOIN_CLASS_NAME == null || SECTION_RELATIOIN_CLASS_NAME.isEmpty())
			|| (SECTION_RELATIOIN_CLASS == null || SECTION_RELATIOIN_CLASS.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

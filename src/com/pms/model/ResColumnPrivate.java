package com.pms.model;

public class ResColumnPrivate {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String ELEMENT;
	private String CLUE_SRC_SYS;
	private String DATA_SET;
	private String COLUMN_CN;
	private String COLUMN_NAME;
	private String COLUMN_RMK;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public ResColumnPrivate() {
	}
	public ResColumnPrivate(ResColumn col) {
		id = col.getId();
		ELEMENT = col.getELEMENT();
		CLUE_SRC_SYS = col.getCLUE_SRC_SYS();
		DATA_SET = col.getDATA_SET();
		COLUMN_CN = col.getCOLUMU_CN();
		COLUMN_NAME = col.getCOLUMN_NAME();
		COLUMN_RMK = col.getRMK();
		DELETE_STATUS = col.getDELETE_STATUS();
		DATA_VERSION = col.getDATA_VERSION();
		LATEST_MOD_TIME = col.getLATEST_MOD_TIME();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getELEMENT() {
		return ELEMENT;
	}
	public void setELEMENT(String eLEMENT) {
		ELEMENT = eLEMENT;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getDATA_SET() {
		return DATA_SET;
	}
	public void setDATA_SET(String dATA_SET) {
		DATA_SET = dATA_SET;
	}
	public String getCOLUMN_CN() {
		return COLUMN_CN;
	}
	public void setCOLUMN_CN(String cOLUMN_CN) {
		COLUMN_CN = cOLUMN_CN;
	}
	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}
	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}
	public String getCOLUMN_RMK() {
		return COLUMN_RMK;
	}
	public void setCOLUMN_RMK(String cOLUMN_RMK) {
		COLUMN_RMK = cOLUMN_RMK;
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
		if( (ELEMENT == null || ELEMENT.isEmpty()) 
				//|| (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				//|| (COLUMU_CN == null || COLUMU_CN.isEmpty())
				//|| (COLUMN_NAME == null || COLUMN_NAME.isEmpty())
				//|| (RMK == null || RMK.isEmpty())
				|| (DATA_SET == null || DATA_SET.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

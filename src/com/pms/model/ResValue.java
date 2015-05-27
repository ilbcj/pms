package com.pms.model;

public class ResValue {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String VALUE_ID;
	private String CLUE_SRC_SYS;
	private String VALUE_NAME;
	private String VALUE_SENSITIVE_ID;
	private String COLUMN_ID;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVALUE_ID() {
		return VALUE_ID;
	}
	public void setVALUE_ID(String vALUE_ID) {
		VALUE_ID = vALUE_ID;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getVALUE_NAME() {
		return VALUE_NAME;
	}
	public void setVALUE_NAME(String vALUE_NAME) {
		VALUE_NAME = vALUE_NAME;
	}
	public String getVALUE_SENSITIVE_ID() {
		return VALUE_SENSITIVE_ID;
	}
	public void setVALUE_SENSITIVE_ID(String vALUE_SENSITIVE_ID) {
		VALUE_SENSITIVE_ID = vALUE_SENSITIVE_ID;
	}
	public String getCOLUMN_ID() {
		return COLUMN_ID;
	}
	public void setCOLUMN_ID(String cOLUMN_ID) {
		COLUMN_ID = cOLUMN_ID;
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
		if( (VALUE_ID == null || VALUE_ID.isEmpty()) 
				&& (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				&& (VALUE_NAME == null || VALUE_NAME.isEmpty())
				&& (VALUE_SENSITIVE_ID == null || VALUE_SENSITIVE_ID.isEmpty())
				&& (COLUMN_ID == null || COLUMN_ID.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

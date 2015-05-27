package com.pms.model;

public class ResValueSensitive {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String VALUE_SENSITTIVE_ID;
	private String CLUE_SRC_SYS;
	private String VALUE_SENSITIVE_NAME;
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
	public String getVALUE_SENSITTIVE_ID() {
		return VALUE_SENSITTIVE_ID;
	}
	public void setVALUE_SENSITTIVE_ID(String vALUE_SENSITTIVE_ID) {
		VALUE_SENSITTIVE_ID = vALUE_SENSITTIVE_ID;
	}
	public String getVALUE_SENSITIVE_NAME() {
		return VALUE_SENSITIVE_NAME;
	}
	public void setVALUE_SENSITIVE_NAME(String vALUE_SENSITIVE_NAME) {
		VALUE_SENSITIVE_NAME = vALUE_SENSITIVE_NAME;
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
		if( (VALUE_SENSITTIVE_ID == null || VALUE_SENSITTIVE_ID.isEmpty()) 
				&& (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				&& (VALUE_SENSITIVE_NAME == null || VALUE_SENSITIVE_NAME.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

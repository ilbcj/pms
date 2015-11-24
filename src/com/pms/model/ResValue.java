package com.pms.model;

public class ResValue {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String ELEMENT_VALUE;
	private String CLUE_SRC_SYS;
	private String VALUE_NAME;
	private String VALUE_SENSITIVE_ID;
	private String ELEMENT;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getELEMENT_VALUE() {
		return ELEMENT_VALUE;
	}
	public void setELEMENT_VALUE(String eLEMENT_VALUE) {
		ELEMENT_VALUE = eLEMENT_VALUE;
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
	public String getELEMENT() {
		return ELEMENT;
	}
	public void setELEMENT(String eLEMENT) {
		ELEMENT = eLEMENT;
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
		if( (ELEMENT_VALUE == null || ELEMENT_VALUE.isEmpty()) 
				//|| (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				|| (VALUE_NAME == null || VALUE_NAME.isEmpty())
				|| (VALUE_SENSITIVE_ID == null || VALUE_SENSITIVE_ID.isEmpty())
				|| (ELEMENT == null || ELEMENT.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

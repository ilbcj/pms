package com.pms.model;

public class ResDataSetSensitive {
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String DATASET_SENSITIVE_LEVEL;
	private String CLUE_SRC_SYS;
	private String DATASET_SENSITIVE_NAME;
	private int DELETE_STATUS;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDATASET_SENSITIVE_LEVEL() {
		return DATASET_SENSITIVE_LEVEL;
	}
	public void setDATASET_SENSITIVE_LEVEL(String dATASET_SENSITIVE_LEVEL) {
		DATASET_SENSITIVE_LEVEL = dATASET_SENSITIVE_LEVEL;
	}
	public String getCLUE_SRC_SYS() {
		return CLUE_SRC_SYS;
	}
	public void setCLUE_SRC_SYS(String cLUE_SRC_SYS) {
		CLUE_SRC_SYS = cLUE_SRC_SYS;
	}
	public String getDATASET_SENSITIVE_NAME() {
		return DATASET_SENSITIVE_NAME;
	}
	public void setDATASET_SENSITIVE_NAME(String dATASET_SENSITIVE_NAME) {
		DATASET_SENSITIVE_NAME = dATASET_SENSITIVE_NAME;
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
		if( (DATASET_SENSITIVE_LEVEL == null || DATASET_SENSITIVE_LEVEL.isEmpty()) 
				&& (CLUE_SRC_SYS == null || CLUE_SRC_SYS.isEmpty()) 
				&& (DATASET_SENSITIVE_NAME == null || DATASET_SENSITIVE_NAME.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

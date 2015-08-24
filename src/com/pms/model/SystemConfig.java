package com.pms.model;

public class SystemConfig {
	public final static int SYSTEMCONFIGTYPESYNC = 1;
	
	public final static String SYSTEMCONFIG_TYPE_WA_AUTHORITY_POLICE = "1";
	public final static String SYSTEMCONFIG_TYPE_WA_AUTHORITY_ORGNIZATION = "2";
	public final static String SYSTEMCONFIG_TYPE_WA_AUTHORITY_RESOURCE = "3";
	
	public final static String SYSTEMCONFIG_ITEM_BUSINESSTYPE = "sync_businesstype";
	public final static String SYSTEMCONFIG_ITEM_DATASOURCE = "sync_datasource";
	public final static String SYSTEMCONFIG_ITEM_SN = "sync_sn";
	public final static String SYSTEMCONFIG_ITEM_EXPORTPATH = "sync_exportpath";
	public final static String SYSTEMCONFIG_ITEM_DATARES = "sync_DataRes";
	public final static String SYSTEMCONFIG_ITEM_ORG = "sync_Org";
	public final static String SYSTEMCONFIG_ITEM_USER = "sync_User";
	public final static String SYSTEMCONFIG_ITEM_Role = "sync_Role";
	public final static String SYSTEMCONFIG_ITEM_RESINROLE = "sync_ResInRole";
	
	private int id;
	private String item;
	private String value;
	private String rmk;
	private int type;
	private String LATEST_MOD_TIME;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATESTMODTIME) {
		LATEST_MOD_TIME = lATESTMODTIME;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}	
}

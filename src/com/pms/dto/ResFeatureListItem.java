package com.pms.dto;

import java.util.List;

import com.pms.model.AttrDictionary;


public class ResFeatureListItem {
	private int id;
	private String SYSTEM_TYPE;
	private String RESOURCE_ID;
	private String APP_ID;
	private String RESOUCE_NAME;
	private String PARENT_RESOURCE;
	private String pname;
	private String URL;
	private String RESOURCE_ICON_PATH;
	private int RESOURCE_STATUS;
	private String RESOURCE_ORDER;
	private String RESOURCE_DESCRIBE;
	private String RMK;
	private int FUN_RESOURCE_TYPE;
	private int DATA_VERSION;
	private int DELETE_STATUS;
	private String LATEST_MOD_TIME;
	
	private List<AttrDictionary> dictionary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSYSTEM_TYPE() {
		return SYSTEM_TYPE;
	}

	public void setSYSTEM_TYPE(String sYSTEM_TYPE) {
		SYSTEM_TYPE = sYSTEM_TYPE;
	}

	public String getRESOURCE_ID() {
		return RESOURCE_ID;
	}

	public void setRESOURCE_ID(String rESOURCE_ID) {
		RESOURCE_ID = rESOURCE_ID;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String aPP_ID) {
		APP_ID = aPP_ID;
	}

	public String getRESOUCE_NAME() {
		return RESOUCE_NAME;
	}

	public void setRESOUCE_NAME(String rESOUCE_NAME) {
		RESOUCE_NAME = rESOUCE_NAME;
	}

	public String getPARENT_RESOURCE() {
		return PARENT_RESOURCE;
	}

	public void setPARENT_RESOURCE(String pARENT_RESOURCE) {
		PARENT_RESOURCE = pARENT_RESOURCE;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getRESOURCE_ICON_PATH() {
		return RESOURCE_ICON_PATH;
	}

	public void setRESOURCE_ICON_PATH(String rESOURCE_ICON_PATH) {
		RESOURCE_ICON_PATH = rESOURCE_ICON_PATH;
	}

	public int getRESOURCE_STATUS() {
		return RESOURCE_STATUS;
	}

	public void setRESOURCE_STATUS(int rESOURCE_STATUS) {
		RESOURCE_STATUS = rESOURCE_STATUS;
	}

	public String getRESOURCE_ORDER() {
		return RESOURCE_ORDER;
	}

	public void setRESOURCE_ORDER(String rESOURCE_ORDER) {
		RESOURCE_ORDER = rESOURCE_ORDER;
	}

	public String getRESOURCE_DESCRIBE() {
		return RESOURCE_DESCRIBE;
	}

	public void setRESOURCE_DESCRIBE(String rESOURCE_DESCRIBE) {
		RESOURCE_DESCRIBE = rESOURCE_DESCRIBE;
	}

	public String getRMK() {
		return RMK;
	}

	public void setRMK(String rMK) {
		RMK = rMK;
	}

	public int getFUN_RESOURCE_TYPE() {
		return FUN_RESOURCE_TYPE;
	}

	public void setFUN_RESOURCE_TYPE(int fUN_RESOURCE_TYPE) {
		FUN_RESOURCE_TYPE = fUN_RESOURCE_TYPE;
	}

	public int getDATA_VERSION() {
		return DATA_VERSION;
	}

	public void setDATA_VERSION(int dATA_VERSION) {
		DATA_VERSION = dATA_VERSION;
	}

	public int getDELETE_STATUS() {
		return DELETE_STATUS;
	}

	public void setDELETE_STATUS(int dELETE_STATUS) {
		DELETE_STATUS = dELETE_STATUS;
	}

	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}

	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}

	public List<AttrDictionary> getDictionary() {
		return dictionary;
	}

	public void setDictionary(List<AttrDictionary> dictionary) {
		this.dictionary = dictionary;
	}


}

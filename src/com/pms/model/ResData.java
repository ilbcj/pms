package com.pms.model;

public class ResData {
	public final static int RESTYPEPUBLIC = 1;
	public final static int RESTYPELOCAL = 2;
	public final static int RESTYPEBILATERAL = 3;
	public final static int RESSTATUSENABLE = 1;
	public final static int RESSTATUSDISABLE = 0;
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
	private int id;
	private String name;
	private int resource_type;
	private String RESOURCE_ID;//resource_id;
	private int RESOURCE_STATUS;//resource_status;
	private String RESOURCE_DESCRIBE;//resource_describe;
	private String DATASET_SENSITIVE_LEVEL;//dataset_sensitive_level;
	private String DATA_SET;//data_set;
	private String SECTION_CLASS;//section_class;
	private String ELEMENT;//element;
	private String SECTION_RELATIOIN_CLASS;//section_relation_class;
	private String OPERATE_SYMBOL;
	private String ELEMENT_VALUE;
	private int DELETE_STATUS;//delete_status;
	private int DATA_VERSION;
	private String LATEST_MOD_TIME;//latest_mod_time;
	private String RESOURCE_REMARK;//resource_remark;

//	private String clue_dst_sys;
//	private String row_constraint;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getResource_type() {
		return resource_type;
	}
	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}
	public String getRESOURCE_ID() {
		return RESOURCE_ID;
	}
	public void setRESOURCE_ID(String rESOURCE_ID) {
		RESOURCE_ID = rESOURCE_ID;
	}
	public int getRESOURCE_STATUS() {
		return RESOURCE_STATUS;
	}
	public void setRESOURCE_STATUS(int rESOURCE_STATUS) {
		RESOURCE_STATUS = rESOURCE_STATUS;
	}
	public String getRESOURCE_DESCRIBE() {
		return RESOURCE_DESCRIBE;
	}
	public void setRESOURCE_DESCRIBE(String rESOURCE_DESCRIBE) {
		RESOURCE_DESCRIBE = rESOURCE_DESCRIBE;
	}
	public String getDATASET_SENSITIVE_LEVEL() {
		return DATASET_SENSITIVE_LEVEL;
	}
	public void setDATASET_SENSITIVE_LEVEL(String dATASET_SENSITIVE_LEVEL) {
		DATASET_SENSITIVE_LEVEL = dATASET_SENSITIVE_LEVEL;
	}
	public String getDATA_SET() {
		return DATA_SET;
	}
	public void setDATA_SET(String dATA_SET) {
		DATA_SET = dATA_SET;
	}
	public String getSECTION_CLASS() {
		return SECTION_CLASS;
	}
	public void setSECTION_CLASS(String sECTION_CLASS) {
		SECTION_CLASS = sECTION_CLASS;
	}
	public String getELEMENT() {
		return ELEMENT;
	}
	public void setELEMENT(String eLEMENT) {
		ELEMENT = eLEMENT;
	}
	public String getSECTION_RELATIOIN_CLASS() {
		return SECTION_RELATIOIN_CLASS;
	}
	public void setSECTION_RELATIOIN_CLASS(String sECTION_RELATIOIN_CLASS) {
		SECTION_RELATIOIN_CLASS = sECTION_RELATIOIN_CLASS;
	}
	public String getOPERATE_SYMBOL() {
		return OPERATE_SYMBOL;
	}
	public void setOPERATE_SYMBOL(String oPERATE_SYMBOL) {
		OPERATE_SYMBOL = oPERATE_SYMBOL;
	}
	public String getELEMENT_VALUE() {
		return ELEMENT_VALUE;
	}
	public void setELEMENT_VALUE(String eLEMENT_VALUE) {
		ELEMENT_VALUE = eLEMENT_VALUE;
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
	public String getRESOURCE_REMARK() {
		return RESOURCE_REMARK;
	}
	public void setRESOURCE_REMARK(String rESOURCE_REMARK) {
		RESOURCE_REMARK = rESOURCE_REMARK;
	}
}

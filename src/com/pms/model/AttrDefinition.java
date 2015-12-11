package com.pms.model;

public class AttrDefinition {
	public final static int ATTRTYPEUSER = 1;
	public final static int ATTRTYPERESOURCEDATA = 2;
	public final static int ATTRTYPEORG = 3;
	public final static int ATTRTYPEROLE = 4;
	public final static int ATTRTYPEFUNCDATA = 5;
	
//	public final static String ATTR_RESOURCEDATA_RESOURCE_TYPE = "资源类型";
//	public final static String ATTR_RESOURCEDATA_RESOURCE_STATUS = "资源状态";
//	public final static String ATTR_RESOURCEDATA_DELETE_STATUS = "删除状态";
//	public final static String ATTR_RESOURCEDATA_DATASET_SENSITIVE_LEVEL = "数据集敏感度编码";
//	public final static String ATTR_RESOURCEDATA_DATA_SET = "数据集编码";
//	public final static String ATTR_RESOURCEDATA_SECTION_CLASS = "字段分类编码";
//	public final static String ATTR_RESOURCEDATA_ELEMENT = "字段编码";
//	public final static String ATTR_RESOURCEDATA_SECTION_RELATION_CLASS = "字段分类关系编码";
	
	public final static String ATTR_RESOURCEDATA_RESOURCE_TYPE_CODE = "resource_type";
	public final static String ATTR_RESOURCEDATA_RESOURCE_STATUS_CODE = "RESOURCE_STATUS";
	public final static String ATTR_RESOURCEDATA_DELETE_STATUS_CODE = "DELETE_STATUS";
	public final static String ATTR_RESOURCEDATA_DATASET_SENSITIVE_LEVEL_CODE = "DATASET_SENSITIVE_LEVEL";
	public final static String ATTR_RESOURCEDATA_DATA_SET_CODE = "DATA_SET";
	public final static String ATTR_RESOURCEDATA_SECTION_CLASS_CODE = "SECTION_CLASS";
	public final static String ATTR_RESOURCEDATA_ELEMENT_CODE = "ELEMENT";
	public final static String ATTR_RESOURCEDATA_SECTION_RELATION_CLASS_CODE = "SECTION_RELATIOIN_CLASS";

	private int id;
	private String name="";
	private String code;
	private int type;
	private String tstamp;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	
}

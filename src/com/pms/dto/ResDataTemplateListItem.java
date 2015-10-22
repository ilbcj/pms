package com.pms.dto;

import java.util.List;

import com.pms.model.AttrDictionary;


public class ResDataTemplateListItem {
	private int id;
	private String name;
	private String RESOURCE_ID;
	private String RESOURCE_STATUS;
	private String RESOURCE_DESCRIBE;
	private String RESOURCE_REMARK;
	private String DELETE_STATUS;
	private String resource_type;
	private String CLUE_DST_SYS;
	private String DATASET_SENSITIVE_LEVEL;
	private String DATA_SET;
	private String SECTION_CLASS;
	private String ELEMENT;
	private String SECTION_RELATIOIN_CLASS;
	private int DATA_VERSION;
	private String pid;
	private String pname;
	private int dest_data_version;
	
	private List<AttrDictionary> dictionary;

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

	public String getRESOURCE_ID() {
		return RESOURCE_ID;
	}

	public void setRESOURCE_ID(String rESOURCEID) {
		RESOURCE_ID = rESOURCEID;
	}

	public String getRESOURCE_STATUS() {
		return RESOURCE_STATUS;
	}

	public void setRESOURCE_STATUS(String rESOURCESTATUS) {
		RESOURCE_STATUS = rESOURCESTATUS;
	}

	public String getRESOURCE_DESCRIBE() {
		return RESOURCE_DESCRIBE;
	}

	public void setRESOURCE_DESCRIBE(String rESOURCEDESCRIBE) {
		RESOURCE_DESCRIBE = rESOURCEDESCRIBE;
	}

	public String getRESOURCE_REMARK() {
		return RESOURCE_REMARK;
	}

	public void setRESOURCE_REMARK(String rESOURCEREMARK) {
		RESOURCE_REMARK = rESOURCEREMARK;
	}

	public String getDELETE_STATUS() {
		return DELETE_STATUS;
	}

	public void setDELETE_STATUS(String dELETESTATUS) {
		DELETE_STATUS = dELETESTATUS;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resourceType) {
		resource_type = resourceType;
	}

	public String getCLUE_DST_SYS() {
		return CLUE_DST_SYS;
	}

	public void setCLUE_DST_SYS(String cLUEDSTSYS) {
		CLUE_DST_SYS = cLUEDSTSYS;
	}

	public String getDATASET_SENSITIVE_LEVEL() {
		return DATASET_SENSITIVE_LEVEL;
	}

	public void setDATASET_SENSITIVE_LEVEL(String dATASETSENSITIVELEVEL) {
		DATASET_SENSITIVE_LEVEL = dATASETSENSITIVELEVEL;
	}

	public String getDATA_SET() {
		return DATA_SET;
	}

	public void setDATA_SET(String dATASET) {
		DATA_SET = dATASET;
	}

	public String getSECTION_CLASS() {
		return SECTION_CLASS;
	}

	public void setSECTION_CLASS(String sECTIONCLASS) {
		SECTION_CLASS = sECTIONCLASS;
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

	public void setSECTION_RELATIOIN_CLASS(String sECTIONRELATIOINCLASS) {
		SECTION_RELATIOIN_CLASS = sECTIONRELATIOINCLASS;
	}

	public int getDATA_VERSION() {
		return DATA_VERSION;
	}

	public void setDATA_VERSION(int dATAVERSION) {
		DATA_VERSION = dATAVERSION;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getDest_data_version() {
		return dest_data_version;
	}

	public void setDest_data_version(int dest_data_version) {
		this.dest_data_version = dest_data_version;
	}

	public List<AttrDictionary> getDictionary() {
		return dictionary;
	}

	public void setDictionary(List<AttrDictionary> dictionary) {
		this.dictionary = dictionary;
	}
	
}

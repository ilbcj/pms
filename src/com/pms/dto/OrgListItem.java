package com.pms.dto;

import java.util.List;

import com.pms.model.AttrDictionary;
import com.pms.model.Organization;

public class OrgListItem {
	private String id;
	private String name;
	//private String uid;
	private String pid;
	private String pname;
	private String orgLevel;
	private String orgParent_org;
	private int data_version;
	
	private List<AttrDictionary> dictionary;
	
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public String getUid() {
//		return uid;
//	}
//	public void setUid(String uid) {
//		this.uid = uid;
//	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public void setOrgLevel(int orgLevel) {
		switch(orgLevel) {
			case Organization.ORG_LEVEL_MINISTRY:
				this.orgLevel = "部";
				break;
			case Organization.ORG_LEVEL_PROVINCE:
				this.orgLevel = "省";
				break;
			case Organization.ORG_LEVEL_CITY:
				this.orgLevel = "市";
				break;
			case Organization.ORG_LEVEL_COUNTY:
				this.orgLevel = "县";
				break;
			case Organization.ORG_LEVEL_GRASSROOTS:
				this.orgLevel = "基层所队";
				break;
			default:
				this.orgLevel = "未知";
				break;
		}
		return;
	}
	public String getOrgParent_org() {
		return orgParent_org;
	}
	public void setOrgParent_org(String orgParent_org) {
		this.orgParent_org = orgParent_org;
	}
	public int getData_version() {
		return data_version;
	}
	public void setData_version(int dataVersion) {
		data_version = dataVersion;
	}
	public List<AttrDictionary> getDictionary() {
		return dictionary;
	}
	public void setDictionary(List<AttrDictionary> dictionary) {
		this.dictionary = dictionary;
	}
	
}

package com.pms.dto;

import java.util.List;

import com.pms.model.AttrDictionary;
//import com.pms.model.Organization;

public class UserListItem {
	private int id;
	private String name;
	private int status;
	private String parent_id;
	private String pname;
	private String gname;
	private String unit;
	private int orgLevel;
	private String police_type;
	private String sex;
	private String idnum;
	private String max_sensitive_level;
	private String position;
	private String dept;
	private String title;
	private String police_num;
	private String business_type;
	private int data_version;
	private String certificate_code_md5;
	
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

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(int orgLevel) {
		this.orgLevel = orgLevel;
	}
//	public void setOrgLevel(int orgLevel) {
//		switch(orgLevel) {
//			case Organization.ORG_LEVEL_MINISTRY:
//				this.orgLevel = "部";
//				break;
//			case Organization.ORG_LEVEL_PROVINCE:
//				this.orgLevel = "省";
//				break;
//			case Organization.ORG_LEVEL_CITY:
//				this.orgLevel = "市";
//				break;
//			case Organization.ORG_LEVEL_COUNTY:
//				this.orgLevel = "县";
//				break;
//			case Organization.ORG_LEVEL_GRASSROOTS:
//				this.orgLevel = "基层所队";
//				break;
//			default:
//				this.orgLevel = "未知";
//				break;
//		}
//		return;
//	}
	public String getPolice_type() {
		return police_type;
	}
	public void setPolice_type(String police_type) {
		this.police_type = police_type;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getMax_sensitive_level() {
		return max_sensitive_level;
	}
	public void setMax_sensitive_level(String max_sensitive_level) {
		this.max_sensitive_level = max_sensitive_level;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPolice_num() {
		return police_num;
	}
	public void setPolice_num(String police_num) {
		this.police_num = police_num;
	}
	public String getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}
	public int getData_version() {
		return data_version;
	}
	public void setData_version(int dataVersion) {
		data_version = dataVersion;
	}
	public String getCertificate_code_md5() {
		return certificate_code_md5;
	}
	public void setCertificate_code_md5(String certificateCodeMd5) {
		certificate_code_md5 = certificateCodeMd5;
	}
	public List<AttrDictionary> getDictionary() {
		return dictionary;
	}
	public void setDictionary(List<AttrDictionary> dictionary) {
		this.dictionary = dictionary;
	}
	
}

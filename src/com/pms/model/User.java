package com.pms.model;

public class User {
	public final static int STATUSNONE = 0;
	public final static int STATUSINUSE = 1;
	public final static int STATUSHANGUP = 2;
	
	private int id;
	private String name="";
	private int status;
	private String parent_id;
	private String unit;
	private String police_type;
	private String sex;
	private String idnum;
	private String max_sensitive_level;
	private String position;
	private String dept;
	private String title;
	private String police_num;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
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
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	
	
}

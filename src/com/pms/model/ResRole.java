package com.pms.model;

public class ResRole {
	private int id;
	private String business_role_name="";
	private String business_role;
	private int business_role_type;
	private String role_describe;
	private String clue_dst_sys;
	private String system_type;
	private String clue_src_sys;
	private int delete_status;
	private String latest_mod_time;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBusiness_role_name() {
		return business_role_name;
	}
	public void setBusiness_role_name(String business_role_name) {
		this.business_role_name = business_role_name;
	}
	public String getBusiness_role() {
		return business_role;
	}
	public void setBusiness_role(String business_role) {
		this.business_role = business_role;
	}
	public int getBusiness_role_type() {
		return business_role_type;
	}
	public void setBusiness_role_type(int business_role_type) {
		this.business_role_type = business_role_type;
	}
	public String getRole_describe() {
		return role_describe;
	}
	public void setRole_describe(String role_describe) {
		this.role_describe = role_describe;
	}
	public String getClue_dst_sys() {
		return clue_dst_sys;
	}
	public void setClue_dst_sys(String clue_dst_sys) {
		this.clue_dst_sys = clue_dst_sys;
	}
	public String getSystem_type() {
		return system_type;
	}
	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}
	public String getClue_src_sys() {
		return clue_src_sys;
	}
	public void setClue_src_sys(String clue_src_sys) {
		this.clue_src_sys = clue_src_sys;
	}
	public int getDelete_status() {
		return delete_status;
	}
	public void setDelete_status(int delete_status) {
		this.delete_status = delete_status;
	}
	public String getLatest_mod_time() {
		return latest_mod_time;
	}
	public void setLatest_mod_time(String latest_mod_time) {
		this.latest_mod_time = latest_mod_time;
	}
}

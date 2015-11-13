package com.pms.model;

public class UserRoleView {
	private String id;
	private String CERTIFICATE_CODE_MD5;
	private String role_id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	
}

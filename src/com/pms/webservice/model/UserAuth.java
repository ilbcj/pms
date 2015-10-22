package com.pms.webservice.model;

public class UserAuth {
	private String GA_DEPARTMENT;
	private String CERTIFICATE_CODE_MD5;
	private String ROLE_ID;
	private String ROLE_TYPE;
	private String COOKIES;
	
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_TYPE() {
		return ROLE_TYPE;
	}
	public void setROLE_TYPE(String rOLE_TYPE) {
		ROLE_TYPE = rOLE_TYPE;
	}
	public String getCOOKIES() {
		return COOKIES;
	}
	public void setCOOKIES(String cOOKIES) {
		COOKIES = cOOKIES;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	
}

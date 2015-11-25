package com.pms.webservice.model;

public class MessageReturnStatusInfo {
	private String BUSINESS_STATUS;//I030003
	private String BUSINESS_TIME;//I010015
	private String BUSINESS_ERRCODE;//I030010
	private String MEMO;//I010009
	
	public String getBUSINESS_STATUS() {
		return BUSINESS_STATUS;
	}
	public void setBUSINESS_STATUS(String bUSINESS_STATUS) {
		BUSINESS_STATUS = bUSINESS_STATUS;
	}
	public String getBUSINESS_TIME() {
		return BUSINESS_TIME;
	}
	public void setBUSINESS_TIME(String bUSINESS_TIME) {
		BUSINESS_TIME = bUSINESS_TIME;
	}
	public String getBUSINESS_ERRCODE() {
		return BUSINESS_ERRCODE;
	}
	public void setBUSINESS_ERRCODE(String bUSINESS_ERRCODE) {
		BUSINESS_ERRCODE = bUSINESS_ERRCODE;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
}

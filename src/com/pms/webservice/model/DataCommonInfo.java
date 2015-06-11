package com.pms.webservice.model;

public class DataCommonInfo {
	private String FROM;
	private String TO;
	private String MESSAGE_SEQUENCE;
	private String MESSAGE_TYPE;
	private String BUSINESS_SERVER_TYPE;
	public String getFROM() {
		return FROM;
	}
	public void setFROM(String fROM) {
		FROM = fROM;
	}
	public String getTO() {
		return TO;
	}
	public void setTO(String tO) {
		TO = tO;
	}
	public String getMESSAGE_SEQUENCE() {
		return MESSAGE_SEQUENCE;
	}
	public void setMESSAGE_SEQUENCE(String mESSAGE_SEQUENCE) {
		MESSAGE_SEQUENCE = mESSAGE_SEQUENCE;
	}
	public String getMESSAGE_TYPE() {
		return MESSAGE_TYPE;
	}
	public void setMESSAGE_TYPE(String mESSAGE_TYPE) {
		MESSAGE_TYPE = mESSAGE_TYPE;
	}
	public String getBUSINESS_SERVER_TYPE() {
		return BUSINESS_SERVER_TYPE;
	}
	public void setBUSINESS_SERVER_TYPE(String bUSINESS_SERVER_TYPE) {
		BUSINESS_SERVER_TYPE = bUSINESS_SERVER_TYPE;
	}
}

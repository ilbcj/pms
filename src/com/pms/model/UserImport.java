package com.pms.model;

public class UserImport {

	private String NAME;//name
	private String CERTIFICATE_CODE_MD5;//idnum
	private String CERTIFICATE_CODE_SUFFIX;
	private String SEXCODE;//sex
	private String GA_DEPARTMENT;//parent_id
	private String POLICE_SORT;//police_type
	private String TAKE_OFFICE;//title
	private String position;
	private String officelevel;

	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getCERTIFICATE_CODE_MD5() {
		return CERTIFICATE_CODE_MD5;
	}
	public void setCERTIFICATE_CODE_MD5(String cERTIFICATE_CODE_MD5) {
		CERTIFICATE_CODE_MD5 = cERTIFICATE_CODE_MD5;
	}
	public String getCERTIFICATE_CODE_SUFFIX() {
		return CERTIFICATE_CODE_SUFFIX;
	}
	public void setCERTIFICATE_CODE_SUFFIX(String cERTIFICATE_CODE_SUFFIX) {
		CERTIFICATE_CODE_SUFFIX = cERTIFICATE_CODE_SUFFIX;
	}
	public String getSEXCODE() {
		return SEXCODE;
	}
	public void setSEXCODE(String sEXCODE) {
		SEXCODE = sEXCODE;
	}
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getPOLICE_SORT() {
		return POLICE_SORT;
	}
	public void setPOLICE_SORT(String pOLICE_SORT) {
		POLICE_SORT = pOLICE_SORT;
	}
	public String getTAKE_OFFICE() {
		return TAKE_OFFICE;
	}
	public void setTAKE_OFFICE(String tAKE_OFFICE) {
		TAKE_OFFICE = tAKE_OFFICE;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOfficelevel() {
		return officelevel;
	}
	public void setOfficelevel(String officelevel) {
		this.officelevel = officelevel;
	}
	public boolean isValid() {
		if( (GA_DEPARTMENT == null || GA_DEPARTMENT.isEmpty()) 
				|| (NAME == null || NAME.isEmpty()) 
				|| (CERTIFICATE_CODE_SUFFIX == null || CERTIFICATE_CODE_SUFFIX.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

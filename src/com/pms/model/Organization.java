package com.pms.model;

public class Organization {
	public final static String ROOTNODEID = "010000000000";
	public final static int ORG_LEVEL_MINISTRY = 1;//"部";
	public final static int ORG_LEVEL_PROVINCE = 2;//"省";
	public final static int ORG_LEVEL_CITY = 3;//"市";
	public final static int ORG_LEVEL_COUNTY = 4;//"县";
	public final static int ORG_LEVEL_GRASSROOTS = 9;//"基层所队";
	public final static int DELSTATUSNO = 0;
	public final static int DELSTATUSYES = 1;
	
//	private int id;
	private String GA_DEPARTMENT;//id
	private String UNIT;//name
	private int ORG_LEVEL;//org_level
	private String PARENT_ORG;//parent_id
	private int DELETE_STATUS;
	private int DATA_VERSION;
//	private String description;//description
	private String LATEST_MOD_TIME;//tstamp
	public String getGA_DEPARTMENT() {
		return GA_DEPARTMENT;
	}
	public void setGA_DEPARTMENT(String gA_DEPARTMENT) {
		GA_DEPARTMENT = gA_DEPARTMENT;
	}
	public String getUNIT() {
		return UNIT;
	}
	public void setUNIT(String uNIT) {
		UNIT = uNIT;
	}
	public int getORG_LEVEL() {
		return ORG_LEVEL;
	}
	public void setORG_LEVEL(int oRG_LEVEL) {
		ORG_LEVEL = oRG_LEVEL;
	}
	public String getPARENT_ORG() {
		return PARENT_ORG;
	}
	public void setPARENT_ORG(String pARENT_ORG) {
		PARENT_ORG = pARENT_ORG;
	}
	public int getDELETE_STATUS() {
		return DELETE_STATUS;
	}
	public void setDELETE_STATUS(int dELETE_STATUS) {
		DELETE_STATUS = dELETE_STATUS;
	}
	public int getDATA_VERSION() {
		return DATA_VERSION;
	}
	public void setDATA_VERSION(int dATA_VERSION) {
		DATA_VERSION = dATA_VERSION;
	}
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}
	
	public int queryOrgLevel(String level) {
		int result = 0;
		if("部".equals(level)) {
			result = ORG_LEVEL_MINISTRY;
		}
		else if("省".equals(level)) {
			result = ORG_LEVEL_PROVINCE;
		}
		else if("市".equals(level)) {
			result = ORG_LEVEL_CITY;
		}
		else if("县".equals(level)) {
			result = ORG_LEVEL_COUNTY;
		}
		else if("基层所队".equals(level)) {
			result = ORG_LEVEL_GRASSROOTS;
		}
		return result;
	}
	
}

package com.pms.model;

public class OrganizationImport {
	private final static String GABID = "010000000000";
	private final static int ORG_LEVEL_BU = 1;
	private final static int ORG_LEVEL_SHENG = 2;
	private final static int ORG_LEVEL_SHI = 3;
	private final static int ORG_LEVEL_XIAN = 4;
	private final static int ORG_LEVEL_JICENGSUODUI = 9;
	
	private String GA_DEPARTMENT;//id
	private String UNIT;//name
	private int ORG_LEVEL;//org_level
	private String PARENT_ORG;//parent_id

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
	
	public void MakeOrgLevel() {
		if( GABID.equals(GA_DEPARTMENT) ) {
			ORG_LEVEL = ORG_LEVEL_BU;
		} 
		else if (Long.parseLong(GA_DEPARTMENT.substring(6)) > 0) {
			ORG_LEVEL = ORG_LEVEL_JICENGSUODUI;
		}
		else if (Long.parseLong(GA_DEPARTMENT.substring(4)) > 0) {
			ORG_LEVEL = ORG_LEVEL_XIAN;
		}
		else if (Long.parseLong(GA_DEPARTMENT.substring(2)) > 0) {
			ORG_LEVEL = ORG_LEVEL_SHI;
		}
		else {
			ORG_LEVEL = ORG_LEVEL_SHENG;
		}

		return;
	}
	
	public boolean isValid() {
		if( (GA_DEPARTMENT == null || GA_DEPARTMENT.isEmpty()) 
				&& (UNIT == null || UNIT.isEmpty()) 
				&& (PARENT_ORG == null || PARENT_ORG.isEmpty()) ) {
			return false;
		}
		return true;
	}
}

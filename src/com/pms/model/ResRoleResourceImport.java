package com.pms.model;

public class ResRoleResourceImport {
	private int id;
	private String roleName;
	private String roleId;
	private String element;
	private String elemnetValue;
	private String dataSet;
	private String sectionClass;
	private String LATEST_MOD_TIME;
	
	public String getLATEST_MOD_TIME() {
		return LATEST_MOD_TIME;
	}
	public void setLATEST_MOD_TIME(String lATEST_MOD_TIME) {
		LATEST_MOD_TIME = lATEST_MOD_TIME;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getElemnetValue() {
		return elemnetValue;
	}
	public void setElemnetValue(String elemnetValue) {
		this.elemnetValue = elemnetValue;
	}
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public String getSectionClass() {
		return sectionClass;
	}
	public void setSectionClass(String sectionClass) {
		this.sectionClass = sectionClass;
	}
	public boolean isValid() {
		if( (roleId == null || roleId.isEmpty()) 
				&& (element == null || element.isEmpty()) 
				&& (elemnetValue == null || elemnetValue.isEmpty()) 
				&& (dataSet == null || dataSet.isEmpty()) 
				&& (sectionClass == null || sectionClass.isEmpty()) ) {
			return false;
		}
		try{
			Integer.parseInt(sectionClass);
		}
		catch( NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}

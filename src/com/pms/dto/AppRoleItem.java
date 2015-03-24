package com.pms.dto;

import java.util.List;

public class AppRoleItem {
	private int appId;
	private String appName;
	private String appFlag;
	private List<Integer> appRoleId;
	private List<String> appRoleName;
	private String appEndTime;
	private int appEndTimeType;
	private int appEndTimeTypeSelfNum;
	private int appEndTimeTypeSelfType;
	
	public String getAppEndTime() {
		return appEndTime;
	}
	public void setAppEndTime(String appEndTime) {
		this.appEndTime = appEndTime;
	}
	public int getAppEndTimeType() {
		return appEndTimeType;
	}
	public void setAppEndTimeType(int appEndTimeType) {
		this.appEndTimeType = appEndTimeType;
	}
	public int getAppEndTimeTypeSelfNum() {
		return appEndTimeTypeSelfNum;
	}
	public void setAppEndTimeTypeSelfNum(int appEndTimeTypeSelfNum) {
		this.appEndTimeTypeSelfNum = appEndTimeTypeSelfNum;
	}
	public int getAppEndTimeTypeSelfType() {
		return appEndTimeTypeSelfType;
	}
	public void setAppEndTimeTypeSelfType(int appEndTimeTypeSelfType) {
		this.appEndTimeTypeSelfType = appEndTimeTypeSelfType;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getAppFlag() {
		return appFlag;
	}
	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}
	public List<Integer> getAppRoleId() {
		return appRoleId;
	}
	public void setAppRoleId(List<Integer> appRoleId) {
		this.appRoleId = appRoleId;
	}
	public List<String> getAppRoleName() {
		return appRoleName;
	}
	public void setAppRoleName(List<String> appRoleName) {
		this.appRoleName = appRoleName;
	}
}

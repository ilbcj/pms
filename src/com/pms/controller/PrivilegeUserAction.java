package com.pms.controller;

import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.PrivUserListItem;
import com.pms.model.User;
import com.pms.service.UserManageService;

public class PrivilegeUserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3235199232700619L;
	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private ArrayList<PrivUserListItem> items;
	private boolean queryAll;
	private int id;
	private String userName;
	private int userStatus;
	private int userPrivStatus;
	private String userUnit;
	private String userPoliceType;
	private String userSex;
	private String userIdnum;
	private String userMaxSensitiveLevel;
	private String userPosition;
	private String userDept;
	private String userTitle;
	private String userPoliceNum;
	
	
	public int getUserPrivStatus() {
		return userPrivStatus;
	}

	public void setUserPrivStatus(int userPrivStatus) {
		this.userPrivStatus = userPrivStatus;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<PrivUserListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<PrivUserListItem> items) {
		this.items = items;
	}

	public boolean isQueryAll() {
		return queryAll;
	}

	public void setQueryAll(boolean queryAll) {
		this.queryAll = queryAll;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserUnit() {
		return userUnit;
	}

	public void setUserUnit(String userUnit) {
		this.userUnit = userUnit;
	}

	public String getUserPoliceType() {
		return userPoliceType;
	}

	public void setUserPoliceType(String userPoliceType) {
		this.userPoliceType = userPoliceType;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserIdnum() {
		return userIdnum;
	}

	public void setUserIdnum(String userIdnum) {
		this.userIdnum = userIdnum;
	}

	public String getUserMaxSensitiveLevel() {
		return userMaxSensitiveLevel;
	}

	public void setUserMaxSensitiveLevel(String userMaxSensitiveLevel) {
		this.userMaxSensitiveLevel = userMaxSensitiveLevel;
	}

	public String getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(String userPosition) {
		this.userPosition = userPosition;
	}

	public String getUserDept() {
		return userDept;
	}

	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}

	public String getUserTitle() {
		return userTitle;
	}

	public void setUserTitle(String userTitle) {
		this.userTitle = userTitle;
	}

	public String getUserPoliceNum() {
		return userPoliceNum;
	}

	public void setUserPoliceNum(String userPoliceNum) {
		this.userPoliceNum = userPoliceNum;
	}
	
	public String QueryPrivlegeUserItems()
	{
		UserManageService ums = new UserManageService();
		items = new ArrayList<PrivUserListItem>();
		try {
			if( queryAll ) {
				User criteria = new User();
				criteria.setName(userName);
				criteria.setStatus(userStatus);
				criteria.setUnit(userUnit);
				criteria.setPolice_type(userPoliceType);
				criteria.setSex(userSex);
				criteria.setIdnum(userIdnum);
				criteria.setMax_sensitive_level(userMaxSensitiveLevel);
				criteria.setPosition(userPosition);
				criteria.setDept(userDept);
				criteria.setTitle(userTitle);
				criteria.setPolice_num(userPoliceNum);
				total = ums.QueryAllPrivUserItems( id, userPrivStatus, criteria, page, rows, items );
			} else {
				total = ums.QueryPrivUserItems( id, userPrivStatus, page, rows, items );
			}
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

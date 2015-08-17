package com.pms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.UserListItem;
import com.pms.model.User;
import com.pms.service.UserManageService;
import com.pms.service.UserUploadService;

public class UserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2772707452395605429L;
	private boolean result;
	private String message;
	private boolean queryAll;
	private int page;
	private int rows;
	private int total;
	
	private User user;
	private ArrayList<UserListItem> items;
	
	private String id;//org_id
	private String userName;
	private int userStatus;
	private String userUnit;
	private String userPoliceType;
	private String userSex;
	private String userIdnum;
	private String userMaxSensitiveLevel;
	private String userPosition;
	private String userDept;
	private String userTitle;
	private String userPoliceNum;
	private List<Integer> delNodeIds;
	
	private File fi;
	private String fiFileName;
	private String fiContentType;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<UserListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<UserListItem> items) {
		this.items = items;
	}

	public boolean isQueryAll() {
		return queryAll;
	}

	public void setQueryAll(boolean queryAll) {
		this.queryAll = queryAll;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public List<Integer> getDelNodeIds() {
		return delNodeIds;
	}

	public void setDelNodeIds(List<Integer> delNodeIds) {
		this.delNodeIds = delNodeIds;
	}

	public File getFi() {
		return fi;
	}

	public void setFi(File fi) {
		this.fi = fi;
	}

	public String getFiFileName() {
		return fiFileName;
	}

	public void setFiFileName(String fiFileName) {
		this.fiFileName = fiFileName;
	}

	public String getFiContentType() {
		return fiContentType;
	}

	public void setFiContentType(String fiContentType) {
		this.fiContentType = fiContentType;
	}

	public String SaveUser()
	{
		UserManageService ums = new UserManageService();
		try {
			user = ums.SaveUser(user);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryUserItems()
	{
		UserManageService ums = new UserManageService();
		items = new ArrayList<UserListItem>();
		if( id == null || id.length() == 0) {
			id = "0";
		}
		try {
			if( queryAll ) {
				User criteria = new User();
				criteria.setNAME(userName);
				criteria.setUSER_STATUS(userStatus);
				criteria.setBUSINESS_TYPE(userUnit);
				criteria.setPOLICE_SORT(userPoliceType);
				criteria.setSEXCODE(userSex);
				criteria.setCERTIFICATE_CODE_SUFFIX(userIdnum);
				criteria.setSENSITIVE_LEVEL(userMaxSensitiveLevel);
				criteria.setPosition(userPosition);
				criteria.setDept(userDept);
				criteria.setTAKE_OFFICE(userTitle);
				criteria.setPOLICE_NO(userPoliceNum);
				total = ums.QueryAllUserItems( id, criteria, page, rows, items );
			} else {
				total = ums.QueryUserItems( id, page, rows, items );
			}
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteUserNode()
	{
		UserManageService ums = new UserManageService();
		try {
			ums.DeleteUserNodes(delNodeIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String FileUploadUser(){
		UserUploadService uus = new UserUploadService();
		try {
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			}
			else {
				uus.UploadUser(fi);
			}
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			return SUCCESS;
		}

		setResult(true);
		return SUCCESS;
		
	}
}

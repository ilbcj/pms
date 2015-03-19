package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.OrgListItem;
import com.pms.dto.UserListItem;
import com.pms.model.User;
import com.pms.service.UserManageService;

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
	private int id;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private User user;
	private ArrayList<UserListItem> items;
	
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
		try {
			if( queryAll ) {

//				User condition = new User();
//				condition.setName(orgName);
//				condition.setUid(orgUid);
//				condition.setOrg_level(orgLevel);
//				total = ums.QueryAllChildrenNodes( id, condition, page, rows, items );
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
}

package com.pms.controller;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.AppRole;
import com.pms.service.AppRoleManageService;

public class AppRoleAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5777897978281291476L;

	private boolean result;
	private String message;
	private boolean queryAll;
	private int page;
	private int rows;
	private int total;
	
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

	public boolean isQueryAll() {
		return queryAll;
	}

	public void setQueryAll(boolean queryAll) {
		this.queryAll = queryAll;
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

	public AppRole getAppRole() {
		return appRole;
	}

	public void setAppRole(AppRole appRole) {
		this.appRole = appRole;
	}

	private AppRole appRole;

	public String SaveAppRole()
	{
		AppRoleManageService arms = new AppRoleManageService();
		try {
			appRole = arms.SaveAppRole(appRole);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

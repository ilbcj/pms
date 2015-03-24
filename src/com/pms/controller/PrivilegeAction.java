package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.AppRoleItem;
import com.pms.service.PrivilegeManageService;

public class PrivilegeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5281263737259390032L;

	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private List<AppRoleItem> items;
	private String orgids;
	
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


	public List<AppRoleItem> getItems() {
		return items;
	}


	public void setItems(List<AppRoleItem> items) {
		this.items = items;
	}


	public String QueryAllAppRoleItems()
	{
		PrivilegeManageService pms = new PrivilegeManageService();
		items = new ArrayList<AppRoleItem>();
		try {
			items = pms.QueryAllAppRoleItems();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

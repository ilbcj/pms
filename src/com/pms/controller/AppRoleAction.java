package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	private ArrayList<AppRole> items;
	private String appRoleName;
	private String appRoleCode;
	private int id;//app_id
	private List<Integer> delAppRoleIds;
	
	public List<Integer> getDelAppRoleIds() {
		return delAppRoleIds;
	}

	public void setDelAppRoleIds(List<Integer> delAppRoleIds) {
		this.delAppRoleIds = delAppRoleIds;
	}

	public ArrayList<AppRole> getItems() {
		return items;
	}

	public void setItems(ArrayList<AppRole> items) {
		this.items = items;
	}

	public String getAppRoleName() {
		return appRoleName;
	}

	public void setAppRoleName(String appRoleName) {
		this.appRoleName = appRoleName;
	}

	public String getAppRoleCode() {
		return appRoleCode;
	}

	public void setAppRoleCode(String appRoleCode) {
		this.appRoleCode = appRoleCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public String QueryAppRoleItems()
	{
		AppRoleManageService arms = new AppRoleManageService();
		items = new ArrayList<AppRole>();
		try {
			if( queryAll ) {
				AppRole criteria = new AppRole();
				criteria.setName(appRoleName);
				criteria.setCode(appRoleCode);
				total = arms.QueryAllAppRoleItems( id, criteria, page, rows, items );
			} else {
				total = arms.QueryAllAppRoleItems( id, null, page, rows, items );
			}
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteAppRole()
	{
		AppRoleManageService arms = new AppRoleManageService();
		try {
			arms.DeleteAppRoles(delAppRoleIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
			
}

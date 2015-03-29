package com.pms.controller;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.AppRoleItem;
import com.pms.dto.PrivilegeTemp;
import com.pms.model.Privilege;
import com.pms.service.PrivilegeManageService;

public class PrivilegeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5281263737259390032L;
	private static final int SAVETYPEADD = 1;
	private static final int SAVETYPEMOD = 2;
	
	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private List<AppRoleItem> items;
	private List<PrivilegeTemp> privilegesToSave;
	private List<Privilege> privileges;
	private String ownerIds;
	private int ownerType;
	private int saveType;
	
	public int getSaveType() {
		return saveType;
	}


	public void setSaveType(int saveType) {
		this.saveType = saveType;
	}

	public List<PrivilegeTemp> getPrivilegesToSave() {
		return privilegesToSave;
	}


	public void setPrivilegesToSave(List<PrivilegeTemp> privilegesToSave) {
		this.privilegesToSave = privilegesToSave;
	}


	public List<Privilege> getPrivileges() {
		return privileges;
	}


	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}

	public String getOwnerIds() {
		return ownerIds;
	}


	public void setOwnerIds(String ownerIds) {
		this.ownerIds = ownerIds;
	}


	public int getOwnerType() {
		return ownerType;
	}


	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
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


	public List<AppRoleItem> getItems() {
		return items;
	}


	public void setItems(List<AppRoleItem> items) {
		this.items = items;
	}


	public String QueryAllAppRoleItems()
	{
		PrivilegeManageService pms = new PrivilegeManageService();
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
	
	public String SavePrivileges()
	{
		PrivilegeManageService pms = new PrivilegeManageService();
		try {
			if( PrivilegeAction.SAVETYPEADD == saveType ) {
				pms.SavePrivilege(ownerIds, ownerType, privilegesToSave);
			}
			else if( PrivilegeAction.SAVETYPEMOD == saveType ) {
				int orgid = Integer.parseInt(ownerIds);
				pms.UpdatePrivilege(orgid, ownerType, privilegesToSave);
			}
			else {
				message = "传入服务的待保存数据不完整。";
				setResult(false);
				return SUCCESS;
			}
//			System.out.println(orgids);
//			System.out.println(privileges);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryPrivilegesByOwnerId()
	{
		PrivilegeManageService pms = new PrivilegeManageService();
		try {
			int orgid = Integer.parseInt(ownerIds);
			privileges = pms.QueryPrivilegesByOwnerId(orgid, ownerType);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

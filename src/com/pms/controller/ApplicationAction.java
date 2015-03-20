package com.pms.controller;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.Application;
import com.pms.service.AppManageService;

public class ApplicationAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3415275039136675054L;

	private boolean result;
	private String message;
	private boolean queryAll;
	private int page;
	private int rows;
	private int total;

	private Application app;

	private String pwdstrength_lower;
	private String pwdstrength_upper;
	private String pwdstrength_number;
	private String pwdstrength_special;
	
	public String getPwdstrength_special() {
		return pwdstrength_special;
	}
	public void setPwdstrength_special(String pwdstrength_special) {
		this.pwdstrength_special = pwdstrength_special;
	}

	public String getPwdstrength_lower() {
		return pwdstrength_lower;
	}
	public void setPwdstrength_lower(String pwdstrength_lower) {
		this.pwdstrength_lower = pwdstrength_lower;
	}
	
	public String getPwdstrength_upper() {
		return pwdstrength_upper;
	}
	public void setPwdstrength_upper(String pwdstrength_upper) {
		this.pwdstrength_upper = pwdstrength_upper;
	}
	public String getPwdstrength_number() {
		return pwdstrength_number;
	}
	public void setPwdstrength_number(String pwdstrength_number) {
		this.pwdstrength_number = pwdstrength_number;
	}
	public Application getApp() {
		return app;
	}
	public void setApp(Application app) {
		this.app = app;
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

	
	public String SaveApp()
	{
		AppManageService ams = new AppManageService();
		try {
			if( Application.CHECKBOXCHECKED.equals(app.getHasPwdAccount()) 
					&& Application.PWDPOLICYSELF.equals(app.getPwd_policy()) ) {
				String pwdstrength = "";
				if( Application.CHECKBOXCHECKED.equals(pwdstrength_lower) ) {
					pwdstrength += Application.PWDSTRENGTHLOWER;
				}
				if( Application.CHECKBOXCHECKED.equals(pwdstrength_upper) ) {
					pwdstrength += Application.PWDSTRENGTHUPPER;
				}
				if( Application.CHECKBOXCHECKED.equals(pwdstrength_number) ) {
					pwdstrength += Application.PWDSTRENGTHNUMBER;
				}
				if( Application.CHECKBOXCHECKED.equals(pwdstrength_special) ) {
					pwdstrength += Application.PWDSTRENGTHSPECIAL;
				}
				app.setPwdstrength(pwdstrength);
			}
			app = ams.SaveApp(app);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

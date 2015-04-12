package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.TreeNode;
import com.pms.model.Application;
import com.pms.service.AppManageService;

public class ApplicationAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3415275039136675054L;

	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;

	private Application app;
	private List<Application> items;
	private String appName;
	private String appFlag;
	private String pwdstrength_lower;
	private String pwdstrength_upper;
	private String pwdstrength_number;
	private String pwdstrength_special;
	private List<Integer> delAppIds;
	private List<TreeNode> treeNodes;
	private int id;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}
	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}
	public List<Integer> getDelAppIds() {
		return delAppIds;
	}
	public void setDelAppIds(List<Integer> delAppIds) {
		this.delAppIds = delAppIds;
	}
	
	public List<Application> getItems() {
		return items;
	}
	public void setItems(List<Application> items) {
		this.items = items;
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
			if( Application.CHECKBOXCHECKED.equals(app.getHasAccountType()) ) {
				if( Application.CHECKBOXCHECKED.equals(app.getHasPwdAccount()) ) {
					if( Application.PWDPOLICYSELF.equals(app.getPwd_policy()) ) {
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
					else {
						app.setDefault_username(null);
						app.setDefault_pwd_type(null);
						app.setDefault_pwd(null);
						app.setPwdlen_min(0);
						app.setPwdlen_max(0);
						app.setPwdstrength(null);
					}
						
				}
				else {
					app.setPwd_policy(null);
					app.setDefault_username(null);
					app.setDefault_pwd_type(null);
					app.setDefault_pwd(null);
					app.setPwdlen_min(0);
					app.setPwdlen_max(0);
					app.setPwdstrength(null);
				}
				
				if( !Application.CHECKBOXCHECKED.equals(app.getHasCertAccount()) ) {
					app.setCert_policy_pki(null);
				}
			}else {
				app.setHasPwdAccount(null);
				app.setPwd_policy(null);
				app.setDefault_username(null);
				app.setDefault_pwd_type(null);
				app.setDefault_pwd(null);
				app.setPwdlen_min(0);
				app.setPwdlen_max(0);
				app.setPwdstrength(null);
				app.setHasCertAccount(null);
				app.setCert_policy_pki(null);
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
	
	public String QueryAppItems() 
	{
		AppManageService ams = new AppManageService();
		items = new ArrayList<Application>();
		try {
//			if( queryAll ) {
				Application criteria = new Application();
				criteria.setName(appName);
				criteria.setFlag(appFlag);
				total = ams.QueryAllAppItems( criteria, page, rows, items );
//			} else {
//				total = ams.QueryUserItems( id, page, rows, items );
//			}
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteApp()
	{
		AppManageService ams = new AppManageService();
		try {
			ams.DeleteApps(delAppIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAppNodes()
	{
		AppManageService ams = new AppManageService();
		try {
			treeNodes = new ArrayList<TreeNode>();
			if( id == 0 ) {
				TreeNode root = new TreeNode();
				root.setState("closed");
				root.setId(-1);
				root.setText("应用列表");
				treeNodes.add(root);
			}
			else {
				ams.QueryAllApps(treeNodes);
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

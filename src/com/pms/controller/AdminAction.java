/**   
 * @ClassName:     ${GroupAction}   
 * @Description:   ${群体管理control}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.8.12} 
*/
package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.Admin;
import com.pms.model.AdminAccredit;
import com.pms.service.AdministratorManageService;

public class AdminAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 683940319646526137L;

	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private String adminLoginid;
	private int adminStatus;
	private List<Integer> pid;
	private List<Integer> delIds;
	private Admin admin;
	private List<Admin> items;
	private List<AdminAccredit> accredits;
	

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

	public String getAdminLoginid() {
		return adminLoginid;
	}

	public void setAdminLoginid(String adminLoginid) {
		this.adminLoginid = adminLoginid;
	}

	public int getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(int adminStatus) {
		this.adminStatus = adminStatus;
	}

	public List<Integer> getPid() {
		return pid;
	}

	public void setPid(List<Integer> pid) {
		this.pid = pid;
	}

	public List<Integer> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<Integer> delIds) {
		this.delIds = delIds;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public List<Admin> getItems() {
		return items;
	}

	public void setItems(List<Admin> items) {
		this.items = items;
	}

	public List<AdminAccredit> getAccredits() {
		return accredits;
	}

	public void setAccredits(List<AdminAccredit> accredits) {
		this.accredits = accredits;
	}

	public String QueryAdmin()
	{
		AdministratorManageService ams = new AdministratorManageService();
		items = new ArrayList<Admin>();
		try {
			Admin criteria = new Admin();
			criteria.setLoginid(adminLoginid);
			criteria.setStatus(adminStatus);
			total = ams.QueryAdministrators( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAdminAccreditById()
	{
		AdministratorManageService ams = new AdministratorManageService();
		try {
			accredits = ams.QueryAccreditByAdminId( admin.getId() );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAdminByLoginid()
	{
		AdministratorManageService ams = new AdministratorManageService();
		try {
			admin = ams.QueryAdminByLoginid( admin.getLoginid() );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveAdmin()
	{
		AdministratorManageService ams = new AdministratorManageService();
		try {
			admin = ams.SaveAdmin(admin, pid);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteAdmin()
	{
		AdministratorManageService ams = new AdministratorManageService();
		try {
			ams.DeleteAdmin(delIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
}

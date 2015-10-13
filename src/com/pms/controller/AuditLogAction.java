package com.pms.controller;


import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.AuditLog;
import com.pms.service.AuditLogService;

@SuppressWarnings("serial")
public class AuditLogAction extends ActionSupport {
	
	private int page;
	private int rows;
	private int total;
	private String message;
	private boolean result;
	private List<AuditLog> items;
	
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public List<AuditLog> getItems() {
		return items;
	}
	public void setItems(List<AuditLog> items) {
		this.items = items;
	}
	
	public String QueryAllAuditLog()
	{
		AuditLogService oms = new AuditLogService();
		items = new ArrayList<AuditLog>();
		try {
			AuditLog criteria=new AuditLog();
			total = oms.QueryAllAuditLogItems(criteria, page, rows, items);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

}

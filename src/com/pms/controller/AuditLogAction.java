package com.pms.controller;


import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.LogGroupItem;
import com.pms.dto.LogItem;
import com.pms.dto.LogOrgItem;
import com.pms.dto.LogPrivItem;
import com.pms.dto.LogResItem;
import com.pms.dto.LogRoleItem;
import com.pms.dto.LogSystemItem;
import com.pms.dto.LogUserItem;
import com.pms.model.AuditGroupLog;
import com.pms.model.AuditLog;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditPrivLog;
import com.pms.model.AuditResLog;
import com.pms.model.AuditRoleLog;
import com.pms.model.AuditSystemLog;
import com.pms.model.AuditUserLog;
import com.pms.service.AuditLogService;

@SuppressWarnings("serial")
public class AuditLogAction extends ActionSupport {
	
	private int page;
	private int rows;
	private int total;
	private String message;
	private boolean result;
	private List<AuditUserLog> items;
	private List<LogUserItem> logUserItems;
	private List<LogOrgItem> logOrgItems;
	private List<LogGroupItem> logGroupItems;
	private List<LogResItem> logResItems;
	private List<LogRoleItem> logRoleItems;
	private List<LogSystemItem> logSystemItems;
	private List<LogPrivItem> logPrivItems;
	private List<LogItem> logItems;
	
	private String flag;
	
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
	public List<AuditUserLog> getItems() {
		return items;
	}
	public void setItems(List<AuditUserLog> items) {
		this.items = items;
	}
	public List<LogUserItem> getLogUserItems() {
		return logUserItems;
	}
	public void setLogUserItems(List<LogUserItem> logUserItems) {
		this.logUserItems = logUserItems;
	}
	public List<LogOrgItem> getLogOrgItems() {
		return logOrgItems;
	}
	public void setLogOrgItems(List<LogOrgItem> logOrgItems) {
		this.logOrgItems = logOrgItems;
	}
	public List<LogGroupItem> getLogGroupItems() {
		return logGroupItems;
	}
	public void setLogGroupItems(List<LogGroupItem> logGroupItems) {
		this.logGroupItems = logGroupItems;
	}
	public List<LogResItem> getLogResItems() {
		return logResItems;
	}
	public void setLogResItems(List<LogResItem> logResItems) {
		this.logResItems = logResItems;
	}
	public List<LogRoleItem> getLogRoleItems() {
		return logRoleItems;
	}
	public void setLogRoleItems(List<LogRoleItem> logRoleItems) {
		this.logRoleItems = logRoleItems;
	}
	public List<LogSystemItem> getLogSystemItems() {
		return logSystemItems;
	}
	public void setLogSystemItems(List<LogSystemItem> logSystemItems) {
		this.logSystemItems = logSystemItems;
	}
	public List<LogPrivItem> getLogPrivItems() {
		return logPrivItems;
	}
	public void setLogPrivItems(List<LogPrivItem> logPrivItems) {
		this.logPrivItems = logPrivItems;
	}
	public List<LogItem> getLogItems() {
		return logItems;
	}
	public void setLogItems(List<LogItem> logItems) {
		this.logItems = logItems;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String QueryAllAuditUserLog()
	{
		AuditLogService oms = new AuditLogService();
		logUserItems = new ArrayList<LogUserItem>();
		try {
			AuditUserLog criteria=new AuditUserLog();
			criteria.setFlag(flag);
			total = oms.QueryUserLogItems(criteria, page, rows, logUserItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditOrgLog()
	{
		AuditLogService oms = new AuditLogService();
		logOrgItems = new ArrayList<LogOrgItem>();
		try {
			AuditOrgLog criteria=new AuditOrgLog();
			criteria.setFlag(flag);
			total = oms.QueryOrgLogItems(criteria, page, rows, logOrgItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditGroupLog()
	{
		AuditLogService oms = new AuditLogService();
		logGroupItems = new ArrayList<LogGroupItem>();
		try {
			AuditGroupLog criteria=new AuditGroupLog();
			criteria.setFlag(flag);
			total = oms.QueryGroupLogItems(criteria, page, rows, logGroupItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditResLog()
	{
		AuditLogService oms = new AuditLogService();
		logResItems = new ArrayList<LogResItem>();
		try {
			AuditResLog criteria=new AuditResLog();
			criteria.setFlag(flag);
			total = oms.QueryResLogItems(criteria, page, rows, logResItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditRoleLog()
	{
		AuditLogService oms = new AuditLogService();
		logRoleItems = new ArrayList<LogRoleItem>();
		try {
			AuditRoleLog criteria=new AuditRoleLog();
			criteria.setFlag(flag);
			total = oms.QueryRoleLogItems(criteria, page, rows, logRoleItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditSystemLog()
	{
		AuditLogService oms = new AuditLogService();
		logSystemItems = new ArrayList<LogSystemItem>();
		try {
			AuditSystemLog criteria=new AuditSystemLog();
			criteria.setFlag(flag);
			total = oms.QuerySystemLogItems(criteria, page, rows, logSystemItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditPrivLog()
	{
		AuditLogService oms = new AuditLogService();
		logPrivItems = new ArrayList<LogPrivItem>();
		try {
			AuditPrivLog criteria=new AuditPrivLog();
			criteria.setFlag(flag);
			total = oms.QueryPrivLogItems(criteria, page, rows, logPrivItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllAuditLog()
	{
		AuditLogService oms = new AuditLogService();
		logItems = new ArrayList<LogItem>();
		try {
			AuditLog criteria=new AuditLog();
			criteria.setFlag(flag);
			total = oms.QueryLogItems(criteria, page, rows, logItems);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

}

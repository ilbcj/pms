package com.pms.service;

import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.pms.dao.AdminDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.impl.AdminDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dto.LogOrgItem;
import com.pms.dto.LogUserItem;
import com.pms.model.Admin;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditOrgLogDescribe;
import com.pms.model.AuditUserLog;
import com.pms.model.AuditUserLogDescribe;

public class AuditLogService {
	public int QueryUserLogItems(AuditUserLog criteria, int page, int rows, List<LogUserItem> items) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		List<AuditUserLog> res = dao.GetAllAuditUserLogs(criteria, page, rows );
		LogUserItem logUserItem = null;
		for(int i=0; i<res.size(); i++) {
			logUserItem = ConvertUserLogToListItem(res.get(i));
			items.add(logUserItem);
		}
		int total = QueryUserLogsCount( criteria );
		return total;
	}
	
	private int QueryUserLogsCount(AuditUserLog criteria) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		int count = dao.GetAuditUserLogsCount( criteria );
		return count;
	}
	
	public LogUserItem ConvertUserLogToListItem(AuditUserLog auditUserLog) throws Exception {
		LogUserItem item = new LogUserItem();
		item.setLogid(auditUserLog.getId());
		item.setAdminId(auditUserLog.getAdminId());
		item.setIpAddr(auditUserLog.getIpAddr());
		item.setFlag(auditUserLog.getFlag());
		item.setResult(auditUserLog.getResult());
		item.setLATEST_MOD_TIME(auditUserLog.getLATEST_MOD_TIME());
		
		AuditLogDescribeDao dao = new AuditLogDescribeDAOImpl();
		List<AuditUserLogDescribe> logdesc = dao.GetUserLogDescByLogId(auditUserLog.getId());
		for (int i = 0; i < logdesc.size(); i++) {
			item.setDesc(logdesc.get(i).getDescrib());
		}
		
		return item;
		
	}
	
	public int QueryOrgLogItems(AuditOrgLog criteria, int page, int rows, List<LogOrgItem> items) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		List<AuditOrgLog> res = dao.GetAllAuditOrgLogs(criteria, page, rows );
		LogOrgItem logOrgItem = null;
		for(int i=0; i<res.size(); i++) {
			logOrgItem = ConvertOrgLogToListItem(res.get(i));
			items.add(logOrgItem);
		}
		int total = QueryOrgLogsCount( criteria );
		return total;
	}
	
	private int QueryOrgLogsCount(AuditOrgLog criteria) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		int count = dao.GetAuditOrgLogsCount( criteria );
		return count;
	}
	
	public LogOrgItem ConvertOrgLogToListItem(AuditOrgLog auditOrgLog) throws Exception {
		LogOrgItem item = new LogOrgItem();
		item.setLogid(auditOrgLog.getId());
		item.setAdminId(auditOrgLog.getAdminId());
		item.setIpAddr(auditOrgLog.getIpAddr());
		item.setFlag(auditOrgLog.getFlag());
		item.setResult(auditOrgLog.getResult());
		item.setLATEST_MOD_TIME(auditOrgLog.getLATEST_MOD_TIME());
		
		AuditLogDescribeDao dao = new AuditLogDescribeDAOImpl();
		List<AuditOrgLogDescribe> logdesc = dao.GetOrgLogDescByLogId(auditOrgLog.getId());
		for (int i = 0; i < logdesc.size(); i++) {
			item.setDesc(logdesc.get(i).getDescrib());
		}
		
		return item;
		
	}
	
	public int adminLogin() throws Exception {
		AdminDAO dao = new AdminDAOImpl();
		ActionContext ctx = ActionContext.getContext();
		String user=(String) ctx.getSession().get("admin");
		Admin admin = null;
		admin = dao.GetAdminByLoginid(user);
		
		return admin.getId();
	}
}

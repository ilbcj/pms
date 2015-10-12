package com.pms.service;

import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.pms.dao.AdminDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.impl.AdminDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dto.UserLogItem;
import com.pms.model.Admin;
import com.pms.model.AuditUserLog;
import com.pms.model.AuditUserLogDescribe;

public class AuditLogService {
	public int QueryAllAuditLogItems(AuditUserLog criteria, int page, int rows, List<UserLogItem> items) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		List<AuditUserLog> res = dao.GetAllAuditUserLogs(criteria, page, rows );
		UserLogItem userLogItem = null;
		for(int i=0; i<res.size(); i++) {
			userLogItem = ConvertLogToListItem(res.get(i));
			items.add(userLogItem);
		}
		int total = QueryAllAuditLogsCount( criteria );
		return total;
	}
	
	private int QueryAllAuditLogsCount(AuditUserLog criteria) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		int count = dao.GetAuditUserLogsCount( criteria );
		return count;
	}
	
	public UserLogItem ConvertLogToListItem(AuditUserLog auditUserLog) throws Exception {
		UserLogItem item = new UserLogItem();
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
	
	public int adminLogin(){
		AdminDAO dao = new AdminDAOImpl();
		ActionContext ctx = ActionContext.getContext();
		String user=(String) ctx.getSession().get("admin");
		Admin admin = null;
		try {
			admin = dao.GetAdminByLoginid(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return admin.getId();
	}
}

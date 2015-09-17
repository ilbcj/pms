package com.pms.service;

import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.pms.dao.AdminDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.impl.AdminDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.model.Admin;
import com.pms.model.AuditLog;

public class AuditLogService {
	public int QueryAllAuditLogItems(AuditLog criteria, int page, int rows, List<AuditLog> items) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		List<AuditLog> res = dao.GetAllAuditLogs(criteria, page, rows );
		items.addAll(res);
		int total = QueryAllAuditLogsCount( criteria );
		return total;
	}
	
	private int QueryAllAuditLogsCount(AuditLog criteria) throws Exception {
		AuditLogDAO dao = new AuditLogDAOImpl();
		int count = dao.GetAuditLogsCount( criteria );
		return count;
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

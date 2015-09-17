package com.pms.dao;

import java.util.List;

import com.pms.model.AuditLog;

public interface AuditLogDAO {
	AuditLog AuditLogAdd(AuditLog auditLog) throws Exception;
	List<AuditLog> GetAllAuditLogs(AuditLog criteria, int page, int rows) throws Exception;
	int GetAuditLogsCount(AuditLog criteria) throws Exception;
}

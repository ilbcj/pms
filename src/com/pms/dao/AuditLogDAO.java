package com.pms.dao;

import java.util.List;

import com.pms.model.AuditUserLog;

public interface AuditLogDAO {
	AuditUserLog AuditUserLogAdd(AuditUserLog auditUserLog) throws Exception;
	List<AuditUserLog> GetAllAuditUserLogs(AuditUserLog criteria, int page, int rows) throws Exception;
	int GetAuditUserLogsCount(AuditUserLog criteria) throws Exception;
}

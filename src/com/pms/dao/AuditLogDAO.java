package com.pms.dao;

import java.util.List;

import com.pms.model.AuditGroupLog;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditPrivLog;
import com.pms.model.AuditResLog;
import com.pms.model.AuditUserLog;

public interface AuditLogDAO {
	AuditUserLog AuditUserLogAdd(AuditUserLog auditUserLog) throws Exception;
	List<AuditUserLog> GetAllAuditUserLogs(AuditUserLog criteria, int page, int rows) throws Exception;
	int GetAuditUserLogsCount(AuditUserLog criteria) throws Exception;
	List<AuditOrgLog> GetAllAuditOrgLogs(AuditOrgLog criteria, int page, int rows) throws Exception;
	AuditOrgLog AuditOrgLogAdd(AuditOrgLog auditOrgLog) throws Exception;
	int GetAuditOrgLogsCount(AuditOrgLog criteria) throws Exception;
	List<AuditGroupLog> GetAllAuditGroupLogs(AuditGroupLog criteria, int page, int rows) throws Exception;
	AuditGroupLog AuditGroupLogAdd(AuditGroupLog auditGroupLog) throws Exception;
	int GetAuditGroupLogsCount(AuditGroupLog criteria) throws Exception;
	List<AuditResLog> GetAllAuditResLogs(AuditResLog criteria, int page, int rows) throws Exception;
	AuditResLog AuditResLogAdd(AuditResLog auditResLog) throws Exception;
	int GetAuditResLogsCount(AuditResLog criteria) throws Exception;
	List<AuditPrivLog> GetAllAuditPrivLogs(AuditPrivLog criteria, int page, int rows) throws Exception;
	AuditPrivLog AuditPrivLogAdd(AuditPrivLog auditPrivLog) throws Exception;
	int GetAuditPrivLogsCount(AuditPrivLog criteria) throws Exception;
}

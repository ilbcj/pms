package com.pms.dao;

import java.util.List;

import com.pms.model.AuditGroupLog;
import com.pms.model.AuditLog;
import com.pms.model.AuditOrgLog;
import com.pms.model.AuditPrivLog;
import com.pms.model.AuditResLog;
import com.pms.model.AuditRoleLog;
import com.pms.model.AuditSystemLog;
import com.pms.model.AuditUserLog;

public interface AuditLogDAO {
	AuditUserLog AuditUserLogAdd(AuditUserLog auditUserLog) throws Exception;
	List<AuditUserLog> GetAllAuditUserLogs(AuditUserLog criteria, int page, int rows) throws Exception;
	int GetAuditUserLogsCount(AuditUserLog criteria) throws Exception;
	List<AuditUserLog> GetAuditUserLogsPercentageByFlag() throws Exception;
	List<AuditOrgLog> GetAllAuditOrgLogs(AuditOrgLog criteria, int page, int rows) throws Exception;
	AuditOrgLog AuditOrgLogAdd(AuditOrgLog auditOrgLog) throws Exception;
	int GetAuditOrgLogsCount(AuditOrgLog criteria) throws Exception;
	List<AuditOrgLog> GetAuditOrgLogsPercentageByFlag() throws Exception;
	List<AuditGroupLog> GetAllAuditGroupLogs(AuditGroupLog criteria, int page, int rows) throws Exception;
	AuditGroupLog AuditGroupLogAdd(AuditGroupLog auditGroupLog) throws Exception;
	int GetAuditGroupLogsCount(AuditGroupLog criteria) throws Exception;
	List<AuditGroupLog> GetAuditGroupLogsPercentageByFlag() throws Exception;
	List<AuditRoleLog> GetAllAuditRoleLogs(AuditRoleLog criteria, int page, int rows) throws Exception;
	AuditRoleLog AuditRoleLogAdd(AuditRoleLog auditRoleLog) throws Exception;
	int GetAuditRoleLogsCount(AuditRoleLog criteria) throws Exception;
	List<AuditRoleLog> GetAuditRoleLogsPercentageByFlag() throws Exception;
	List<AuditSystemLog> GetAllAuditSystemLogs(AuditSystemLog criteria, int page, int rows) throws Exception;
	AuditSystemLog AuditSystemLogAdd(AuditSystemLog auditSystemLog) throws Exception;
	int GetAuditSystemLogsCount(AuditSystemLog criteria) throws Exception;
	List<AuditSystemLog> GetAuditSystemLogsPercentageByFlag() throws Exception;
	List<AuditResLog> GetAllAuditResLogs(AuditResLog criteria, int page, int rows) throws Exception;
	AuditResLog AuditResLogAdd(AuditResLog auditResLog) throws Exception;
	int GetAuditResLogsCount(AuditResLog criteria) throws Exception;
	List<AuditResLog> GetAuditResLogsPercentageByFlag() throws Exception;
	List<AuditPrivLog> GetAllAuditPrivLogs(AuditPrivLog criteria, int page, int rows) throws Exception;
	AuditPrivLog AuditPrivLogAdd(AuditPrivLog auditPrivLog) throws Exception;
	int GetAuditPrivLogsCount(AuditPrivLog criteria) throws Exception;
	List<AuditPrivLog> GetAuditPrivLogsPercentageByFlag() throws Exception;
	List<AuditLog> GetAllAuditLogs(AuditLog criteria, int page, int rows) throws Exception;
	AuditLog AuditLogAdd(AuditLog auditLog) throws Exception;
	int GetAuditLogsCount(AuditLog criteria) throws Exception;
	List<AuditLog> GetAuditLogsPercentageByFlag() throws Exception;
}

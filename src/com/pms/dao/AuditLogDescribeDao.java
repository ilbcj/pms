package com.pms.dao;


import java.util.List;

import com.pms.model.AuditUserLogDescribe;

public interface AuditLogDescribeDao {
	AuditUserLogDescribe AuditUserLogDescribeAdd(AuditUserLogDescribe auditUserLogDescribe) throws Exception;
	List<AuditUserLogDescribe> GetUserLogDescByLogId(int logid) throws Exception;
}

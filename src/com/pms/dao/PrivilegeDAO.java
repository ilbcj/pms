package com.pms.dao;

import java.util.List;

import com.pms.model.Privilege;

public interface PrivilegeDAO {
	public Privilege PrivilegeAdd(Privilege priv) throws Exception;
	public void PrivilegeMod(Privilege priv) throws Exception;
	public void PrivilegeDel(Privilege priv) throws Exception;

	public List<Privilege> GetPrivileges(int pid, int page, int rows) throws Exception;
	public List<User> GetUsersByParentIdWithNoPage(int pid, User criteria) throws Exception;
	public int GetUsersCountByParentId(int id, User criteria) throws Exception;
}

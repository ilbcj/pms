package com.pms.dao;

import java.util.List;

import com.pms.model.Organization;

public interface OrganizationDAO {
	public Organization OrgNodeAdd(Organization node) throws Exception;
	public void OrgNodeMod(Organization node) throws Exception;
	public void OrgNodeDel(Organization node) throws Exception;
	public List<Organization> GetOrgNodeByParentId(int pid) throws Exception;
	public boolean OrgHasChild(int pid) throws Exception;
	public List<Organization> GetOrgNodeByParentId(int pid, int page, int rows) throws Exception;
	public int GetOrgNodeCountByParentId(int pid)throws Exception;
	List<Organization> GetOrgNodeByParentId(int pid, Organization condition) throws Exception;
	public Organization GetOrgNodeById(int id) throws Exception;;
}

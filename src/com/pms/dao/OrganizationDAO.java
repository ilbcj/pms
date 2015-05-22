package com.pms.dao;

import java.util.List;

import com.pms.model.Organization;

public interface OrganizationDAO {
	public Organization OrgNodeAdd(Organization node) throws Exception;
	public void OrgNodeMod(Organization node) throws Exception;
	public void OrgNodeDel(Organization node) throws Exception;
	public List<Organization> GetOrgNodeByParentId(String pid) throws Exception;
	public boolean OrgHasChild(String pid) throws Exception;
	public List<Organization> GetOrgNodeByParentId(String pid, int page, int rows) throws Exception;
	public int GetOrgNodeCountByParentId(String pid)throws Exception;
	List<Organization> GetOrgNodeByParentId(String pid, Organization condition) throws Exception;
	public Organization GetOrgNodeById(String id) throws Exception;;
}

package com.pms.dao;

import java.util.List;

import com.pms.model.Organization;
import com.pms.model.OrganizationImport;

public interface OrganizationDAO {
	public Organization OrgNodeAdd(Organization node) throws Exception;
	public void OrgNodeMod(Organization node) throws Exception;
	public void OrgNodeDel(Organization node) throws Exception;
	List<Organization> GetOrgsByTime(String time) throws Exception;
	public List<Organization> GetOrgNodeByParentId(String pid) throws Exception;
	public boolean OrgHasChild(String pid) throws Exception;
	public List<Organization> GetOrgNodeByParentId(String pid, int page, int rows) throws Exception;
	public int GetOrgNodeCountByParentId(String pid)throws Exception;
	List<Organization> GetOrgNodeByParentId(String pid, Organization condition) throws Exception;
	public Organization GetOrgNodeById(String id) throws Exception;
	List<Organization> GetOrgById(String id) throws Exception;
	List<Organization> GetOrgNodeByParentIdAndIdNotIn(String pid, List<String> id) throws Exception;
	List<Organization> GetOrgByIdNotIn(List<String> id, Organization condition, int page, int rows) throws Exception;
	int GetSyncConfigOrgCount(List<String> id, Organization condition) throws Exception;
	
	List<Organization> GetAllOrgs() throws Exception;
	List<Organization> GetAllOrgs(Organization condition) throws Exception;
	OrganizationImport OrganizationImportSave(OrganizationImport oi) throws Exception;
	public List<OrganizationImport> GetOrgImports() throws Exception;
	public void OrgImport(OrganizationImport oi) throws Exception;
}

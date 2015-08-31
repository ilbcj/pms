package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.OrganizationDAO;
import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.dto.OrgListItem;
import com.pms.dto.TreeNode;
import com.pms.model.Organization;

public class OrgManageService {
	public List<Organization> QueryAllBureauNode() throws Exception
	{
		return QueryChildrenNodes( Organization.ROOTNODEID );
	}
	
	public Organization CheckUid( String uid ) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		return dao.GetOrgNodeById( uid );
	}
	
	public Organization SaveOrgNode( Organization org ) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		org.setLATEST_MOD_TIME(timenow);
		org.setDATA_VERSION(org.getDATA_VERSION()+1);
		org = dao.OrgNodeAdd(org);
		return org;
	}
	
	public int QueryChildrenNodes(String pid, int page, int rows, List<OrgListItem> items) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = dao.GetOrgNodeByParentId( pid, page, rows );
		OrgListItem node = null;
		for(int i=0; i<res.size(); i++) {
			node = ConvertOrgToListItem(res.get(i));
			items.add(node);
		}
		int total = QueryChildrenNodesCount( pid );
		return total;
	}
	
	public Organization DeleteOrgNode(Organization target) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> nodes = dao.GetOrgById(target.getGA_DEPARTMENT());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());

		for(int i = 0; i< nodes.size(); i++) {
			target.setUNIT(nodes.get(i).getUNIT());
			target.setORG_LEVEL(nodes.get(i).getORG_LEVEL());
			target.setPARENT_ORG(nodes.get(i).getPARENT_ORG());
			target.setDELETE_STATUS(Organization.DELSTATUSYES);
			target.setDATA_VERSION(nodes.get(i).getDATA_VERSION());
			target.setLATEST_MOD_TIME(timenow);
			
			target = dao.OrgNodeAdd(target);
		}
		
		return target;
	}
	
	public void DeleteOrgNodes(List<String> nodeIds) throws Exception
	{
		if(nodeIds == null)
			return;
		Organization target;
		for(int i = 0; i< nodeIds.size(); i++) {
			target = new Organization();
			target.setGA_DEPARTMENT(nodeIds.get(i));
			
			DeleteOrgNode(target);
		}
		
		return ;
	}
	
//	private void getAllChildrenNodesById(String pid, List<Organization> children) throws Exception
//	{
//		OrganizationDAO dao = new OrganizationDAOImpl();
//		List<Organization> res = dao.GetOrgNodeByParentId( pid );
//		if(res == null || res.size() == 0) {
//			return;
//		}
//		else {
//			children.addAll(res);
//		}
//			
//		for(int i=0; i<res.size(); i++)
//		{
//			getAllChildrenNodesById(res.get(i).getGA_DEPARTMENT(), children);
//		}
//		return;
//	}

	public TreeNode ConvertOrgToTreeNode(Organization org) throws Exception {
		TreeNode node = new TreeNode();
		String state = hasChild(org.getGA_DEPARTMENT()) ? "closed" : "open";
		node.setState(state);
		node.setId(org.getGA_DEPARTMENT());
		node.setText(org.getUNIT());
		
		return node;
	}
	
	private boolean hasChild(String pid) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		return dao.OrgHasChild( pid );
	}

	public OrgListItem ConvertOrgToListItem(Organization org) throws Exception {
		OrgListItem item = new OrgListItem();
		item.setId(org.getGA_DEPARTMENT());
		item.setName(org.getUNIT());
		item.setPid(org.getPARENT_ORG());
		item.setOrgLevel(org.getORG_LEVEL());
		item.setData_version(org.getDATA_VERSION());
		item.setOrgParent_org(org.getPARENT_ORG());
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization parent = dao.GetOrgNodeById(org.getPARENT_ORG());
		item.setPname(parent == null ? "" : parent.getUNIT());
		return item;
	}

	public List<Organization> QueryChildrenNodes(String id) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = dao.GetOrgNodeByParentId( id );
		return res;
	}

	public int QueryChildrenNodesCount(String id) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		int count = dao.GetOrgNodeCountByParentId( id );
		return count;
	}

	public int QueryAllChildrenNodes(String id, Organization condition, int page, int rows,
			List<OrgListItem> items) throws Exception {
		//get all items;
		List<Organization> nodes = new ArrayList<Organization>();
		queryAllChildrenNodesById(id, condition, nodes);
		//get all items' count;
		int total = nodes.size();
		//convert to datagrid's item
		OrgListItem node = null;
		for(int i=(page-1)*rows; i<page*rows && i<total; i++) {
			node = ConvertOrgToListItem(nodes.get(i));
			items.add(node);
		}
		return total;
	}
	
	public void queryAllChildrenNodesById(String pid, Organization condition, List<Organization> children) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = null; 
		if( pid.equals("0") ){	
			res = dao.GetAllOrgs(condition);
			if(res == null || res.size() == 0) {
				return;
			}
			else {
				children.addAll(res);
			}
			return;
		}else{
			res = dao.GetOrgNodeByParentId( pid, condition );
			if(res == null || res.size() == 0) {
				return;
			}
			else {
				children.addAll(res);
			}
				
			for(int i=0; i<res.size(); i++)
			{
				queryAllChildrenNodesById(res.get(i).getGA_DEPARTMENT(), condition, children);
			}
		}
		return;
	}

	//public void ModifyOrgNodeName(int id, String orgName, String orgUid) throws Exception {
	public void ModifyOrgNodeName(String id, String orgName) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization node = dao.GetOrgNodeById(id);
		if(node == null) {
			throw new Exception("ID为" + id + "的机构结点不存在。");
		}
		
		node.setUNIT(orgName);
		//node.setUid(orgUid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		node.setLATEST_MOD_TIME(timenow);
		node.setDATA_VERSION(node.getDATA_VERSION()+1);
		node = dao.OrgNodeAdd(node);
		return;
	}
	
	public String QueryNodePath(String nodeid) throws Exception
	{
		String name = "";
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization res = null;
		do 
		{
			res = dao.GetOrgNodeById( nodeid );
			if(res != null ) {
				name = res.getUNIT() + "/" + name;
				nodeid = res.getPARENT_ORG();
			}
		}while( res != null);
		
		if(name.length() > 0) {
			name = name.substring(0, name.length()-1);
		}
		return name;
	}
}

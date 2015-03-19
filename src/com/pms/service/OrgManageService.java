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
	
	public Organization SaveOrgNode( Organization org ) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		org.setTstamp(timenow);
		org = dao.OrgNodeAdd(org);
		return org;
	}
	
	public int QueryChildrenNodes(int pid, int page, int rows, List<OrgListItem> items) throws Exception
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
	
	public void DeleteOrgNode(Organization target) throws Exception
	{
		List<Organization> nodes = new ArrayList<Organization>();
		getAllChildrenNodesById(target.getId(), nodes);
		nodes.add(target);
		
		OrganizationDAO dao = new OrganizationDAOImpl();
		for(int i = 0; i< nodes.size(); i++) {
			dao.OrgNodeDel(nodes.get(i));
		}
		
		return;
	}
	
	public void DeleteOrgNodes(List<Integer> nodeIds) throws Exception
	{
		if(nodeIds == null)
			return;
		Organization target;
		for(int i = 0; i< nodeIds.size(); i++) {
			target = new Organization();
			target.setId(nodeIds.get(i));
			
			DeleteOrgNode(target);
		}
		
		return ;
	}
	
	private void getAllChildrenNodesById(int pid, List<Organization> children) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = dao.GetOrgNodeByParentId( pid );
		if(res == null || res.size() == 0) {
			return;
		}
		else {
			children.addAll(res);
		}
			
		for(int i=0; i<res.size(); i++)
		{
			getAllChildrenNodesById(res.get(i).getId(), children);
		}
		return;
	}

	public TreeNode ConvertOrgToTreeNode(Organization org) throws Exception {
		TreeNode node = new TreeNode();
		String state = hasChild(org.getId()) ? "closed" : "open";
		node.setState(state);
		node.setId(org.getId());
		node.setText(org.getName());
		
		return node;
	}
	
	private boolean hasChild(int pid) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		return dao.OrgHasChild( pid );
	}

	public OrgListItem ConvertOrgToListItem(Organization org) throws Exception {
		OrgListItem item = new OrgListItem();
		item.setId(org.getId());
		item.setName(org.getName());
		item.setPid(org.getParent_id());
		item.setUid(org.getUid());
		item.setOrgLevel(org.getOrg_level());
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization parent = dao.GetOrgNodeById(org.getParent_id());
		item.setPname(parent == null ? "" : parent.getName());
		return item;
	}

	public List<Organization> QueryChildrenNodes(int id) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = dao.GetOrgNodeByParentId( id );
		return res;
	}

	public int QueryChildrenNodesCount(int id) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		int count = dao.GetOrgNodeCountByParentId( id );
		return count;
	}

	public int QueryAllChildrenNodes(int id, Organization condition, int page, int rows,
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
	
	public void queryAllChildrenNodesById(int pid, Organization condition, List<Organization> children) throws Exception
	{
		OrganizationDAO dao = new OrganizationDAOImpl();
		List<Organization> res = dao.GetOrgNodeByParentId( pid, condition );
		if(res == null || res.size() == 0) {
			return;
		}
		else {
			children.addAll(res);
		}
			
		for(int i=0; i<res.size(); i++)
		{
			queryAllChildrenNodesById(res.get(i).getId(), condition, children);
		}
		return;
	}

	public void ModifyOrgNodeName(int id, String orgName, String orgUid) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization node = dao.GetOrgNodeById(id);
		if(node == null) {
			throw new Exception("ID为" + id + "的机构结点不存在。");
		}
		
		node.setName(orgName);
		node.setUid(orgUid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		node.setTstamp(timenow);
		node = dao.OrgNodeAdd(node);
		return;
	}
	
	public String QueryNodePath(int nodeid) throws Exception
	{
		String name = "";
		OrganizationDAO dao = new OrganizationDAOImpl();
		Organization res = null;
		do 
		{
			res = dao.GetOrgNodeById( nodeid );
			if(res != null ) {
				name = res.getName() + "/" + name;
				nodeid = res.getParent_id();
			}
		}while( res != null);
		
		if(name.length() > 0) {
			name = name.substring(0, name.length()-1);
		}
		return name;
	}
}

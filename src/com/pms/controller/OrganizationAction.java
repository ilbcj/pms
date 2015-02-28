package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.OrgListItem;
import com.pms.dto.TreeNode;
import com.pms.model.Organization;
import com.pms.service.OrgManageService;

@SuppressWarnings("serial")
public class OrganizationAction extends ActionSupport {
	private List<Organization> bureauNodes;
	private List<Organization> childrenNodes;
	private List<TreeNode> treeNodes;
	private Organization orgNode;
	private int total;
	private List<OrgListItem> rows;
	private int id;
	private boolean result;
	private String message;
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<OrgListItem> getRows() {
		return rows;
	}

	public void setRows(List<OrgListItem> rows) {
		this.rows = rows;
	}

	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Organization> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<Organization> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Organization getOrgNode() {
		return orgNode;
	}

	public void setOrgNode(Organization orgNode) {
		this.orgNode = orgNode;
	}

	public List<Organization> getBureauNodes() {
		return bureauNodes;
	}

	public void setBureauNodes(List<Organization> bureauNodes) {
		this.bureauNodes = bureauNodes;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
//public actions
	public String QueryAllBureauNode()
	{
		OrgManageService oms = new OrgManageService();
		try {
			bureauNodes = oms.QueryAllBureauNode();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveOrgNode()
	{
		OrgManageService oms = new OrgManageService();
		try {
			orgNode = oms.SaveOrgNode(orgNode);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryChildrenNodes()
	{
		OrgManageService oms = new OrgManageService();
		try {
			//childrenNodes = oms.QueryChildrenNodes(orgNode.getParent_id());
			treeNodes = new ArrayList<TreeNode>();
			childrenNodes = oms.QueryChildrenNodes( id );
			TreeNode node = null;
			for(int i=0; i<childrenNodes.size(); i++) {
				node = oms.ConvertOrgToTreeNode(childrenNodes.get(i));
				this.treeNodes.add(node);
			}
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryChildrenItems()
	{
		OrgManageService oms = new OrgManageService();
		try {
			rows = new ArrayList<OrgListItem>();
			childrenNodes = oms.QueryChildrenNodes( id );
			OrgListItem node = null;
			for(int i=0; i<childrenNodes.size(); i++) {
				node = oms.ConvertOrgToListItem(childrenNodes.get(i));
				this.rows.add(node);
			}
			total = rows.size();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteOrgNode()
	{
		OrgManageService oms = new OrgManageService();
		try {
			oms.DeleteOrgNode(orgNode);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

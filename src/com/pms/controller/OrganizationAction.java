package com.pms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.OrgListItem;
import com.pms.dto.TreeNode;
import com.pms.model.Organization;
import com.pms.service.OrgManageService;
import com.pms.service.OrgUploadService;

@SuppressWarnings("serial")
public class OrganizationAction extends ActionSupport {
	private List<Organization> bureauNodes;
	private List<Organization> childrenNodes;
	private List<TreeNode> treeNodes;
	private Organization orgNode;
	private int page;
	private int rows;
	private int total;
	private List<OrgListItem> items;
	private String id;
	private List<String> delNodeIds;
	private boolean queryAll;
	private String orgName;
	private String orgUid;
	private String orgLevel;
	private boolean result;
	private String message;
	private String checkresult;
	
	private File fi;
	private String fiFileName;
	private String fiContentType;
	
	public List<String> getDelNodeIds() {
		return delNodeIds;
	}

	public void setDelNodeIds(List<String> delNodeIds) {
		this.delNodeIds = delNodeIds;
	}

	public boolean isQueryAll() {
		return queryAll;
	}

	public void setQueryAll(boolean queryAll) {
		this.queryAll = queryAll;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgUid() {
		return orgUid;
	}

	public void setOrgUid(String orgUid) {
		this.orgUid = orgUid;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<OrgListItem> getItems() {
		return items;
	}

	public void setItems(List<OrgListItem> items) {
		this.items = items;
	}

	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
	
	public String getCheckresult() {
		return checkresult;
	}

	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}

	public File getFi() {
		return fi;
	}

	public void setFi(File fi) {
		this.fi = fi;
	}

	public String getFiFileName() {
		return fiFileName;
	}

	public void setFiFileName(String fiFileName) {
		this.fiFileName = fiFileName;
	}

	public String getFiContentType() {
		return fiContentType;
	}

	public void setFiContentType(String fiContentType) {
		this.fiContentType = fiContentType;
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
	
	public String CheckUid()
	{
		OrgManageService oms = new OrgManageService();
		try {
			orgNode = oms.CheckUid(orgUid);
			if(null != orgNode){
				this.checkresult = "err";
			}else{
				this.checkresult = "ok";
			}
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
	
	public String ModifyOrgNodeName()
	{
		OrgManageService oms = new OrgManageService();
		try {
			//oms.ModifyOrgNodeName(id, orgName, orgUid);
			oms.ModifyOrgNodeName(id, orgName);
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
			if( id == null || id.length() == 0) {
				id = "0";
			}
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
		items = new ArrayList<OrgListItem>();
		if( id == null || id.length() == 0) {
			id = "0";
		}
		
		try {
			if( queryAll ) {
				System.out.println(orgName);
				//orgName = new String(orgName.getBytes("ISO8859-1"), "UTF-8");
//				orgName = java.net.URLDecoder.decode(orgName,"UTF-8");
				System.out.println(orgLevel);
				Organization condition = new Organization();
				condition.setUNIT(orgName);
//				condition.setUid(orgUid);
				condition.setORG_LEVEL(orgLevel);
				total = oms.QueryAllChildrenNodes( id, condition, page, rows, items );
			} else {
				
				total = oms.QueryChildrenNodes( id, page, rows, items );
			}
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
			oms.DeleteOrgNodes(delNodeIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	public String FileUploadOrg(){
		if(fi.length()==0){
			System.out.println("上传文件长度为0");
			setResult(true);
			return SUCCESS;
		}
		
		try {
			OrgUploadService ous = new OrgUploadService();
			ous.UploadOrg(fi);
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			return SUCCESS;
		}

		setResult(true);
		return SUCCESS;
		
	}
	
}

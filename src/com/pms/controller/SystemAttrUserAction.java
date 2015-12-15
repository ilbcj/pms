package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.AttrDictItem;
import com.pms.dto.OrgListItem;
import com.pms.dto.TreeNode;
import com.pms.model.AttrDefinition;
import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.SyncConfig;
import com.pms.model.User;
import com.pms.service.AttrDictionaryService;
import com.pms.service.DataSyncService;
import com.pms.service.OrgManageService;
import com.pms.service.SyncConfigService;

public class SystemAttrUserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7175852911429163884L;
	
	private boolean result;
	private String message;
	//private boolean queryAll;
	private int page;
	private int rows;
	private int total;
	
	private AttrDictItem attrItem;
	private String attrName;
	private String attrCode;
	private String orgName;
	private String orgUid;
	private String id;
	private int syncId;
	private String syncDest;
	private ArrayList<AttrDictItem> items;
	private SyncConfig syncConfig;
	private ArrayList<SyncConfig> syncConfigs;
	private List<Organization> orgNode;
	private List<OrgListItem> orgItems;
	private List<TreeNode> treeNodes;
	private String amount;
	private List<ResData> resDatas;
	private List<User> users;
	private List<ResRole> roles;
	private List<ResRoleResource> resRoleResources;
	private String filePath;
	
	public AttrDictItem getAttrItem() {
		return attrItem;
	}
	public void setAttrItem(AttrDictItem attrItem) {
		this.attrItem = attrItem;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getAttrCode() {
		return attrCode;
	}
	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getSyncId() {
		return syncId;
	}
	public void setSyncId(int syncId) {
		this.syncId = syncId;
	}
	public String getSyncDest() {
		return syncDest;
	}
	public void setSyncDest(String syncDest) {
		this.syncDest = syncDest;
	}
	public ArrayList<AttrDictItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<AttrDictItem> items) {
		this.items = items;
	}
	public SyncConfig getSyncConfig() {
		return syncConfig;
	}
	public void setSyncConfig(SyncConfig syncConfig) {
		this.syncConfig = syncConfig;
	}
	public ArrayList<SyncConfig> getSyncConfigs() {
		return syncConfigs;
	}
	public void setSyncConfigs(ArrayList<SyncConfig> syncConfigs) {
		this.syncConfigs = syncConfigs;
	}
	public List<Organization> getOrgNode() {
		return orgNode;
	}
	public void setOrgNode(List<Organization> orgNode) {
		this.orgNode = orgNode;
	}
	public List<OrgListItem> getOrgItems() {
		return orgItems;
	}
	public void setOrgItems(List<OrgListItem> orgItems) {
		this.orgItems = orgItems;
	}
	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}
	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<ResData> getResDatas() {
		return resDatas;
	}
	public void setResDatas(List<ResData> resDatas) {
		this.resDatas = resDatas;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<ResRole> getRoles() {
		return roles;
	}
	public void setRoles(List<ResRole> roles) {
		this.roles = roles;
	}
	public List<ResRoleResource> getResRoleResources() {
		return resRoleResources;
	}
	public void setResRoleResources(List<ResRoleResource> resRoleResources) {
		this.resRoleResources = resRoleResources;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String QueryUserAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		items = new ArrayList<AttrDictItem>();
		try {
			
			AttrDefinition criteria = new AttrDefinition();
			criteria.setName(attrName);
			criteria.setCode(attrCode);

			total = ads.QueryAttrDictionaryOfUser( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryResourceDataAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		items = new ArrayList<AttrDictItem>();
		try {
			
			AttrDefinition criteria = new AttrDefinition();
			criteria.setName(attrName);
			criteria.setCode(attrCode);

			total = ads.QueryAttrDictionaryOfResourceData( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryResourceFeatureAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		items = new ArrayList<AttrDictItem>();
		try {
			
			AttrDefinition criteria = new AttrDefinition();
			criteria.setName(attrName);
			criteria.setCode(attrCode);
			
			total = ads.QueryAttrDictionaryOfResourceFeature( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryOrgAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		items = new ArrayList<AttrDictItem>();
		try {
			
			AttrDefinition criteria = new AttrDefinition();
			criteria.setName(attrName);
			criteria.setCode(attrCode);

			total = ads.QueryAttrDictionaryOfOrg( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRoleAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		items = new ArrayList<AttrDictItem>();
		try {
			
			AttrDefinition criteria = new AttrDefinition();
			criteria.setName(attrName);
			criteria.setCode(attrCode);

			total = ads.QueryAttrDictionaryOfRole( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveUserAttrs()
	{
		AttrDictionaryService ads = new AttrDictionaryService();
		try {
			ads.SaveAttrDictionary(attrItem);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DataSyncRes()
	{
		DataSyncService dss = new DataSyncService();
		resDatas = new ArrayList<ResData>();
		try {
			filePath = dss.DownLoadRes( amount, resDatas);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DataSyncOrg()
	{
		DataSyncService dss = new DataSyncService();
		orgNode = new ArrayList<Organization>();
		try {
			filePath = dss.DownLoadOrg( amount, orgNode );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DataSyncUser()
	{
		DataSyncService dss = new DataSyncService();
		users = new ArrayList<User>();
		try {
			filePath = dss.DownLoadUser( amount, users );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DataSyncResRole()
	{
		DataSyncService dss = new DataSyncService();
		resRoleResources = new ArrayList<ResRoleResource>();
		try {
			filePath = dss.DownLoadResRole( amount, resRoleResources );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String DataSyncRole()
	{
		DataSyncService dss = new DataSyncService();
		roles = new ArrayList<ResRole>();
		try {
			filePath = dss.DownLoadRole( amount, roles );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QuerySyncConfigItems()
	{
		SyncConfigService sss = new SyncConfigService();
		 syncConfigs = new ArrayList<SyncConfig>();
		try {
			SyncConfig criteria = new SyncConfig();
			criteria.setId(syncId);
			criteria.setCLUE_DST_SYS(syncDest);
			total = sss.QueryAllSyncConfigItems( criteria, page, rows, syncConfigs );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveSyncConfigItems()
	{
		SyncConfigService sss = new SyncConfigService();
		try {
			sss.SaveSyncConfig(syncConfig);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QuerySyncConfigOrg()
	{
		SyncConfigService sss = new SyncConfigService();
		orgItems = new ArrayList<OrgListItem>();
		try {
			Organization criteria = new Organization();
			criteria.setUNIT(orgName);
			total = sss.QuerySyncConfigOrg( criteria, page, rows, orgItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QuerySyncConfigOrgNodes()
	{
		OrgManageService oms = new OrgManageService();
		SyncConfigService sss = new SyncConfigService();
		try {
			treeNodes = new ArrayList<TreeNode>();
			if( id == null || id.length() == 0) {
				id = "0";
			}
			orgNode = sss.QuerySyncConfigOrg( id );
			TreeNode node = null;
			for(int i=0; i<orgNode.size(); i++) {
				node = oms.ConvertOrgToTreeNode(orgNode.get(i));
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
}

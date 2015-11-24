package com.pms.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.ResDataListItem;
import com.pms.dto.ResDataTemplateListItem;
import com.pms.dto.RoleListItem;
import com.pms.dto.TreeNode;
import com.pms.model.ResData;
import com.pms.model.ResDataOrg;
import com.pms.model.ResDataTemplate;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.service.ResourceManageService;
import com.pms.service.ResourceUploadService;

public class ResourceAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2406858103501091573L;
	
	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private String resName;
	private String resCode;
	private String reSystemtType;
	private String resAppid;
	private ResFeature feature;
	private List<Integer> delIds;
	private List<ResFeature> features;
	private ResData data;
	private ResDataOrg resDataOrg;
	private List<ResData> datas;
	private ResRole role;
	private ResRoleOrg resRoleOrg;
	private List<ResRole> roles;
	private List<String> addFeatureIds;
	private List<String> addDataIds;
	private List<ResDataListItem> dataItems;
	private List<ResDataTemplateListItem> dataTemplateItems;
	private List<RoleListItem> roleItems;
	
	private String resource_id;
	private List<String> resource_status;
	private String resource_describe;
	private String resource_remark;
	private List<String> delete_status;
	private List<String> resource_type;
	private String clue_dst_sys;
	private List<String> dataset_sensitive_level;
	private List<String> data_set;
	private List<String> section_class;
	private List<String> element;
	private List<String> section_relatioin_class;
	
	private List<TreeNode> treeNodes;
	private List<ResFeature> childrenNodes;
	private String id;
	
	private File fi;
	private String fiFileName;
	private String fiContentType;
	

	public List<ResDataListItem> getDataItems() {
		return dataItems;
	}

	public void setDataItems(List<ResDataListItem> dataItems) {
		this.dataItems = dataItems;
	}

	public List<ResDataTemplateListItem> getDataTemplateItems() {
		return dataTemplateItems;
	}

	public void setDataTemplateItems(List<ResDataTemplateListItem> dataTemplateItems) {
		this.dataTemplateItems = dataTemplateItems;
	}

	public List<RoleListItem> getRoleItems() {
		return roleItems;
	}

	public void setRoleItems(List<RoleListItem> roleItems) {
		this.roleItems = roleItems;
	}

	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resourceId) {
		resource_id = resourceId;
	}

	public List<String> getResource_status() {
		return resource_status;
	}

	public void setResource_status(List<String> resourceStatus) {
		resource_status = resourceStatus;
	}

	public String getResource_describe() {
		return resource_describe;
	}

	public void setResource_describe(String resourceDescribe) {
		resource_describe = resourceDescribe;
	}

	public String getResource_remark() {
		return resource_remark;
	}

	public void setResource_remark(String resourceRemark) {
		resource_remark = resourceRemark;
	}

	public List<String> getDelete_status() {
		return delete_status;
	}

	public void setDelete_status(List<String> deleteStatus) {
		delete_status = deleteStatus;
	}

	public List<String> getResource_type() {
		return resource_type;
	}

	public void setResource_type(List<String> resourceType) {
		resource_type = resourceType;
	}

	public String getClue_dst_sys() {
		return clue_dst_sys;
	}

	public void setClue_dst_sys(String clueDstSys) {
		clue_dst_sys = clueDstSys;
	}

	public List<String> getDataset_sensitive_level() {
		return dataset_sensitive_level;
	}

	public void setDataset_sensitive_level(List<String> datasetSensitiveLevel) {
		dataset_sensitive_level = datasetSensitiveLevel;
	}

	public List<String> getData_set() {
		return data_set;
	}

	public void setData_set(List<String> dataSet) {
		data_set = dataSet;
	}

	public List<String> getSection_class() {
		return section_class;
	}

	public void setSection_class(List<String> sectionClass) {
		section_class = sectionClass;
	}

	public List<String> getElement() {
		return element;
	}

	public void setElement(List<String> element) {
		this.element = element;
	}

	public List<String> getSection_relatioin_class() {
		return section_relatioin_class;
	}

	public void setSection_relatioin_class(List<String> sectionRelatioinClass) {
		section_relatioin_class = sectionRelatioinClass;
	}

	public List<String> getAddFeatureIds() {
		return addFeatureIds;
	}

	public void setAddFeatureIds(List<String> addFeatureIds) {
		this.addFeatureIds = addFeatureIds;
	}

	public List<String> getAddDataIds() {
		return addDataIds;
	}

	public void setAddDataIds(List<String> addDataIds) {
		this.addDataIds = addDataIds;
	}

	public ResRole getRole() {
		return role;
	}

	public void setRole(ResRole role) {
		this.role = role;
	}

	public ResRoleOrg getResRoleOrg() {
		return resRoleOrg;
	}

	public void setResRoleOrg(ResRoleOrg resRoleOrg) {
		this.resRoleOrg = resRoleOrg;
	}

	public List<ResRole> getRoles() {
		return roles;
	}

	public void setRoles(List<ResRole> roles) {
		this.roles = roles;
	}

	public ResData getData() {
		return data;
	}

	public void setData(ResData data) {
		this.data = data;
	}

	public ResDataOrg getResDataOrg() {
		return resDataOrg;
	}

	public void setResDataOrg(ResDataOrg resDataOrg) {
		this.resDataOrg = resDataOrg;
	}

	public List<ResData> getDatas() {
		return datas;
	}

	public void setDatas(List<ResData> datas) {
		this.datas = datas;
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

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getReSystemtType() {
		return reSystemtType;
	}

	public void setReSystemtType(String reSystemtType) {
		this.reSystemtType = reSystemtType;
	}

	public String getResAppid() {
		return resAppid;
	}

	public void setResAppid(String resAppid) {
		this.resAppid = resAppid;
	}

	public ResFeature getFeature() {
		return feature;
	}

	public void setFeature(ResFeature feature) {
		this.feature = feature;
	}

	public List<Integer> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<Integer> delIds) {
		this.delIds = delIds;
	}

	public List<ResFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<ResFeature> features) {
		this.features = features;
	}

	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}

	public List<ResFeature> getChildrenNodes() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<ResFeature> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	
	public String QueryFeatureItems()
	{
		ResourceManageService rms = new ResourceManageService();
		features = new ArrayList<ResFeature>();
		if( id == null || id.length() == 0) {
			id = "0";
		}
		try {

				ResFeature criteria = new ResFeature();
				criteria.setRESOUCE_NAME(resName);
				criteria.setRESOURCE_ID(resCode);
				criteria.setSYSTEM_TYPE(reSystemtType);
				criteria.setAPP_ID(resAppid);
				total = rms.QueryAllFeatureItems( id, criteria, page, rows, features );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryFeatureChildrenNodes()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			treeNodes = new ArrayList<TreeNode>();
			if( id == null || id.length() == 0) {
				id = "0";
			}
			ResFeature criteria = new ResFeature();
			criteria.setSYSTEM_TYPE(reSystemtType);
			criteria.setAPP_ID(resAppid);
			childrenNodes = rms.QueryFeatureChildrenNodes( id ,criteria);
			TreeNode node = null;
			for(int i=0; i<childrenNodes.size(); i++) {
				node = rms.ConvertFeatureToTreeNode(childrenNodes.get(i));
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

	public String SaveResourceFeature() throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceManageService rms = new ResourceManageService();
			feature = rms.SaveResourceFeature(feature);
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			msg.put("message", message);
			return null;
		} finally {
			msg.put("result", result);
			json = JSONObject.fromObject(msg).toString();
			htmlout.print(json);
			htmlout.flush();
			htmlout.close();
		}
	}
	
	public String DeleteResourceFeatures()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			rms.DeleteResourceFeatures(delIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryDataItems()
	{
		ResourceManageService rms = new ResourceManageService();
		dataItems = new ArrayList<ResDataListItem>();
		try {
			ResData criteria = new ResData();
			criteria.setName(resName);
			criteria.setRESOURCE_ID(resCode);
//			criteria.setRESOURCE_ID(resource_id);
			criteria.setRESOURCE_DESCRIBE(resource_describe);
			criteria.setRMK(resource_remark);
			total = rms.QueryAllDataItems(resource_status, resource_type, dataset_sensitive_level,
					data_set, section_class, element,section_relatioin_class, 
					criteria, page, rows, dataItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryDataTemplateItems()
	{
		ResourceManageService rms = new ResourceManageService();
		dataTemplateItems = new ArrayList<ResDataTemplateListItem>();
		try {
			ResDataTemplate criteria = new ResDataTemplate();
			criteria.setName(resName);
			criteria.setRESOURCE_ID(resCode);
			criteria.setRESOURCE_DESCRIBE(resource_describe);
			criteria.setRMK(resource_remark);
			total = rms.QueryAllDataTemplateItems(resource_status, resource_type, dataset_sensitive_level,
					data_set, section_class, element,section_relatioin_class, 
					criteria, page, rows, dataTemplateItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String SaveResourceData() throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceManageService rms = new ResourceManageService();
			data = rms.SaveResourceData(data, resDataOrg);
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			msg.put("message", message);
			return null;
		} finally {
			msg.put("result", result);
			json = JSONObject.fromObject(msg).toString();
			htmlout.print(json);
			htmlout.flush();
			htmlout.close();
		}
	}
	
	public String DeleteResourceDatas()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			rms.DeleteResourceDatas(delIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRoleItems()
	{
		ResourceManageService rms = new ResourceManageService();
		roleItems = new ArrayList<RoleListItem>();
		try {

			ResRole criteria = new ResRole();
			criteria.setBUSINESS_ROLE_NAME(resName);
			criteria.setBUSINESS_ROLE(resCode);
			total = rms.QueryAllRoleItems( criteria, page, rows, roleItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String SaveResourceRole()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			role = rms.SaveResourceRole(role, resRoleOrg, this.addFeatureIds, this.addDataIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteResourceRole()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			rms.DeleteResourceRoles(delIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRoleResource()
	{
		ResourceManageService rms = new ResourceManageService();
		features = new ArrayList<ResFeature>();
		dataItems = new ArrayList<ResDataListItem>();
//		dataTemplateItems = new ArrayList<ResDataTemplateListItem>();
		try {
			rms.QueryRoleResource( role.getBUSINESS_ROLE(), features, dataItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String FileUploadResourceData() throws IOException{
		
//		System.out.println("文件的名称："+fiFileName);
//		System.out.println("文件的类型："+fiContentType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
//			String path=ServletActionContext.getServletContext().getRealPath("")+"/upload/"+fiFileName;
//			System.out.println(path);
//			InputStream in=new FileInputStream(fi);
//			OutputStream out=new FileOutputStream(new File(path));
//			byte b[]=new byte[1024*1024*5];
//			int i=0;
//			while((i=in.read(b))!=-1) {
//				out.write(b, 0, i);
//			}
//			out.flush();
//			out.close();
			
			ResourceUploadService rus = new ResourceUploadService();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadResourceData(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			msg.put("message", message);
			return null;
		} finally {
			msg.put("result", result);
			json = JSONObject.fromObject(msg).toString();
			htmlout.print(json);
			htmlout.flush();
			htmlout.close();
		}
	}
	
	public String FileUploadResourceFeature() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadService rus = new ResourceUploadService();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadResourceFeature(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			msg.put("message", message);
			return null;
		} finally {
			msg.put("result", result);
			json = JSONObject.fromObject(msg).toString();
			htmlout.print(json);
			htmlout.flush();
			htmlout.close();
		}
	}
	
	public String FileUploadRole() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadService rus = new ResourceUploadService();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadResourceRole(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入文件失败。" + e.getMessage());
			msg.put("message", message);
			return null;
		} finally {
			msg.put("result", result);
			json = JSONObject.fromObject(msg).toString();
			htmlout.print(json);
			htmlout.flush();
			htmlout.close();
		}
	}
}

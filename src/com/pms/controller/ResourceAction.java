package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.ResData;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.service.ResourceManageService;

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
	private ResFeature feature;
	private List<Integer> delIds;
	private List<ResFeature> features;
	private ResData data;
	private List<ResData> datas;
	private ResRole role;
	private List<ResRole> roles;
	private List<Integer> addFeatureIds;
	private List<Integer> addDataIds;
	
	private String resource_id;
	private List<Integer> resource_status;
	private String resource_describe;
	private String resource_remark;
	private List<Integer> delete_status;
	private List<Integer> resource_type;
	private String clue_dst_sys;
	private List<Integer> dataset_sensitive_level;
	private List<Integer> data_set;
	private List<Integer> section_class;
	private List<Integer> lement;
	private List<Integer> section_relation_class;

	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resourceId) {
		resource_id = resourceId;
	}

	public List<Integer> getResource_status() {
		return resource_status;
	}

	public void setResource_status(List<Integer> resourceStatus) {
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

	public List<Integer> getDelete_status() {
		return delete_status;
	}

	public void setDelete_status(List<Integer> deleteStatus) {
		delete_status = deleteStatus;
	}

	public List<Integer> getResource_type() {
		return resource_type;
	}

	public void setResource_type(List<Integer> resourceType) {
		resource_type = resourceType;
	}

	public String getClue_dst_sys() {
		return clue_dst_sys;
	}

	public void setClue_dst_sys(String clueDstSys) {
		clue_dst_sys = clueDstSys;
	}

	public List<Integer> getDataset_sensitive_level() {
		return dataset_sensitive_level;
	}

	public void setDataset_sensitive_level(List<Integer> datasetSensitiveLevel) {
		dataset_sensitive_level = datasetSensitiveLevel;
	}

	public List<Integer> getData_set() {
		return data_set;
	}

	public void setData_set(List<Integer> dataSet) {
		data_set = dataSet;
	}

	public List<Integer> getSection_class() {
		return section_class;
	}

	public void setSection_class(List<Integer> sectionClass) {
		section_class = sectionClass;
	}

	public List<Integer> getLement() {
		return lement;
	}

	public void setLement(List<Integer> lement) {
		this.lement = lement;
	}

	public List<Integer> getSection_relation_class() {
		return section_relation_class;
	}

	public void setSection_relation_class(List<Integer> sectionRelationClass) {
		section_relation_class = sectionRelationClass;
	}

	public List<Integer> getAddFeatureIds() {
		return addFeatureIds;
	}

	public void setAddFeatureIds(List<Integer> addFeatureIds) {
		this.addFeatureIds = addFeatureIds;
	}

	public List<Integer> getAddDataIds() {
		return addDataIds;
	}

	public void setAddDataIds(List<Integer> addDataIds) {
		this.addDataIds = addDataIds;
	}

	public ResRole getRole() {
		return role;
	}

	public void setRole(ResRole role) {
		this.role = role;
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

	public String QueryFeatureItems()
	{
		ResourceManageService rms = new ResourceManageService();
		features = new ArrayList<ResFeature>();
		try {

				ResFeature criteria = new ResFeature();
				criteria.setName(resName);
				criteria.setResource_id(resCode);
				total = rms.QueryAllFeatureItems( criteria, page, rows, features );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String SaveResourceFeature()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			feature = rms.SaveResourceFeature(feature);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
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
		datas = new ArrayList<ResData>();
		try {

				ResData criteria = new ResData();
				criteria.setName(resName);
				criteria.setResource_id(resCode);
				total = rms.QueryAllDataItems( criteria, page, rows, datas );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String SaveResourceData()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			data = rms.SaveResourceData(data);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
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
		roles = new ArrayList<ResRole>();
		try {

			ResRole criteria = new ResRole();
			criteria.setBusiness_role_name(resName);
			criteria.setBusiness_role(resCode);
			total = rms.QueryAllRoleItems( criteria, page, rows, roles );
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
			role = rms.SaveResourceRole(role, this.addFeatureIds, this.addDataIds);
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
		datas = new ArrayList<ResData>();
		try {
			rms.QueryRoleResource( role.getId(), features, datas );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

	public String QueryRoleResourceTest()
	{		
		System.out.println(resource_id);		
		for (int i = 0; i < resource_status.size(); i++) {
			System.out.println(resource_status.get(i));
		}
//		ResourceManageService rms = new ResourceManageService();
//		features = new ArrayList<ResFeature>();
//		datas = new ArrayList<ResData>();
		try {
//			rms.QueryRoleResource( role.getId(), features, datas );		
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

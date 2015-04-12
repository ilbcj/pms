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
}

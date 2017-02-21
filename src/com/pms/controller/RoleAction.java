/**   
 * @ClassName:     ${ResourceAction}   
 * @Description:   ${资源管理control}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.8.12} 
*/
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
import com.pms.dto.ResRoleResourceTemplateListItem;
import com.pms.dto.RoleListItem;
import com.pms.model.ResFeature;
import com.pms.model.ResRole;
import com.pms.model.ResRoleOrg;
import com.pms.model.ResRoleResourceTemplate;
import com.pms.service.ResourceManageService;
import com.pms.service.ResourceUploadServiceV2;

public class RoleAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7765624147856825267L;
	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private String resName;
	private String resCode;
	private List<String> delIds;
	private List<ResFeature> features;
	private ResRole role;
	private ResRoleOrg resRoleOrg;
	private List<String> addFeatureIds;
	private List<String> addDataIds;
	private List<String> delFeatureIds;
	private List<String> delDataIds;
	private List<ResDataListItem> dataItems;
	private List<RoleListItem> roleItems;
	private ResRoleResourceTemplate resRoleResourceTemplate;
	private List<ResRoleResourceTemplate> resRoleResourceTemplates;
	private List<ResRoleResourceTemplateListItem> resRoleResourceTemplateListItems;
	
	private File fi;
	private String fiFileName;
	private String fiContentType;
	
	
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

	public List<String> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<String> delIds) {
		this.delIds = delIds;
	}

	public List<ResFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<ResFeature> features) {
		this.features = features;
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

	public List<String> getDelFeatureIds() {
		return delFeatureIds;
	}

	public void setDelFeatureIds(List<String> delFeatureIds) {
		this.delFeatureIds = delFeatureIds;
	}

	public List<String> getDelDataIds() {
		return delDataIds;
	}

	public void setDelDataIds(List<String> delDataIds) {
		this.delDataIds = delDataIds;
	}

	public List<ResDataListItem> getDataItems() {
		return dataItems;
	}

	public void setDataItems(List<ResDataListItem> dataItems) {
		this.dataItems = dataItems;
	}

	public List<RoleListItem> getRoleItems() {
		return roleItems;
	}

	public void setRoleItems(List<RoleListItem> roleItems) {
		this.roleItems = roleItems;
	}

	public ResRoleResourceTemplate getResRoleResourceTemplate() {
		return resRoleResourceTemplate;
	}

	public void setResRoleResourceTemplate(
			ResRoleResourceTemplate resRoleResourceTemplate) {
		this.resRoleResourceTemplate = resRoleResourceTemplate;
	}

	public List<ResRoleResourceTemplate> getResRoleResourceTemplates() {
		return resRoleResourceTemplates;
	}

	public void setResRoleResourceTemplates(
			List<ResRoleResourceTemplate> resRoleResourceTemplates) {
		this.resRoleResourceTemplates = resRoleResourceTemplates;
	}

	public List<ResRoleResourceTemplateListItem> getResRoleResourceTemplateListItems() {
		return resRoleResourceTemplateListItems;
	}

	public void setResRoleResourceTemplateListItems(
			List<ResRoleResourceTemplateListItem> resRoleResourceTemplateListItems) {
		this.resRoleResourceTemplateListItems = resRoleResourceTemplateListItems;
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
			role = rms.SaveResourceRole(role, resRoleOrg, this.addFeatureIds, this.addDataIds, this.delDataIds, this.delFeatureIds);
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
	
	public String QueryRoleResourceData()
	{
		ResourceManageService rms = new ResourceManageService();
		dataItems = new ArrayList<ResDataListItem>();
		try {
			total = rms.QueryRoleResourceData( role.getBUSINESS_ROLE(), page, rows, dataItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRoleResourceFunc()
	{
		ResourceManageService rms = new ResourceManageService();
		features = new ArrayList<ResFeature>();
		try {
			total = rms.QueryRoleResourceFunc( role.getBUSINESS_ROLE(), page, rows, features );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryResRoleResourceTemplate()
	{
		ResourceManageService rms = new ResourceManageService();
		resRoleResourceTemplateListItems = new ArrayList<ResRoleResourceTemplateListItem>();
		try {
			total = rms.QueryResRoleResourceTemplate(resRoleResourceTemplate, page, rows, resRoleResourceTemplateListItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveResRoleResourceTemplate()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			resRoleResourceTemplate = rms.SaveResRoleResourceTemplate(resRoleResourceTemplate, this.addFeatureIds, this.addDataIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryAllResRoleResourceTemplate()
	{
		ResourceManageService rms = new ResourceManageService();
		resRoleResourceTemplates = new ArrayList<ResRoleResourceTemplate>();
		try {
			
			total = rms.QueryAllResRoleResourceTemplate(page, rows, resRoleResourceTemplates );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteResRoleResourceTemplate()
	{
		ResourceManageService rms = new ResourceManageService();
		try {
			rms.DeleteRoleResourceTemplate(resRoleResourceTemplate);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRoleResourceTemplate()
	{
		ResourceManageService rms = new ResourceManageService();
		features = new ArrayList<ResFeature>();
		dataItems = new ArrayList<ResDataListItem>();
		try {
			rms.QueryRoleResourceTemplate(resRoleResourceTemplate, features, dataItems );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
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
			ResourceUploadServiceV2 rus = new ResourceUploadServiceV2();
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
	
	public String FileUploadUser() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadServiceV2 rus = new ResourceUploadServiceV2();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadWZUser(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入用户信息失败。" + e.getMessage());
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
	
	public String FileUploadPrivateRole() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadServiceV2 rus = new ResourceUploadServiceV2();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadWZRole(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入角色信息失败。" + e.getMessage());
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
		
	public String FileUploadPrivateRoleUserRelation() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadServiceV2 rus = new ResourceUploadServiceV2();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadWZRoleUserRelation(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入角色用户关系数据失败。" + e.getMessage());
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
	
	public String FileUploadPrivateRoleFuncRelation() throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter htmlout = response.getWriter();
		String json = "";
		HashMap<String, Object> msg = new HashMap<String, Object>();  
		setResult(false);
		
		try {
			ResourceUploadServiceV2 rus = new ResourceUploadServiceV2();
			if(fi.length()==0){
				System.out.println("上传文件长度为0");
			} else {
				rus.UploadWZRoleFuncRelation(fi);
			}
			setResult(true);
			return null;
		} catch (Exception e) {
			setResult(false);
			this.setMessage("导入角色功能资源关系数据失败。" + e.getMessage());
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


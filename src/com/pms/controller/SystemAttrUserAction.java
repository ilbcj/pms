package com.pms.controller;

import java.util.ArrayList;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.AttrDictItem;
import com.pms.model.AttrDefinition;
import com.pms.service.AttrDictionaryService;
import com.pms.service.DataSyncService;

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
	private ArrayList<AttrDictItem> items;
	
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
	public ArrayList<AttrDictItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<AttrDictItem> items) {
		this.items = items;
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
		try {
			dss.DownLoadRes();
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
		try {
			dss.DownLoadOrg();
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
		try {
			dss.DownLoadUser();
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
		try {
			dss.DownLoadResRole();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
}

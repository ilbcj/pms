package com.pms.controller;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnClassify;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetSensitive;
import com.pms.model.ResRelationClassify;
import com.pms.model.ResValue;
import com.pms.service.DataResourceService;

public class DataResourceAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2936550599373351246L;
	
	private boolean result;
	private String message;
	
	private List<ResDataSet> resDataSet;
	private List<ResDataSetSensitive> resDataSetSensitive;
	private List<ResColumn> resColumn;
	private List<ResValue> resValue;
	private List<ResColumnClassify> resColumnClassify;
	private List<ResRelationClassify> resRelationClassify;
	private String dataSet;
	private String element;
	private String sectionClass;
	
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
	public List<ResDataSet> getResDataSet() {
		return resDataSet;
	}
	public void setResDataSet(List<ResDataSet> resDataSet) {
		this.resDataSet = resDataSet;
	}
	public List<ResDataSetSensitive> getResDataSetSensitive() {
		return resDataSetSensitive;
	}
	public void setResDataSetSensitive(List<ResDataSetSensitive> resDataSetSensitive) {
		this.resDataSetSensitive = resDataSetSensitive;
	}
	public List<ResColumn> getResColumn() {
		return resColumn;
	}
	public void setResColumn(List<ResColumn> resColumn) {
		this.resColumn = resColumn;
	}
	public List<ResColumnClassify> getResColumnClassify() {
		return resColumnClassify;
	}
	public void setResColumnClassify(List<ResColumnClassify> resColumnClassify) {
		this.resColumnClassify = resColumnClassify;
	}
	public List<ResRelationClassify> getResRelationClassify() {
		return resRelationClassify;
	}
	public void setResRelationClassify(List<ResRelationClassify> resRelationClassify) {
		this.resRelationClassify = resRelationClassify;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	public List<ResValue> getResValue() {
		return resValue;
	}
	public void setResValue(List<ResValue> resValue) {
		this.resValue = resValue;
	}
	public String getSectionClass() {
		return sectionClass;
	}
	public void setSectionClass(String sectionClass) {
		this.sectionClass = sectionClass;
	}
	public String QueryRowDataSets()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resDataSet = drs.QueryRowDataSet();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryDataSetSensitive()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resDataSetSensitive = drs.QueryDataSetSensitive(dataSet);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRowColumn()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resColumn = drs.QueryRowColumn(dataSet);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryRowResValue()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resValue = drs.QueryRowResValue(dataSet, element);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryColumnDataSets()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resDataSet = drs.QueryColumnDataSet();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryColumnClassify()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resColumnClassify = drs.QueryColumnClassify(dataSet);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryColumnColumn()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resColumn = drs.QueryColumnColumn(dataSet, sectionClass);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryClassifyDataSets()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resDataSet = drs.QueryClassifyDataSet();
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QeryResRelationClassify()
	{
		DataResourceService drs = new DataResourceService();
		try {
			resRelationClassify = drs.QueryResRelationClassify(dataSet);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}

}

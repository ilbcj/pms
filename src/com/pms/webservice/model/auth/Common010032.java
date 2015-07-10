package com.pms.webservice.model.auth;

import java.util.List;

import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;

public class Common010032 {
	private String sourceName;
	private String parentCondition;
	private List<Condition> subConditions;
	private List<Item> items;
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public String getParentCondition() {
		return parentCondition;
	}
	public void setParentCondition(String parentCondition) {
		this.parentCondition = parentCondition;
	}
	public List<Condition> getSubConditions() {
		return subConditions;
	}
	public void setSubConditions(List<Condition> subConditions) {
		this.subConditions = subConditions;
	}
}

package com.pms.webservice.model.auth;

import java.util.List;

import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;

public class Common010032 {
	private String sourceName;
	private String syncKey;
	private String parentCondition;
	private List<Condition> subConditions;
	private List<Item> subItems;
	private List<Item> items;
	private List<Item> whiteListItems;
	
	public List<Item> getWhiteListItems() {
		return whiteListItems;
	}
	public void setWhiteListItems(List<Item> whiteListItems) {
		this.whiteListItems = whiteListItems;
	}
	public String getSyncKey() {
		return syncKey;
	}
	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}
	public List<Item> getSubItems() {
		return subItems;
	}
	public void setSubItems(List<Item> subItems) {
		this.subItems = subItems;
	}
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

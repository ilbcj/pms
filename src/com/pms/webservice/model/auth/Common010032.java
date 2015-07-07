package com.pms.webservice.model.auth;

import java.util.List;

import com.pms.webservice.model.Condition;
import com.pms.webservice.model.Item;

public class Common010032 {
	private String sourceName;
	private List<Condition> conditions;
	private List<Item> items;
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
}

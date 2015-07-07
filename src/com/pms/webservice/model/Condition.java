package com.pms.webservice.model;

import java.util.List;

public class Condition {
	private String rel;
	private List<Item> items;
	
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
}

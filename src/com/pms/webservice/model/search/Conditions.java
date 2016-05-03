package com.pms.webservice.model.search;

import java.util.List;

import com.pms.webservice.model.Condition;

public class Conditions {
	//private String rel;
	private List<Condition> conditions;
	private Condition condition;
	
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
//	public String getRel() {
//		return rel;
//	}
//	public void setRel(String rel) {
//		this.rel = rel;
//	}
	public List<Condition> getConditions() {
		return conditions;
	}
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}
}

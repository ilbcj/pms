package com.pms.webservice.model.search;

import java.util.List;

import com.pms.webservice.model.Condition;

public class NestConditions {
	private Condition condition;
	private List<NestConditions> subConditions;
	
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	public List<NestConditions> getSubConditions() {
		return subConditions;
	}
	public void setSubConditions(List<NestConditions> subConditions) {
		this.subConditions = subConditions;
	}
}

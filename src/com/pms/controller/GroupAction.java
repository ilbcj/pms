package com.pms.controller;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pms.dto.UserListItem;
import com.pms.model.Group;
import com.pms.model.Rule;
import com.pms.service.GroupManageService;

public class GroupAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 683940319646526137L;

	private boolean result;
	private String message;
	private int page;
	private int rows;
	private int total;
	
	private String groupName;
	private String groupCode;
	private Group group;
	private List<Group> items;
	private List<Integer> addUserIds;
	private List<UserListItem> users;
	private List<Integer> delIds;
	private List<Rule> rules;
	
	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public List<Integer> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<Integer> delIds) {
		this.delIds = delIds;
	}

	public List<UserListItem> getUsers() {
		return users;
	}

	public void setUsers(List<UserListItem> users) {
		this.users = users;
	}

	public boolean isResult() {
		return result;
	}

	public List<Group> getItems() {
		return items;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public void setItems(List<Group> items) {
		this.items = items;
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


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}


	public List<Integer> getAddUserIds() {
		return addUserIds;
	}


	public void setAddUserIds(List<Integer> addUserIds) {
		this.addUserIds = addUserIds;
	}


	public String SaveGroupUser()
	{
		GroupManageService gms = new GroupManageService();
		try {
			group = gms.SaveGroupUser(group, this.addUserIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryGroupUserItems()
	{
		GroupManageService gms = new GroupManageService();
		items = new ArrayList<Group>();
		try {
			Group criteria = new Group();
			criteria.setName(groupName);
			criteria.setCode(groupCode);
			total = gms.QueryAllGroupUserItems( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryGroupUsers()
	{
		GroupManageService gms = new GroupManageService();
		try {
			users = gms.QueryGroupUsersByGroupId(group.getId());
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String DeleteGroups()
	{
		GroupManageService gms = new GroupManageService();
		try {
			gms.DeleteGroups(delIds);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String SaveGroupRule()
	{
		GroupManageService gms = new GroupManageService();
		try {
			group = gms.SaveGroupRule(group, rules);
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryGroupRuleItems()
	{
		GroupManageService gms = new GroupManageService();
		items = new ArrayList<Group>();
		try {
			Group criteria = new Group();
			criteria.setName(groupName);
			criteria.setCode(groupCode);
			total = gms.QueryAllGroupRuleItems( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryGroupRules()
	{
		GroupManageService gms = new GroupManageService();
		try {
			users = gms.QueryGroupRulesByGroupId(group.getId());
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
	
	public String QueryGroupItems()
	{
		GroupManageService gms = new GroupManageService();
		items = new ArrayList<Group>();
		try {
			Group criteria = new Group();
			criteria.setName(groupName);
			criteria.setCode(groupCode);
			total = gms.QueryAllGroupUserItems( criteria, page, rows, items );
		} catch (Exception e) {
			message = e.getMessage();
			setResult(false);
			return SUCCESS;
		}
		setResult(true);
		return SUCCESS;
	}
}

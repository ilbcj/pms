package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Hibernate;

import com.pms.dao.AttributeDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.GroupDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.GroupDAOImpl;
import com.pms.dto.RuleListItem;
import com.pms.dto.UserListItem;
import com.pms.model.AttrDictionary;
import com.pms.model.AuditGroupLog;
import com.pms.model.AuditGroupLogDescribe;
import com.pms.model.Group;
import com.pms.model.Rule;
import com.pms.model.User;

public class GroupManageService {

	public Group SaveGroupUser(Group group, List<String> userIds) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		group.setTstamp(timenow);
		group.setType(Group.GROUPTYPEUSER);
		
		AddGroupAddOrUpdateLog(group, null);
		
		group = dao.GroupAdd(group);
		
		dao.UpdateGroupUsers(group.getCode(), userIds);
		return group;
	}

	public int QueryAllGroupUserItems(Group criteria, int page, int rows,
			List<Group> items) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		List<Group> res = dao.GetGroupUsers( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllGroupsUsersCount( criteria );
		
		AddGroupQueryLog(criteria);
		return total;
	}

	private int QueryAllGroupsUsersCount(Group criteria) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		int count = dao.GetGroupUsersCount( criteria );
		return count;
	}

	public List<UserListItem> QueryGroupUsersByGroupId(String id) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		List<User> users = dao.GetGroupUsersByGroupId( id );
		
		List<UserListItem> res = new ArrayList<UserListItem>();
		UserManageService ums = new UserManageService();
		UserListItem userItem = null;
		for(int i=0; i<users.size(); i++) {
			userItem = ums.ConvertUserToListItem(users.get(i));
			res.add(userItem);
		}
		
		return res;
	}

	public void DeleteGroupUsers(List<Integer> groupIds) throws Exception {
		if(groupIds == null)
			return;
		
		Group target;
		GroupDAO dao = new GroupDAOImpl();
		for(int i = 0; i< groupIds.size(); i++) {
			target = new Group();
			target.setId(groupIds.get(i));
			
			AddGroupDelLog(target);
			
			dao.GroupOfUserDel(target);
		}
		
		return ;		
	}
	
	public int QueryAllGroupItems(Group criteria, int page, int rows,
			List<Group> items) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		List<Group> res = dao.GetAllGroups( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllGroupsUsersCount( criteria );
		return total;
	}

	public Group SaveGroupRule(Group group, List<Rule> rules) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		group.setTstamp(timenow);
		group.setType(Group.GROUPTYPERULE);
		
		AddGroupAddOrUpdateLog(group, rules);
		
		group = dao.GroupAdd(group);
		
		dao.UpdateGroupRules(group.getCode(), rules);
		return group;
	}

	public int QueryAllGroupRuleItems(Group criteria, int page, int rows,
			List<Group> items) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		List<Group> res = dao.GetGroupRules( criteria, page, rows );
		items.addAll(res);
		int total = QueryAllGroupsRulesCount( criteria );
		
		AddGroupQueryLog(criteria);
		
		return total;
	}
	
	private int QueryAllGroupsRulesCount(Group criteria) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		int count = dao.GetGroupRulesCount( criteria );
		return count;
	}

	public void QueryGroupRulesByGroupId(String id, List<RuleListItem> items) throws Exception {
		GroupDAO dao = new GroupDAOImpl();
		List<Rule> rules = dao.GetGroupRulesByGroupId( id );
		
		RuleListItem ruleListItem = null;
		for (int i = 0; i < rules.size(); i++) {
			 ruleListItem = ConvertRuleToListItem(rules.get(i));
			 items.add(ruleListItem);
		}
	}
	
	public RuleListItem ConvertRuleToListItem(Rule rule) throws Exception {
		RuleListItem item = new RuleListItem();
		item.setId(rule.getId());
		item.setAttrid(rule.getAttrid());
		item.setGroupid(rule.getGroupid());
		item.setRulename(rule.getRulename());
		AttributeDAO attrdao = new AttributeDAOImpl();
		String value="";
		String str[]=rule.getRulevalue().split(",");
		for(int j=0; j<str.length; j++) {     
			AttrDictionary attr = attrdao.GetAttrDictionarysByAttrIdAndCode(rule.getAttrid(),str[j]);
			if (attr != null) {	
				value += attr.getValue()+",";
			}
		}
		
		item.setRulevalue(value);
		item.setRulecode(rule.getRulevalue());
		item.setRuletype(rule.getRuletype());
		
		return item;
		}

	public void DeleteGroupRules(List<Integer> groupIds) throws Exception {
		if(groupIds == null)
			return;
		
		Group target;
		GroupDAO dao = new GroupDAOImpl();
		for(int i = 0; i< groupIds.size(); i++) {
			target = new Group();
			target.setId(groupIds.get(i));
			
			AddGroupDelLog(target);
			
			dao.GroupOfRuleDel(target);
		}
		
		return ;
	}
	private void AddGroupQueryLog(Group criteria) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditGroupLog auditGroupLog = new AuditGroupLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditGroupLog.setAdminId(als.adminLogin());
		auditGroupLog.setIpAddr("");
		auditGroupLog.setFlag(AuditGroupLog.LOGFLAGQUERY);
		auditGroupLog.setResult(AuditGroupLog.LOGRESULTSUCCESS);
		auditGroupLog.setLATEST_MOD_TIME(timenow);
		auditGroupLog = logdao.AuditGroupLogAdd(auditGroupLog);
		
		if( criteria != null ) {
			AuditGroupLogDescribe auditGroupLogDescribe = new AuditGroupLogDescribe();
			AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
			
			auditGroupLogDescribe.setLogid(auditGroupLog.getId());
			String str="";
			if(criteria.getName() != null && criteria.getName().length() > 0) {
				str += criteria.getName()+";";
			}
			if(criteria.getCode() != null && criteria.getCode().length() > 0) {
				str += criteria.getCode();
			}
//			auditGroupLogDescribe.setDescrib(str);
			
			auditGroupLogDescribe.setLATEST_MOD_TIME(timenow);
//			auditGroupLogDescribe = logDescdao.AuditGroupLogDescribeAdd(auditGroupLogDescribe);
		}
	}
	
	private void AddGroupAddOrUpdateLog(Group group, List<Rule> rules) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		AuditGroupLog auditGroupLog = new AuditGroupLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditGroupLog.setAdminId(als.adminLogin());
		auditGroupLog.setIpAddr("");
		if(group.getId() == 0){
			auditGroupLog.setFlag(AuditGroupLog.LOGFLAGADD);
		}else{
			auditGroupLog.setFlag(AuditGroupLog.LOGFLAGUPDATE);
		}
		auditGroupLog.setResult(AuditGroupLog.LOGRESULTSUCCESS);
		auditGroupLog.setLATEST_MOD_TIME(timenow);
		auditGroupLog = logdao.AuditGroupLogAdd(auditGroupLog);
		
		AuditGroupLogDescribe auditGroupLogDescribe = new AuditGroupLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditGroupLogDescribe.setLogid(auditGroupLog.getId());
		String str="";
		if(group.getName() != null && group.getName().length() > 0) {
			str += group.getName()+";";
		}
		if(group.getCode() != null && group.getCode().length() > 0) {
			str += group.getCode()+";";
		}
		if(group.getDescrib() != null && group.getDescrib().length() > 0) {
			str += group.getDescrib()+";";
		}
		if (rules != null) {
			for(int i = 0; i<rules.size(); i++) {
				if(rules.get(i).getRulename() != null && rules.get(i).getRulename().length() > 0) {
					str += rules.get(i).getRulename()+";";
				}
				if(rules.get(i).getRulevalue() != null && rules.get(i).getRulevalue().length() > 0) {
					str += rules.get(i).getRulevalue()+";";
				}
			}
		}
		GroupDAO dao = new GroupDAOImpl();
		List<User> users = dao.GetGroupUsersByGroupId( group.getCode() );
		for(int i = 0; i<users.size(); i++) {
			if(users.get(i).getNAME() != null && users.get(i).getNAME().length() > 0) {
				str += users.get(i).getNAME()+";";
			}
			if(users.get(i).getUNIT() != null && users.get(i).getUNIT().length() > 0) {
				str += users.get(i).getUNIT()+";";
			}
		}
	    byte[] byteData = str.getBytes("UTF-8");
	    
//	    Blob b=Hibernate.createBlob(byteData);
//	    ByteArrayInputStream out = (ByteArrayInputStream) b.getBinaryStream();		
//		byte[] byteData2 = new byte[out.available()];		
//		out.read(byteData2, 0,byteData2.length);
//		System.out.println(new String(byteData2,"utf-8"));
		
	    auditGroupLogDescribe.setDescrib(Hibernate.createBlob(byteData));
		auditGroupLogDescribe.setLATEST_MOD_TIME(timenow);
//		auditGroupLogDescribe = logDescdao.AuditGroupLogDescribeAdd(auditGroupLogDescribe);
		
		return ;
	}
	
	private void AddGroupDelLog(Group group) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditGroupLog auditGroupLog = new AuditGroupLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditGroupLog.setAdminId(als.adminLogin());
		auditGroupLog.setIpAddr("");
		auditGroupLog.setFlag(AuditGroupLog.LOGFLAGDELETE);
		auditGroupLog.setResult(AuditGroupLog.LOGRESULTSUCCESS);
		auditGroupLog.setLATEST_MOD_TIME(timenow);
		auditGroupLog = logdao.AuditGroupLogAdd(auditGroupLog);
		
		AuditGroupLogDescribe auditGroupLogDescribe = new AuditGroupLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditGroupLogDescribe.setLogid(auditGroupLog.getId());
		
		GroupDAO dao = new GroupDAOImpl();
		List<Group> groups = dao.GetGroupByGroupId( group.getCode() );
		String str="";
		for (int i = 0; i < groups.size(); i++) {
			if(groups.get(i).getName() != null && groups.get(i).getName().length() > 0) {
				str += groups.get(i).getName()+";";
			}
			if(groups.get(i).getCode() != null && groups.get(i).getCode().length() > 0) {
				str += groups.get(i).getCode()+";";
			}
			if(groups.get(i).getDescrib() != null && groups.get(i).getDescrib().length() > 0) {
				str += groups.get(i).getDescrib()+";";
			}
		}
		
		List<Rule> rules = dao.GetGroupRulesByGroupId( group.getCode() );
		for(int i = 0; i<rules.size(); i++) {
			if(rules.get(i).getRulename() != null && rules.get(i).getRulename().length() > 0) {
				str += rules.get(i).getRulename()+";";
			}
			if(rules.get(i).getRulevalue() != null && rules.get(i).getRulevalue().length() > 0) {
				str += rules.get(i).getRulevalue();
			}
		}
		List<User> users = dao.GetGroupUsersByGroupId( group.getCode() );
		for(int i = 0; i<users.size(); i++) {
			if(users.get(i).getNAME() != null && users.get(i).getNAME().length() > 0) {
				str += users.get(i).getNAME()+";";
			}
			if(users.get(i).getUNIT() != null && users.get(i).getUNIT().length() > 0) {
				str += users.get(i).getUNIT();
			}
		}
//		auditGroupLogDescribe.setDescrib(str);
		
		auditGroupLogDescribe.setLATEST_MOD_TIME(timenow);
//		auditGroupLogDescribe = logDescdao.AuditGroupLogDescribeAdd(auditGroupLogDescribe);
	}
	
}

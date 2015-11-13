package com.pms.dao;

import java.util.List;

import com.pms.model.Group;
import com.pms.model.GroupUser;
import com.pms.model.Rule;
import com.pms.model.User;

public interface GroupDAO {

	Group GroupAdd(Group group) throws Exception;
	void UpdateGroupUsers(String id, List<String> userIds) throws Exception;
	List<Group> GetGroupUsers(Group criteria, int page, int rows) throws Exception;
	int GetGroupUsersCount(Group criteria) throws Exception;
	List<User> GetGroupUsersByGroupId(String id) throws Exception;
	List<GroupUser> GetGroupsByUserId(String id) throws Exception;
	void GroupOfUserDel(Group group) throws Exception;
	List<Group> GetAllGroups(Group criteria, int page, int rows) throws Exception;
	void UpdateGroupRules(String id, List<Rule> rules) throws Exception;
	List<Group> GetGroupRules(Group criteria, int page, int rows) throws Exception;
	int GetGroupRulesCount(Group criteria) throws Exception;
	List<Rule> GetGroupRulesByGroupId(String id) throws Exception;
	List<Group> GetGroupByGroupId(String id) throws Exception;
	void GroupOfRuleDel(Group target)  throws Exception;

}

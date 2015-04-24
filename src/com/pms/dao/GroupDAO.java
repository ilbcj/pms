package com.pms.dao;

import java.util.List;

import com.pms.model.Group;
import com.pms.model.GroupUser;
import com.pms.model.Rule;
import com.pms.model.User;

public interface GroupDAO {

	Group GroupAdd(Group group) throws Exception;
	void UpdateGroupUsers(int id, List<Integer> userIds) throws Exception;
	List<Group> GetGroupUsers(Group criteria, int page, int rows) throws Exception;
	int GetGroupUsersCount(Group criteria) throws Exception;
	List<User> GetGroupUsersByGroupId(int id) throws Exception;
	List<GroupUser> GetGroupsByUserId(int id) throws Exception;
	void GroupDel(Group group) throws Exception;
	List<Group> GetAllGroups(Group criteria, int page, int rows) throws Exception;
	void UpdateGroupRules(int id, List<Rule> rules) throws Exception;

}

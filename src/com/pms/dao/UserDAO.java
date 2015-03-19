package com.pms.dao;

import java.util.List;

import com.pms.model.User;

public interface UserDAO {
	public User UserAdd(User user) throws Exception;
	public void UserMod(User user) throws Exception;
	public void UserDel(User user) throws Exception;

	public List<User> GetUsersByParentId(int pid, int page, int rows) throws Exception;
	public List<User> GetUsersByParentIdWithNoPage(int pid, User criteria) throws Exception;
	public int GetUsersCountByParentId(int id, User criteria) throws Exception;
}

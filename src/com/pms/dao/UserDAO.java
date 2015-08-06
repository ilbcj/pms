package com.pms.dao;

import java.util.List;

import com.pms.model.User;

public interface UserDAO {
	public User UserAdd(User user) throws Exception;
	public void UserMod(User user) throws Exception;
	public void UserDel(User user) throws Exception;

	public List<User> GetUsersByParentId(String pid, int page, int rows) throws Exception;
	public List<User> GetUsersByParentIdWithNoPage(String pid, User criteria) throws Exception;
	public int GetUsersCountByParentId(String pid, User criteria) throws Exception;
	public User GetUserByUserName(String name) throws Exception;
	List<User> GetUserById(int id) throws Exception;
}

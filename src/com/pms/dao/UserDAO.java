package com.pms.dao;

import java.util.List;

import com.pms.model.Organization;
import com.pms.model.User;
import com.pms.model.UserImport;

public interface UserDAO {
	public User UserAdd(User user) throws Exception;
	public void UserMod(User user) throws Exception;
	public void UserDel(User user) throws Exception;

	public List<User> GetUsersByParentId(String pid, int page, int rows) throws Exception;
	public List<User> GetUsersByParentIdWithNoPage(String pid, User criteria) throws Exception;
	public int GetUsersCountByParentId(String pid, User criteria) throws Exception;
	public User GetUserByUserName(String name) throws Exception;
	List<User> GetUsersByTime(String time) throws Exception;
	List<User> GetUserById(int id) throws Exception;
	
	public void UserImportSave(UserImport ui) throws Exception;
	public int UserImportClear() throws Exception;
	public List<UserImport> GetUserImports() throws Exception;
	public void UserImport(UserImport oi, Organization org) throws Exception;
}

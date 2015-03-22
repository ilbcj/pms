package com.pms.dao;


import java.util.List;

import com.pms.model.Application;

public interface AppDAO {
	public Application AppAdd(Application app) throws Exception;
	public void AppMod(Application app) throws Exception;
	public void AppDel(Application app) throws Exception;
	public List<Application> GetApplications(Application criteria, int page,
			int rows) throws Exception;
	public int GetApplicationsCount(Application criteria) throws Exception;
	public List<Application> GetApplicationsWithNopage() throws Exception;

//	public List<User> GetUsersByParentId(int pid, int page, int rows) throws Exception;
//	public List<User> GetUsersByParentIdWithNoPage(int pid, User criteria) throws Exception;
//	public int GetUsersCountByParentId(int id, User criteria) throws Exception;
}

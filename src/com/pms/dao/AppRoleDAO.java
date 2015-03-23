package com.pms.dao;


import java.util.List;

import com.pms.model.AppRole;

public interface AppRoleDAO {
	public AppRole AppRoleAdd(AppRole ar) throws Exception;
	public void AppRoleMod(AppRole ar) throws Exception;
	public void AppRoleDel(AppRole ar) throws Exception;
//	public List<AppRole> GetAppRoles(AppRole criteria, int page,
//			int rows) throws Exception;

	public List<AppRole> GetAppRolesByAppId(int appid, AppRole criteria,
			int page, int rows) throws Exception;
	public int GetAppRolesCountByAppId(int appid, AppRole criteria) throws Exception;

}

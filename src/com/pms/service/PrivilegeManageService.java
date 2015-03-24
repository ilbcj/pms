package com.pms.service;

import java.util.ArrayList;
import java.util.List;
import com.pms.dao.AppDAO;
import com.pms.dao.AppRoleDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.dao.impl.AppRoleDAOImpl;
import com.pms.dto.AppRoleItem;
import com.pms.model.AppRole;
import com.pms.model.Application;



public class PrivilegeManageService {
	
//	public AppRole SaveAppRole( AppRole ar ) throws Exception
//	{
//		AppRoleDAO dao = new AppRoleDAOImpl();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
//				Locale.SIMPLIFIED_CHINESE);
//		String timenow = sdf.format(new Date());
//		ar.setTstamp(timenow);
//		ar = dao.AppRoleAdd(ar);
//		return ar;
//	}

	public List<AppRoleItem> QueryAllAppRoleItems() throws Exception {
		List<AppRoleItem> res = new ArrayList<AppRoleItem>();
		
		AppDAO adao = new AppDAOImpl();
		AppRoleDAO ardao = new AppRoleDAOImpl();
		
		List<Application> apps = adao.GetApplicationsWithNopage();
		for( int i = 0; i< apps.size(); i++ ) {
			AppRoleItem item = new AppRoleItem();
			item.setAppId(apps.get(i).getId());
			item.setAppName(apps.get(i).getName());
			item.setAppFlag(apps.get(i).getFlag());
			List<Integer> roleIds = new ArrayList<Integer>();
			List<String> roleNames = new ArrayList<String>();
			List<AppRole> roles = ardao.GetAppRolesByAppId(apps.get(i).getId(), null, -1, -1);
			for(int j = 0; j<roles.size(); j++) {
				roleIds.add(roles.get(j).getId());
				roleNames.add(roles.get(j).getName());
			}
			item.setAppRoleId(roleIds);
			item.setAppRoleName(roleNames);
			
			res.add(item);
		}
		
		return res;
	}

//	public void DeleteAppRoles(List<Integer> appRoleIds) throws Exception {
//		if(appRoleIds == null)
//			return;
//	
//		AppRole target;
//		AppRoleDAO dao = new AppRoleDAOImpl();
//		for(int i = 0; i< appRoleIds.size(); i++) {
//			target = new AppRole();
//			target.setId(appRoleIds.get(i));
//			dao.AppRoleDel(target);
//		}
//	
//		return ;
//	}
	
}

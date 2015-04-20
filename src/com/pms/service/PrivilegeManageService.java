package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.AppDAO;
import com.pms.dao.AppRoleDAO;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.dao.impl.AppRoleDAOImpl;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dto.AppRoleItem;
//import com.pms.dto.PrivilegeTemp;
import com.pms.model.AppRole;
import com.pms.model.Application;
import com.pms.model.Privilege;



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

	public void SavePrivilege(String ownerids, int ownertype,
			List<Integer> roleIds) throws Exception {
		String orgs[] = ownerids.split(",");
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		for(int i = 0; i< orgs.length; i++) {
			for(int j = 0; j<roleIds.size(); j++) {
				Privilege priv = new Privilege();
				priv.setOwner_id(Integer.parseInt(orgs[i]));
				priv.setOwner_type(ownertype);
				//priv.setApp_id(roleIds.get(j).getAppid());
				priv.setRole_id(roleIds.get(j));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
						Locale.SIMPLIFIED_CHINESE);
				String timenow = sdf.format(new Date());
				priv.setTstamp(timenow);
				dao.PrivilegeAdd(priv);
			}
		}
		
	}

	public void UpdatePrivilege(int ownerid, int ownertype,
			List<Integer> roleIds) throws Exception {
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		List<Privilege> privs = new ArrayList<Privilege>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		for(int i = 0; i<roleIds.size(); i++) {
			Privilege priv = new Privilege();
			priv.setOwner_id(ownerid);
			priv.setOwner_type(ownertype);
			priv.setRole_id(roleIds.get(i));
			priv.setTstamp(timenow);
			privs.add(priv);
		}
		dao.UpdatePrivilegeByOwnerId(ownerid, ownertype, privs);
	}

	public List<Privilege> QueryPrivilegesByOwnerId(int ownerid, int ownertype) throws Exception {
		List<Privilege> res = null;
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		res = dao.QueryPrivilegesByOwnerId(ownerid, ownertype);
		return res;
	}

	
}

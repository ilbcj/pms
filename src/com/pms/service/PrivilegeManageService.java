package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.AppDAO;
import com.pms.dao.AppRoleDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.GroupDAO;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.dao.impl.AppRoleDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.GroupDAOImpl;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.dto.AppRoleItem;
//import com.pms.dto.PrivilegeTemp;
import com.pms.model.AppRole;
import com.pms.model.Application;
import com.pms.model.AuditPrivLog;
import com.pms.model.AuditPrivLogDescribe;
import com.pms.model.Group;
import com.pms.model.Privilege;
import com.pms.model.ResRole;
import com.pms.model.User;



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
			AddPrivilegeAddOrUpdateLog(Integer.parseInt(orgs[i]),ownertype,roleIds,"add");
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
		AddPrivilegeAddOrUpdateLog(ownerid,ownertype,roleIds,"update");
	}

	public List<Privilege> QueryPrivilegesByOwnerId(int ownerid, int ownertype) throws Exception {
		List<Privilege> res = null;
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		res = dao.QueryPrivilegesByOwnerId(ownerid, ownertype);
		return res;
	}
	
	private void AddPrivilegeAddOrUpdateLog(int ownerid, int ownertype, List<Integer> roleIds, String flag) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		AuditPrivLog auditPrivLog = new AuditPrivLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditPrivLog.setAdminId(als.adminLogin());
		auditPrivLog.setIpAddr("");
		if(flag=="add") {
			auditPrivLog.setFlag(AuditPrivLog.LOGFLAGADD);
		}
		else if(flag=="update") {
			auditPrivLog.setFlag(AuditPrivLog.LOGFLAGUPDATE);
		}
		auditPrivLog.setResult(AuditPrivLog.LOGRESULTSUCCESS);
		auditPrivLog.setLATEST_MOD_TIME(timenow);
		auditPrivLog = logdao.AuditPrivLogAdd(auditPrivLog);
		
		AuditPrivLogDescribe auditPrivLogDescribe = new AuditPrivLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditPrivLogDescribe.setLogid(auditPrivLog.getId());
		String str="";
		if (ownertype==Privilege.OWNERTYPEUSER) {
			UserDAO userDao = new UserDAOImpl();
			List<User> userNodes = userDao.GetUserById(ownerid);
			for (int i = 0; i < userNodes.size(); i++) {
				if(userNodes.get(i).getNAME() != null && userNodes.get(i).getNAME().length() > 0) {
					str += userNodes.get(i).getNAME()+";";
				}
				if(userNodes.get(i).getUNIT() != null && userNodes.get(i).getUNIT().length() > 0) {
					str += userNodes.get(i).getUNIT()+";";
				}
				
			}
			ResourceDAO resDao = new ResourceDAOImpl();
			for(int i = 0; i<roleIds.size(); i++) {
				List<ResRole> roleNodes=resDao.GetRoleById(roleIds.get(i));
				for (int j = 0; j < roleNodes.size(); j++) {
					if(roleNodes.get(j).getBUSINESS_ROLE_NAME() != null && roleNodes.get(j).getBUSINESS_ROLE_NAME().length() > 0) {
						str += roleNodes.get(j).getBUSINESS_ROLE_NAME()+";";
					}
					if(roleNodes.get(j).getBUSINESS_ROLE() != null && roleNodes.get(j).getBUSINESS_ROLE().length() > 0) {
						str += roleNodes.get(j).getBUSINESS_ROLE();
					}
				}
			}			
		}
		else if(ownertype==Privilege.OWNERTYPEUSERGROUP){
			GroupDAO dao = new GroupDAOImpl();
			List<Group> groupNodes = dao.GetGroupByGroupId(ownerid);
			for (int i = 0; i < groupNodes.size(); i++) {
				if(groupNodes.get(i).getName() != null && groupNodes.get(i).getName().length() > 0) {
					str += groupNodes.get(i).getName()+";";
				}
				if(groupNodes.get(i).getCode() != null && groupNodes.get(i).getCode().length() > 0) {
					str += groupNodes.get(i).getCode()+";";
				}
			}
			ResourceDAO resDao = new ResourceDAOImpl();
			for(int i = 0; i<roleIds.size(); i++) {
				List<ResRole> roleNodes=resDao.GetRoleById(roleIds.get(i));
				for (int j = 0; j < roleNodes.size(); j++) {
					if(roleNodes.get(j).getBUSINESS_ROLE_NAME() != null && roleNodes.get(j).getBUSINESS_ROLE_NAME().length() > 0) {
						str += roleNodes.get(j).getBUSINESS_ROLE_NAME()+";";
					}
					if(roleNodes.get(j).getBUSINESS_ROLE() != null && roleNodes.get(j).getBUSINESS_ROLE().length() > 0) {
						str += roleNodes.get(j).getBUSINESS_ROLE();
					}
				}
			}			
		}
		auditPrivLogDescribe.setDescrib(str);
		
		auditPrivLogDescribe.setLATEST_MOD_TIME(timenow);
		auditPrivLogDescribe = logDescdao.AuditPrivLogDescribeAdd(auditPrivLogDescribe);
		
		return ;
	}
	
}

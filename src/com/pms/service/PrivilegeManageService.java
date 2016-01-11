/**   
 * @ClassName:     ${PrivilegeManageService}   
 * @Description:   ${授权管理功能}   
 * 
 * @ProductName:   ${中盈集中授权平台}
 * @author         ${北京中盈网信科技有限公司}  
 * @version        V1.0     
 * @Date           ${2014.8.28} 
*/
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
import com.pms.dao.OrganizationDAO;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.dao.impl.AppRoleDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.GroupDAOImpl;
import com.pms.dao.impl.OrganizationDAOImpl;
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
import com.pms.model.Organization;
import com.pms.model.Privilege;
import com.pms.model.ResRole;
import com.pms.model.User;
import com.pms.model.UserRole;
import com.pms.model.UserRoleView;



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
			List<String> roleIds) throws Exception {
		String orgs[] = ownerids.split(",");
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		for(int i = 0; i< orgs.length; i++) {
			for(int j = 0; j<roleIds.size(); j++) {
				Privilege priv = new Privilege();
				//priv.setOwner_id(Integer.parseInt(orgs[i]));
				priv.setOwner_id(orgs[i]);
				priv.setOwner_type(ownertype);
				//priv.setApp_id(roleIds.get(j).getAppid());
				priv.setRole_id(roleIds.get(j));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
						Locale.SIMPLIFIED_CHINESE);
				String timenow = sdf.format(new Date());
				priv.setTstamp(timenow);
				dao.PrivilegeAdd(priv);
				
//				saveUserRole(orgs[i], ownertype, roleIds.get(j) );
			}
			saveUserRole();
			
			AddPrivilegeAddOrUpdateLog(orgs[i],ownertype,roleIds,"add");
		}
	}
	
	public void UpdatePrivilege(String ownerid, int ownertype,
			List<String> roleIds) throws Exception {
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
			
//			saveUserRole(ownerid, ownertype, roleIds.get(i) );
		}
		dao.UpdatePrivilegeByOwnerId(ownerid, ownertype, privs);
		
		saveUserRole();
		AddPrivilegeAddOrUpdateLog(ownerid,ownertype,roleIds,"update");
	}

	public List<Privilege> QueryPrivilegesByOwnerId(String ownerid, int ownertype) throws Exception {
		List<Privilege> res = null;
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		res = dao.QueryPrivilegesByOwnerId(ownerid, ownertype);
		return res;
	}
	
	private void saveUserRole() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		PrivilegeDAO dao = new PrivilegeDAOImpl();
		
		UserRole userRole =  new UserRole();
		List<UserRole>  res = dao.QueryUserRole();
		for (int i = 0; i < res.size(); i++) {
			UserRoleView  nodeView = dao.GetUserRoleViewByUserIdRoleID( res.get(i).getCERTIFICATE_CODE_MD5(), res.get(i).getBUSINESS_ROLE());
			if(nodeView == null && res.get(i).getDELETE_STATUS() != UserRole.DELSTATUSYES){
				userRole.setId(res.get(i).getId());
				userRole.setCERTIFICATE_CODE_MD5( res.get(i).getCERTIFICATE_CODE_MD5() );
				userRole.setBUSINESS_ROLE(res.get(i).getBUSINESS_ROLE());
				
				userRole.setDELETE_STATUS(UserRole.DELSTATUSYES);
				userRole.setDATA_VERSION(res.get(i).getDATA_VERSION()+1);
				userRole.setLATEST_MOD_TIME(timenow);
				dao.UserRoleUpdate(userRole);
			}
		}
		
		List<UserRoleView>  resView = dao.QueryUserRoleView();
		for (int i = 0; i < resView.size(); i++) {
			
			UserRole  node = dao.GetUserRoleByUserIdRoleID( resView.get(i).getCERTIFICATE_CODE_MD5(), resView.get(i).getRole_id());
			if(node == null){
				userRole.setCERTIFICATE_CODE_MD5( resView.get(i).getCERTIFICATE_CODE_MD5() );
				userRole.setBUSINESS_ROLE(resView.get(i).getRole_id());	
				userRole.setDELETE_STATUS(UserRole.DELSTATUSNO);
				userRole.setDATA_VERSION(1);
				userRole.setLATEST_MOD_TIME(timenow);
				dao.UserRoleUpdate(userRole);
			}else{
				
				if( node.getDELETE_STATUS() == UserRole.DELSTATUSYES ){
					userRole.setId(node.getId());
					userRole.setCERTIFICATE_CODE_MD5( node.getCERTIFICATE_CODE_MD5() );
					userRole.setBUSINESS_ROLE(node.getBUSINESS_ROLE());
					
					userRole.setDELETE_STATUS(UserRole.DELSTATUSNO);
					userRole.setDATA_VERSION(node.getDATA_VERSION()+1);
					userRole.setLATEST_MOD_TIME(timenow);
					dao.UserRoleUpdate(userRole);
				}
				
			}
			
		}
		
	}
	
	private void AddPrivilegeAddOrUpdateLog(String ownerid, int ownertype, List<String> roleIds, String flag) throws Exception {
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
		if (ownertype==Privilege.OWNERTYPEORG) {
			OrganizationDAO orgDao = new OrganizationDAOImpl();
			Organization orgNode = orgDao.GetOrgNodeById(ownerid);
			str += "机构授权;";
			if(orgNode.getUNIT() != null && orgNode.getUNIT().length() > 0) {
				str += "机构名称:" + orgNode.getUNIT()+";";
			}
			if(orgNode.getGA_DEPARTMENT() != null && orgNode.getGA_DEPARTMENT().length() > 0) {
				str += "机构编码:" + orgNode.getGA_DEPARTMENT()+";";
			}
			if(orgNode.getPARENT_ORG() != null && orgNode.getPARENT_ORG().length() > 0) {
				str += "上级机构编码:" + orgNode.getPARENT_ORG()+";";
			}
			str += "公安组织机构级别:" + orgNode.getORG_LEVEL();
		}
		else if (ownertype==Privilege.OWNERTYPEUSER) {
			UserDAO userDao = new UserDAOImpl();
			User userNode = userDao.GetUserByCertificateCodeMd5(ownerid);
			str += "用户授权;";
			if(userNode.getNAME() != null && userNode.getNAME().length() > 0) {
				str += "姓名:" + userNode.getNAME()+";";
			}
			if(userNode.getUNIT() != null && userNode.getUNIT().length() > 0) {
				str += "组织机构:" + userNode.getUNIT()+";";
			}		
		}
		else if(ownertype==Privilege.OWNERTYPEUSERGROUP){
			GroupDAO dao = new GroupDAOImpl();
			List<Group> groupNodes = dao.GetGroupByGroupId(ownerid);
			str += "群体授权;";
			for (int i = 0; i < groupNodes.size(); i++) {
				if(groupNodes.get(i).getName() != null && groupNodes.get(i).getName().length() > 0) {
					str += "名称:" + groupNodes.get(i).getName()+";";
				}
				if(groupNodes.get(i).getCode() != null && groupNodes.get(i).getCode().length() > 0) {
					str += "编码:" +groupNodes.get(i).getCode()+";";
				}
			}			
		}
		ResourceDAO resDao = new ResourceDAOImpl();
		for(int i = 0; i<roleIds.size(); i++) {
			List<ResRole> roleNodes=resDao.GetRoleById(roleIds.get(i));
			for (int j = 0; j < roleNodes.size(); j++) {
				if(roleNodes.get(j).getBUSINESS_ROLE_NAME() != null && roleNodes.get(j).getBUSINESS_ROLE_NAME().length() > 0) {
					str += "角色名称:" + roleNodes.get(j).getBUSINESS_ROLE_NAME()+";";
				}
				if(roleNodes.get(j).getBUSINESS_ROLE() != null && roleNodes.get(j).getBUSINESS_ROLE().length() > 0) {
					str += "角色编码:" + roleNodes.get(j).getBUSINESS_ROLE();
				}
			}
		}
		auditPrivLogDescribe.setDescrib(str);
		
		auditPrivLogDescribe.setLATEST_MOD_TIME(timenow);
		auditPrivLogDescribe = logDescdao.AuditPrivLogDescribeAdd(auditPrivLogDescribe);
		
		return ;
	}
	
}

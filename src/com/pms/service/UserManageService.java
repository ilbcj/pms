package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.pms.dao.AttributeDAO;
import com.pms.dao.AuditLogDAO;
import com.pms.dao.AuditLogDescribeDao;
import com.pms.dao.PrivilegeDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.AttributeDAOImpl;
import com.pms.dao.impl.AuditLogDAOImpl;
import com.pms.dao.impl.AuditLogDescribeDAOImpl;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.dto.PrivUserListItem;
import com.pms.dto.UserListItem;
import com.pms.model.AttrDictionary;
import com.pms.model.AuditUserLog;
import com.pms.model.AuditUserLogDescribe;
import com.pms.model.Organization;
import com.pms.model.Privilege;
import com.pms.model.User;
import com.pms.util.MD5Security;



public class UserManageService {
	
	public User SaveUser( User user ) throws Exception
	{
		UserDAO dao = new UserDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		user.setLATEST_MOD_TIME(timenow);
		user.setDATA_VERSION(user.getDATA_VERSION()+1);
		String idNum = user.getCERTIFICATE_CODE_SUFFIX();
		if( idNum.length() > 6 ){
			user.setCERTIFICATE_CODE_MD5( generateHash(idNum) );
			user.setCERTIFICATE_CODE_SUFFIX( generateSuffix(idNum) );
		}
		
		AddUserAddOrUpdateLog(user);
		user = dao.UserAdd(user);
		
		return user;
	}
	
	public int QueryUserItems(String pid, int page, int rows,
			List<UserListItem> items) throws Exception {
		UserDAO dao = new UserDAOImpl();
		List<User> res = dao.GetUsersByParentId( pid, page, rows );
		UserListItem userItem = null;
		for(int i=0; i<res.size(); i++) {
			userItem = ConvertUserToListItem(res.get(i));
			items.add(userItem);
		}
		int total = QueryChildrenUsersCount( pid, null );
		return total;
	}
	
	public int QueryChildrenUsersCount(String pid, User criteria) throws Exception {
		UserDAO dao = new UserDAOImpl();
		int count = dao.GetUsersCountByParentId( pid, criteria );
		return count;
	}

	public UserListItem ConvertUserToListItem(User user) throws Exception {
		UserListItem item = new UserListItem();
		item.setId(user.getId());
		item.setName(user.getNAME());
		item.setParent_id(user.getGA_DEPARTMENT());
		item.setOrgLevel(user.getORG_LEVEL());
		item.setDept(user.getDept());
		item.setIdnum(user.getCERTIFICATE_CODE_SUFFIX());
		item.setMax_sensitive_level(user.getSENSITIVE_LEVEL());
		item.setBusiness_type(user.getBUSINESS_TYPE());
		item.setPolice_num(user.getPOLICE_NO());
		item.setPolice_type(user.getPOLICE_SORT());
		item.setPosition(user.getPosition());
		item.setSex(user.getSEXCODE());
		item.setStatus(user.getUSER_STATUS());
		item.setTitle(user.getTAKE_OFFICE());
		item.setData_version(user.getDATA_VERSION());
		item.setCertificate_code_md5(user.getCERTIFICATE_CODE_MD5());
		
		OrgManageService oms = new OrgManageService();
		String path = oms.QueryNodePath(user.getGA_DEPARTMENT());
		if(path != null && path.length() > 0){
			//name1/name2/name3/name4 -->  name1/name2/name3  and  name4
			int index = path.lastIndexOf('/');
			String pname = "";
			String gname = "";
			if( -1 == index ) {
				pname = path;
			}
			else {
				pname = path.substring(path.lastIndexOf('/')+1, path.length());
				gname = path.substring(0, path.lastIndexOf('/'));
			}
			item.setPname(pname);
			item.setGname(gname);
		}
		
		AttributeDAO attrdao = new AttributeDAOImpl();
		List<AttrDictionary> attrDicts = attrdao.GetUsersDictionarys(user.getId());
		List<AttrDictionary> data = new ArrayList<AttrDictionary>();
		for(int i = 0; i < attrDicts.size(); i++) {
			AttrDictionary attrDictionary=new AttrDictionary();

			attrDictionary.setId(attrDicts.get(i).getId());
			attrDictionary.setAttrid(attrDicts.get(i).getAttrid());
			attrDictionary.setValue(attrDicts.get(i).getValue());
			attrDictionary.setCode(attrDicts.get(i).getCode());
			attrDictionary.setTstamp(attrDicts.get(i).getTstamp());
			data.add(attrDictionary);
		}
		
		item.setDictionary(data);
		
		return item;
	}

	public int QueryAllUserItems(String pid, User criteria, int page, int rows,
			List<UserListItem> items) throws Exception {
		criteria.setDELETE_STATUS(User.DELSTATUSNO);
		UserDAO dao = new UserDAOImpl();
		int total = 0;
		List<User> res = new ArrayList<User>();
		UserListItem userItem = null;
		
		if (pid.equals("0")) {
			res=dao.GetAllUsers(criteria, page, rows);
			for(int i=0; i<res.size(); i++) {
				userItem = ConvertUserToListItem(res.get(i));
				items.add(userItem);
			}
			total=dao.GetAllUsersCount(criteria);
		}else{
			//get all orgs;
			List<Organization> nodes = new ArrayList<Organization>();
			OrgManageService oms = new OrgManageService();
			oms.queryAllChildrenNodesById(pid, null, nodes);
			Organization first = new Organization();
			first.setGA_DEPARTMENT(pid);
			nodes.add(0, first);
		
			//calculate all items' count and get users;
			int pre_count = 0;
			for(int i = 0; i< nodes.size(); i++) {
				total += QueryChildrenUsersCount( nodes.get(i).getGA_DEPARTMENT(), criteria );
				if( total <= ((page-1) * rows) ) {
					pre_count = total;
				}
				else if ( total <= (page * rows) ) {
					List<User> tmp = dao.GetUsersByParentIdWithNoPage( nodes.get(i).getGA_DEPARTMENT(), criteria );
					if( tmp != null && tmp.size() > 0) {
						if( pre_count < ((page-1) * rows) ) {
							int fromIndex = rows - (pre_count%rows);
							int toIndex = tmp.size() - fromIndex >= rows ? fromIndex+rows : tmp.size();
							res.addAll(tmp.subList( fromIndex, toIndex));
						}
						else {
							int toIndex = rows - (pre_count%rows) >= tmp.size() ? tmp.size() : rows - (pre_count%rows);
							res.addAll(tmp.subList(0, toIndex));
						}
					}
					pre_count = total;
				} 
				else {//total > page * rows
					if( pre_count < (page * rows) ) {
						List<User> tmp = dao.GetUsersByParentIdWithNoPage( nodes.get(i).getGA_DEPARTMENT(), criteria );
						if( tmp != null && tmp.size() > 0 ) {
							int toIndex =  rows - (pre_count%rows);
							res.addAll(tmp.subList( 0, toIndex));
						}
					}
					pre_count = total;
				}
			}
			
			//convert to datagrid's item
			for(int i=0; i<res.size(); i++) {
				userItem = ConvertUserToListItem(res.get(i));
				items.add(userItem);
			}
		}
		
		AddUserQueryLog(criteria);
		
		return total;
	}

	public int QueryAllPrivUserItems(String pid, int privStatus, User criteria, int page, int rows,
			List<PrivUserListItem> items) throws Exception {
		List<UserListItem> ulItems = new ArrayList<UserListItem>();
		int total = QueryAllUserItems(pid, criteria, page, rows, ulItems);
		
		PrivilegeDAO pdao = new PrivilegeDAOImpl();
		for(int i = 0; i<ulItems.size(); i++) {
			PrivUserListItem pulItem = new PrivUserListItem();
			pulItem.setId(ulItems.get(i).getId());
			pulItem.setName(ulItems.get(i).getName());
			pulItem.setParent_id(ulItems.get(i).getParent_id());
			pulItem.setPname(ulItems.get(i).getPname());
			pulItem.setGname(ulItems.get(i).getGname());
			pulItem.setStatus(ulItems.get(i).getStatus());
			pulItem.setCertificate_code_md5(ulItems.get(i).getCertificate_code_md5());
			int count = pdao.QueryPrivilegesCountByOwnerId(ulItems.get(i).getId(), Privilege.OWNERTYPEUSER);
			pulItem.setPriv_status(count>0?PrivUserListItem.PRIVSTATUSYES:PrivUserListItem.PRIVSTATUSNO);
			if(PrivUserListItem.PRIVSTATUSYES == privStatus) {
				if( pulItem.getPriv_status() == PrivUserListItem.PRIVSTATUSNO ) {
					total--;
					continue;
				}
			} else if( PrivUserListItem.PRIVSTATUSNO == privStatus) {
				if( pulItem.getPriv_status() == PrivUserListItem.PRIVSTATUSYES ) {
					total--;
					continue;
				}
			}
			
			items.add(pulItem);
		}
		return total;
	}

	public int QueryPrivUserItems(String pid, int privStatus, int page, int rows,
			List<PrivUserListItem> items) throws Exception {
		List<UserListItem> ulItems = new ArrayList<UserListItem>();
		int total = QueryUserItems(pid, page, rows, ulItems);

		PrivilegeDAO pdao = new PrivilegeDAOImpl();
		for(int i = 0; i<ulItems.size(); i++) {
			PrivUserListItem pulItem = new PrivUserListItem();
			pulItem.setId(ulItems.get(i).getId());
			pulItem.setName(ulItems.get(i).getName());
			pulItem.setParent_id(ulItems.get(i).getParent_id());
			pulItem.setPname(ulItems.get(i).getPname());
			pulItem.setGname(ulItems.get(i).getGname());
			pulItem.setStatus(ulItems.get(i).getStatus());
			
			int count = pdao.QueryPrivilegesCountByOwnerId(ulItems.get(i).getId(), Privilege.OWNERTYPEUSER);
			pulItem.setPriv_status(count>0?PrivUserListItem.PRIVSTATUSYES:PrivUserListItem.PRIVSTATUSNO);
			items.add(pulItem);
		}
		return total;
	}
	
	public void DeleteUserNodes(List<String> nodeIds) throws Exception
	{	
		if(nodeIds == null)
			return;
		User user;
		for(int i = 0; i< nodeIds.size(); i++) {
			user = new User();
			user.setCERTIFICATE_CODE_MD5(nodeIds.get(i));
			
			DeleteUserNode(user);
		}
		
		return ;
	}
	public User DeleteUserNode(User user) throws Exception
	{
		UserDAO dao = new UserDAOImpl();
		User node = dao.GetUserByCertificateCodeMd5(user.getCERTIFICATE_CODE_MD5());
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		user.setId(node.getId());
		user.setNAME(node.getNAME());
		user.setCERTIFICATE_CODE_MD5(node.getCERTIFICATE_CODE_MD5());
		user.setCERTIFICATE_CODE_SUFFIX(node.getCERTIFICATE_CODE_SUFFIX());
		user.setSEXCODE(node.getSEXCODE());
		user.setGA_DEPARTMENT(node.getGA_DEPARTMENT());
		user.setUNIT(node.getUNIT());
		user.setORG_LEVEL(node.getORG_LEVEL());
		user.setPOLICE_SORT(node.getPOLICE_SORT());
		user.setPOLICE_NO(node.getPOLICE_NO());
		user.setSENSITIVE_LEVEL(node.getSENSITIVE_LEVEL());
		user.setBUSINESS_TYPE(node.getBUSINESS_TYPE());
		user.setTAKE_OFFICE(node.getTAKE_OFFICE());
		user.setUSER_STATUS(node.getUSER_STATUS());
		user.setPosition(node.getPosition());
		user.setDept(node.getDept());
		user.setDELETE_STATUS(User.DELSTATUSYES);
		user.setDATA_VERSION(node.getDATA_VERSION()+1);
		user.setLATEST_MOD_TIME(timenow);
		
		user = dao.UserAdd(user);
		AddUserDelLog(user);
		return user;
	}
	
	private String generateHash(String idNum) throws Exception {
		
//		MessageDigest digest = MessageDigest.getInstance("MD5");
//
//		if(digest == null)
//		{
//			throw new Exception("generate hash operator fail.");
//		}
//		byte[] hash = digest.digest(idNum.getBytes());
//
//		String result =  new BASE64Encoder().encode(hash);
//		return result;
		return MD5Security.md5(idNum);
	}
	
	private String generateSuffix(String idNum) throws Exception {
		
		String value = idNum.substring( idNum.length()-6, idNum.length() );

		return value;
	}
	
	private void AddUserQueryLog(User criteria) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditUserLog auditUserLog = new AuditUserLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditUserLog.setAdminId(als.adminLogin());
		auditUserLog.setIpAddr("");
		auditUserLog.setFlag(AuditUserLog.LOGFLAGQUERY);
		auditUserLog.setResult(AuditUserLog.LOGRESULTSUCCESS);
		auditUserLog.setLATEST_MOD_TIME(timenow);
		auditUserLog = logdao.AuditUserLogAdd(auditUserLog);
		
		if( criteria != null ) {
			AuditUserLogDescribe auditUserLogDescribe = new AuditUserLogDescribe();
			AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
			
			auditUserLogDescribe.setLogid(auditUserLog.getId());
			String str="";
			if(criteria.getNAME() != null && criteria.getNAME().length() > 0) {
				str += "姓名:" + criteria.getNAME()+";";
			}
			str += "状态:" + criteria.getUSER_STATUS()+";";
			if(criteria.getBUSINESS_TYPE() != null && criteria.getBUSINESS_TYPE().length() > 0) {
				str += "业务部门类别:" + criteria.getBUSINESS_TYPE()+";";
			}
			if(criteria.getPOLICE_SORT() != null && criteria.getPOLICE_SORT().length() > 0) {
				str += "警种:" + criteria.getPOLICE_SORT()+";";
			}
			if(criteria.getSEXCODE() != null && criteria.getSEXCODE().length() > 0) {
				str += "性别:" + criteria.getSEXCODE()+";";
			}
			if(criteria.getCERTIFICATE_CODE_SUFFIX() != null && criteria.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
				str += "身份证:" + criteria.getCERTIFICATE_CODE_SUFFIX()+";";
			}
			if(criteria.getSENSITIVE_LEVEL() != null && criteria.getSENSITIVE_LEVEL().length() > 0) {
				str += "最高敏感级别:" + criteria.getSENSITIVE_LEVEL()+";";
			}
			if(criteria.getPosition() != null && criteria.getPosition().length() > 0) {
				str += "岗位:" + criteria.getPosition()+";";
			}
			if(criteria.getDept() != null && criteria.getDept().length() > 0) {
				str += "二级部门:" + criteria.getDept()+";";
			}
			if(criteria.getTAKE_OFFICE() != null && criteria.getTAKE_OFFICE().length() > 0) {
				str += "职务/职称:" + criteria.getTAKE_OFFICE()+";";
			}
			if(criteria.getPOLICE_NO() != null && criteria.getPOLICE_NO().length() > 0) {
				str += "警号  :" + criteria.getPOLICE_NO();
			}
			auditUserLogDescribe.setDescrib(str);
			
			auditUserLogDescribe.setLATEST_MOD_TIME(timenow);
			auditUserLogDescribe = logDescdao.AuditUserLogDescribeAdd(auditUserLogDescribe);
		}
	}
	
	private void AddUserAddOrUpdateLog(User user) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		AuditUserLog auditUserLog = new AuditUserLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditUserLog.setAdminId(als.adminLogin());
		auditUserLog.setIpAddr("");
		if(user.getId() == 0){
			auditUserLog.setFlag(AuditUserLog.LOGFLAGADD);
		}else{
			auditUserLog.setFlag(AuditUserLog.LOGFLAGUPDATE);
		}
		auditUserLog.setResult(AuditUserLog.LOGRESULTSUCCESS);
		auditUserLog.setLATEST_MOD_TIME(timenow);
		auditUserLog = logdao.AuditUserLogAdd(auditUserLog);
		
		AuditUserLogDescribe auditUserLogDescribe = new AuditUserLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditUserLogDescribe.setLogid(auditUserLog.getId());
		String str="";
		if(user.getNAME() != null && user.getNAME().length() > 0) {
			str += "姓名:" + user.getNAME()+";";
		}
		str += "状态:" + user.getUSER_STATUS()+";";
		if(user.getBUSINESS_TYPE() != null && user.getBUSINESS_TYPE().length() > 0) {
			str += "业务部门类别:" + user.getBUSINESS_TYPE()+";";
		}
		if(user.getPOLICE_SORT() != null && user.getPOLICE_SORT().length() > 0) {
			str += "警种:" + user.getPOLICE_SORT()+";";
		}
		if(user.getSEXCODE() != null && user.getSEXCODE().length() > 0) {
			str += "性别:" + user.getSEXCODE()+";";
		}
		if(user.getCERTIFICATE_CODE_SUFFIX() != null && user.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
			str += "身份证:" + user.getCERTIFICATE_CODE_SUFFIX()+";";
		}
		if(user.getGA_DEPARTMENT() != null && user.getGA_DEPARTMENT().length() > 0) {
			str += "组织机构编码:" + user.getGA_DEPARTMENT()+";";
		}
		if(user.getUNIT() != null && user.getUNIT().length() > 0) {
			str += "组织机构名称:" + user.getUNIT()+";";
		}
		if(user.getSENSITIVE_LEVEL() != null && user.getSENSITIVE_LEVEL().length() > 0) {
			str += "最高敏感级别:" + user.getSENSITIVE_LEVEL()+";";
		}
		if(user.getPosition() != null && user.getPosition().length() > 0) {
			str += "岗位:" + user.getPosition()+";";
		}
		if(user.getDept() != null && user.getDept().length() > 0) {
			str += "二级部门:" + user.getDept()+";";
		}
		if(user.getTAKE_OFFICE() != null && user.getTAKE_OFFICE().length() > 0) {
			str += "职务/职称:" + user.getTAKE_OFFICE()+";";
		}
		if(user.getPOLICE_NO() != null && user.getPOLICE_NO().length() > 0) {
			str += "警号  :" + user.getPOLICE_NO();
		}
		auditUserLogDescribe.setDescrib(str);
		
		auditUserLogDescribe.setLATEST_MOD_TIME(timenow);
		auditUserLogDescribe = logDescdao.AuditUserLogDescribeAdd(auditUserLogDescribe);
		
		return ;
	}
	
	private void AddUserDelLog(User user) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		AuditUserLog auditUserLog = new AuditUserLog();
		AuditLogDAO logdao = new AuditLogDAOImpl();
		AuditLogService als = new AuditLogService();
		
		auditUserLog.setAdminId(als.adminLogin());
		auditUserLog.setIpAddr("");
		auditUserLog.setFlag(AuditUserLog.LOGFLAGDELETE);
		auditUserLog.setResult(AuditUserLog.LOGRESULTSUCCESS);
		auditUserLog.setLATEST_MOD_TIME(timenow);
		auditUserLog = logdao.AuditUserLogAdd(auditUserLog);
		
		AuditUserLogDescribe auditUserLogDescribe = new AuditUserLogDescribe();
		AuditLogDescribeDao logDescdao = new AuditLogDescribeDAOImpl();
		
		auditUserLogDescribe.setLogid(auditUserLog.getId());
		
		String str="";
		if(user.getNAME() != null && user.getNAME().length() > 0) {
			str += "姓名:" + user.getNAME()+";";
		}
		str += "状态:" + user.getUSER_STATUS()+";";
		if(user.getBUSINESS_TYPE() != null && user.getBUSINESS_TYPE().length() > 0) {
			str += "业务部门类别:" + user.getBUSINESS_TYPE()+";";
		}
		if(user.getPOLICE_SORT() != null && user.getPOLICE_SORT().length() > 0) {
			str += "警种:" + user.getPOLICE_SORT()+";";
		}
		if(user.getSEXCODE() != null && user.getSEXCODE().length() > 0) {
			str += "性别:" + user.getSEXCODE()+";";
		}
		if(user.getCERTIFICATE_CODE_SUFFIX() != null && user.getCERTIFICATE_CODE_SUFFIX().length() > 0) {
			str += "身份证:" + user.getCERTIFICATE_CODE_SUFFIX()+";";
		}
		if(user.getGA_DEPARTMENT() != null && user.getGA_DEPARTMENT().length() > 0) {
			str += "组织机构编码:" + user.getGA_DEPARTMENT()+";";
		}
		if(user.getUNIT() != null && user.getUNIT().length() > 0) {
			str += "组织机构名称:" + user.getUNIT()+";";
		}
		if(user.getSENSITIVE_LEVEL() != null && user.getSENSITIVE_LEVEL().length() > 0) {
			str += "最高敏感级别:" + user.getSENSITIVE_LEVEL()+";";
		}
		if(user.getPosition() != null && user.getPosition().length() > 0) {
			str += "岗位:" + user.getPosition()+";";
		}
		if(user.getDept() != null && user.getDept().length() > 0) {
			str += "二级部门:" + user.getDept()+";";
		}
		if(user.getTAKE_OFFICE() != null && user.getTAKE_OFFICE().length() > 0) {
			str += "职务/职称:" + user.getTAKE_OFFICE()+";";
		}
		if(user.getPOLICE_NO() != null && user.getPOLICE_NO().length() > 0) {
			str += "警号  :" + user.getPOLICE_NO();
		}
		
		auditUserLogDescribe.setDescrib(str);
		
		auditUserLogDescribe.setLATEST_MOD_TIME(timenow);
		auditUserLogDescribe = logDescdao.AuditUserLogDescribeAdd(auditUserLogDescribe);
	}
}

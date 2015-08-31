package com.pms.service;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sun.misc.BASE64Encoder;

import com.pms.dao.PrivilegeDAO;
import com.pms.dao.UserDAO;
import com.pms.dao.impl.PrivilegeDAOImpl;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.dto.PrivUserListItem;
import com.pms.dto.UserListItem;
import com.pms.model.Organization;
import com.pms.model.Privilege;
import com.pms.model.User;



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
		item.setDept(user.getDept());
		item.setIdnum(user.getCERTIFICATE_CODE_SUFFIX());
		item.setMax_sensitive_level(user.getSENSITIVE_LEVEL());
		item.setPolice_num(user.getPOLICE_NO());
		item.setPolice_type(user.getPOLICE_SORT());
		item.setPosition(user.getPosition());
		item.setSex(user.getSEXCODE());
		item.setStatus(user.getUSER_STATUS());
		item.setTitle(user.getTAKE_OFFICE());
		item.setUnit(user.getBUSINESS_TYPE());
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
		
		return item;
	}

	public int QueryAllUserItems(String pid, User criteria, int page, int rows,
			List<UserListItem> items) throws Exception {
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
	
	public void DeleteUserNodes(List<Integer> nodeIds) throws Exception
	{
		if(nodeIds == null)
			return;
		User user;
		for(int i = 0; i< nodeIds.size(); i++) {
			user = new User();
			user.setId(nodeIds.get(i));
			
			DeleteUserNode(user);
		}
		
		return ;
	}
	public User DeleteUserNode(User user) throws Exception
	{
		UserDAO dao = new UserDAOImpl();
		List<User> nodes = dao.GetUserById(user.getId());
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		
		for(int i = 0; i< nodes.size(); i++) {
			user.setNAME(nodes.get(i).getNAME());
			user.setCERTIFICATE_CODE_MD5(nodes.get(i).getCERTIFICATE_CODE_MD5());
			user.setCERTIFICATE_CODE_SUFFIX(nodes.get(i).getCERTIFICATE_CODE_SUFFIX());
			user.setSEXCODE(nodes.get(i).getSEXCODE());
			user.setGA_DEPARTMENT(nodes.get(i).getGA_DEPARTMENT());
			user.setUNIT(nodes.get(i).getUNIT());
			user.setORG_LEVEL(nodes.get(i).getORG_LEVEL());
			user.setPOLICE_SORT(nodes.get(i).getPOLICE_SORT());
			user.setPOLICE_NO(nodes.get(i).getPOLICE_NO());
			user.setSENSITIVE_LEVEL(nodes.get(i).getSENSITIVE_LEVEL());
			user.setBUSINESS_TYPE(nodes.get(i).getBUSINESS_TYPE());
			user.setTAKE_OFFICE(nodes.get(i).getTAKE_OFFICE());
			user.setUSER_STATUS(nodes.get(i).getUSER_STATUS());
			user.setPosition(nodes.get(i).getPosition());
			user.setDept(nodes.get(i).getDept());
			user.setDELETE_STATUS(User.DELSTATUSYES);
			user.setDATA_VERSION(nodes.get(i).getDATA_VERSION());
			user.setLATEST_MOD_TIME(timenow);
			
			user = dao.UserAdd(user);
		}
		
		return user;
	}
	
	private String generateHash(String idNum) throws Exception {
		
		MessageDigest digest = MessageDigest.getInstance("MD5");

		if(digest == null)
		{
			throw new Exception("generate hash operator fail.");
		}
		byte[] hash = digest.digest(idNum.getBytes());

		String result =  new BASE64Encoder().encode(hash);
		return result;
	}
	
	private String generateSuffix(String idNum) throws Exception {
		
		String value = idNum.substring( idNum.length()-6, idNum.length() );

		return value;
	}
}

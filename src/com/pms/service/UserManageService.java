package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.UserDAO;
import com.pms.dao.impl.UserDAOImpl;
import com.pms.dto.UserListItem;
import com.pms.model.User;



public class UserManageService {
	
	public User SaveUser( User user ) throws Exception
	{
		UserDAO dao = new UserDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		user.setTstamp(timenow);
		user = dao.UserAdd(user);
		return user;
	}

	public int QueryUserItems(int pid, int page, int rows,
			ArrayList<UserListItem> items) throws Exception {
		UserDAO dao = new UserDAOImpl();
		List<User> res = dao.GetUsersByParentId( pid, page, rows );
		UserListItem userItem = null;
		for(int i=0; i<res.size(); i++) {
			userItem = ConvertUserToListItem(res.get(i));
			items.add(userItem);
		}
		int total = QueryChildrenUsersCount( pid );
		return total;
	}
	
	public int QueryChildrenUsersCount(int id) throws Exception {
		UserDAO dao = new UserDAOImpl();
		int count = dao.GetUsersCountByParentId( id );
		return count;
	}

	public UserListItem ConvertUserToListItem(User user) throws Exception {
		UserListItem item = new UserListItem();
		item.setId(user.getId());
		item.setName(user.getName());
		item.setParent_id(user.getParent_id());
		item.setDept(user.getDept());
		item.setIdnum(user.getIdnum());
		item.setMax_sensitive_level(item.getMax_sensitive_level());
		item.setPolice_num(user.getPolice_num());
		item.setPolice_type(user.getPolice_type());
		item.setPosition(user.getPosition());
		item.setSex(user.getSex());
		item.setStatus(user.getStatus());
		item.setTitle(user.getTitle());
		item.setUnit(user.getUnit());

		OrgManageService oms = new OrgManageService();
		String path = oms.QueryNodePath(user.getParent_id());
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
	
}

package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.pms.dao.AppDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.model.Application;



public class AppManageService {
	
	public Application SaveApp( Application app ) throws Exception
	{
		AppDAO dao = new AppDAOImpl();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.SIMPLIFIED_CHINESE);
		String timenow = sdf.format(new Date());
		app.setTstamp(timenow);
		app = dao.AppAdd(app);
		return app;
	}

//	public int QueryUserItems(int pid, int page, int rows,
//			ArrayList<UserListItem> items) throws Exception {
//		UserDAO dao = new UserDAOImpl();
//		List<User> res = dao.GetUsersByParentId( pid, page, rows );
//		UserListItem userItem = null;
//		for(int i=0; i<res.size(); i++) {
//			userItem = ConvertUserToListItem(res.get(i));
//			items.add(userItem);
//		}
//		int total = QueryChildrenUsersCount( pid, null );
//		return total;
//	}
//	
//	public int QueryChildrenUsersCount(int id, User criteria) throws Exception {
//		UserDAO dao = new UserDAOImpl();
//		int count = dao.GetUsersCountByParentId( id, criteria );
//		return count;
//	}
//
//	public UserListItem ConvertUserToListItem(User user) throws Exception {
//		UserListItem item = new UserListItem();
//		item.setId(user.getId());
//		item.setName(user.getName());
//		item.setParent_id(user.getParent_id());
//		item.setDept(user.getDept());
//		item.setIdnum(user.getIdnum());
//		item.setMax_sensitive_level(item.getMax_sensitive_level());
//		item.setPolice_num(user.getPolice_num());
//		item.setPolice_type(user.getPolice_type());
//		item.setPosition(user.getPosition());
//		item.setSex(user.getSex());
//		item.setStatus(user.getStatus());
//		item.setTitle(user.getTitle());
//		item.setUnit(user.getUnit());
//
//		OrgManageService oms = new OrgManageService();
//		String path = oms.QueryNodePath(user.getParent_id());
//		if(path != null && path.length() > 0){
//			//name1/name2/name3/name4 -->  name1/name2/name3  and  name4
//			int index = path.lastIndexOf('/');
//			String pname = "";
//			String gname = "";
//			if( -1 == index ) {
//				pname = path;
//			}
//			else {
//				pname = path.substring(path.lastIndexOf('/')+1, path.length());
//				gname = path.substring(0, path.lastIndexOf('/'));
//			}
//			item.setPname(pname);
//			item.setGname(gname);
//		}
//		
//		return item;
//	}
//
//	public int QueryAllUserItems(int pid, User criteria, int page, int rows,
//			ArrayList<UserListItem> items) throws Exception {
//		//get all orgs;
//		List<Organization> nodes = new ArrayList<Organization>();
//		OrgManageService oms = new OrgManageService();
//		oms.queryAllChildrenNodesById(pid, null, nodes);
//		Organization first = new Organization();
//		first.setId(pid);
//		nodes.add(0, first);
//		
//		//calculate all items' count and get users;
//		UserDAO dao = new UserDAOImpl();
//		int total = 0;
//		List<User> res = new ArrayList<User>();
//		int pre_count = 0;
//		for(int i = 0; i< nodes.size(); i++) {
//			total += QueryChildrenUsersCount( nodes.get(i).getId(), criteria );
//			if( total <= ((page-1) * rows) ) {
//				pre_count = total;
//			}
//			else if ( total <= (page * rows) ) {
//				List<User> tmp = dao.GetUsersByParentIdWithNoPage( nodes.get(i).getId(), criteria );
//				if( tmp != null && tmp.size() > 0) {
//					if( pre_count < ((page-1) * rows) ) {
//						int fromIndex = rows - (pre_count%rows);
//						int toIndex = tmp.size() - fromIndex >= rows ? fromIndex+rows : tmp.size();
//						res.addAll(tmp.subList( fromIndex, toIndex));
//					}
//					else {
//						int toIndex = rows - (pre_count%rows) >= tmp.size() ? tmp.size() : rows - (pre_count%rows);
//						res.addAll(tmp.subList(0, toIndex));
//					}
//				}
//				pre_count = total;
//			} 
//			else {//total > page * rows
//				if( pre_count < (page * rows) ) {
//					List<User> tmp = dao.GetUsersByParentIdWithNoPage( nodes.get(i).getId(), criteria );
//					if( tmp != null && tmp.size() > 0 ) {
//						int toIndex =  rows - (pre_count%rows);
//						res.addAll(tmp.subList( 0, toIndex));
//					}
//				}
//				pre_count = total;
//			}
//		}
//		
//		//convert to datagrid's item
//		UserListItem userItem = null;
//		for(int i=0; i<res.size(); i++) {
//			userItem = ConvertUserToListItem(res.get(i));
//			items.add(userItem);
//		}
//		return total;
//	}
	
}

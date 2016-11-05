package com.pms.service;

import java.util.List;
import com.pms.dao.AppRoleDAO;
import com.pms.dao.impl.AppRoleDAOImpl;
import com.pms.model.AppRole;
import com.pms.util.DateTimeUtil;



public class AppRoleManageService {
	
	public AppRole SaveAppRole( AppRole ar ) throws Exception
	{
		AppRoleDAO dao = new AppRoleDAOImpl();
		String timenow = DateTimeUtil.GetCurrentTime();
		ar.setTstamp(timenow);
		ar = dao.AppRoleAdd(ar);
		return ar;
	}

	public int QueryAllAppRoleItems(int appid, AppRole criteria, int page,
			int rows, List<AppRole> items) throws Exception {
		AppRoleDAO dao = new AppRoleDAOImpl();
		List<AppRole> res = dao.GetAppRolesByAppId(appid, criteria, page, rows );
		items.addAll(res);
		int total = QueryAppRolesCountByAppId(appid, criteria );
		return total;
	}
	
	public int QueryAppRolesCountByAppId(int appid, AppRole criteria) throws Exception {
		AppRoleDAO dao = new AppRoleDAOImpl();
		int count = dao.GetAppRolesCountByAppId( appid, criteria );
		return count;
	}

	public void DeleteAppRoles(List<Integer> appRoleIds) throws Exception {
		if(appRoleIds == null)
			return;
	
		AppRole target;
		AppRoleDAO dao = new AppRoleDAOImpl();
		for(int i = 0; i< appRoleIds.size(); i++) {
			target = new AppRole();
			target.setId(appRoleIds.get(i));
			dao.AppRoleDel(target);
		}
	
		return ;
	}


//	public void DeleteApps(List<Integer> appIds) throws Exception {
//		if(appIds == null)
//			return;
//		
//		Application target;
//		AppDAO dao = new AppDAOImpl();
//		for(int i = 0; i< appIds.size(); i++) {
//			target = new Application();
//			target.setId(appIds.get(i));
//			dao.AppDel(target);
//		}
//		
//		return ;
//	}
//
//	public void QueryAllApps(List<TreeNode> treeNodes) throws Exception {
//		AppDAO dao = new AppDAOImpl();
//		List<Application> res = dao.GetApplicationsWithNopage();
//		
//		TreeNode node = null;
//		for(int i=0; i<res.size(); i++) {
//			node = ConvertAppToTreeNode(res.get(i));
//			treeNodes.add(node);
//		}
//		return;
//	}
//
//	public TreeNode ConvertAppToTreeNode(Application application) {
//		TreeNode node = new TreeNode();
//		//String state = hasChild() ? "closed" : "open";
//		String state = "open";
//		node.setState(state);
//		node.setId(application.getId());
//		node.setText(application.getName());
//		
//		return node;
//	}


	
}

package com.pms.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pms.dao.AppDAO;
import com.pms.dao.impl.AppDAOImpl;
import com.pms.dto.TreeNode;
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

	public int QueryAllAppItems(Application criteria, int page, int rows,
			List<Application> items) throws Exception {
		AppDAO dao = new AppDAOImpl();
		List<Application> res = dao.GetApplications( criteria, page, rows );
		items.addAll(res);
		int total = QueryApplicationsCount( criteria );
		return total;
	}
	
	public int QueryApplicationsCount(Application criteria) throws Exception {
		AppDAO dao = new AppDAOImpl();
		int count = dao.GetApplicationsCount( criteria );
		return count;
	}

	public void DeleteApps(List<Integer> appIds) throws Exception {
		if(appIds == null)
			return;
		
		Application target;
		AppDAO dao = new AppDAOImpl();
		for(int i = 0; i< appIds.size(); i++) {
			target = new Application();
			target.setId(appIds.get(i));
			dao.AppDel(target);
		}
		
		return ;
	}

	public void QueryAllApps(List<TreeNode> treeNodes) throws Exception {
		AppDAO dao = new AppDAOImpl();
		List<Application> res = dao.GetApplicationsWithNopage();
		
		TreeNode node = null;
		for(int i=0; i<res.size(); i++) {
			node = ConvertAppToTreeNode(res.get(i));
			treeNodes.add(node);
		}
		return;
	}

	public TreeNode ConvertAppToTreeNode(Application application) {
		TreeNode node = new TreeNode();
		//String state = hasChild() ? "closed" : "open";
		String state = "open";
		node.setState(state);
		node.setId(application.getId());
		node.setText(application.getName());
		
		return node;
	}

//	public List<Application> QueryAllAppItems() throws Exception {
//		AppDAO dao = new AppDAOImpl();
//		List<Application> res = dao.GetApplicationsWithNopage();
//		return res;
//	}
	
}

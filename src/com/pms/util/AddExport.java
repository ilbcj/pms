package com.pms.util;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pms.model.Organization;
import com.pms.model.ResData;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.User;
import com.pms.service.DataSyncService;



public class AddExport implements Job{
	private List<ResData> resDatas;
	private List<Organization> orgNode;
	private List<User> users;
	private List<ResRole> roles;
	private List<ResRoleResource> resRoleResources;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		DataSyncService dss = new DataSyncService();
		String amount = "Add";
		String exportMode = "定时导出;";
		resDatas = new ArrayList<ResData>();
		orgNode = new ArrayList<Organization>();
		users = new ArrayList<User>();
		roles = new ArrayList<ResRole>();
		resRoleResources = new ArrayList<ResRoleResource>();
		try {
			dss.DownLoadRes(amount, exportMode, resDatas);
			dss.DownLoadOrg(amount, exportMode, orgNode);
			dss.DownLoadUser(amount, exportMode, users);
			dss.DownLoadResRole(amount, exportMode, resRoleResources);
			dss.DownLoadRole(amount, exportMode, roles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

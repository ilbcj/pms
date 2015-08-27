package com.pms.service;

import java.util.ArrayList;
import java.util.List;

import com.pms.dao.OrganizationDAO;
import com.pms.dao.SyncConfigDao;
import com.pms.dao.impl.OrganizationDAOImpl;
import com.pms.dao.impl.SyncConfigDaoImpl;
import com.pms.dto.OrgListItem;
import com.pms.model.Organization;
import com.pms.model.SyncConfig;

public class SyncConfigService {
	public int QueryAllSyncConfigItems(SyncConfig criteria, int page, int rows, List<SyncConfig> items) throws Exception {
		SyncConfigDao dao = new SyncConfigDaoImpl();
		List<SyncConfig> res = dao.GetSyncConfigs(criteria, page, rows );
		items.addAll(res);
		int total = QueryAllSyncConfigsCount( criteria );
		return total;
	}
	
	private int QueryAllSyncConfigsCount(SyncConfig criteria) throws Exception {
		SyncConfigDao dao = new SyncConfigDaoImpl();
		int count = dao.GetSyncConfigsCount( criteria );
		return count;
	}
	
	public SyncConfig SaveSyncConfig(SyncConfig syncConfig) throws Exception {
		SyncConfigDao dao = new SyncConfigDaoImpl();
		syncConfig = dao.SyncConfigAdd(syncConfig);
		return syncConfig;
	}
	
	public int QuerySyncConfigOrg(Organization criteria, int page, int rows, List<OrgListItem> items) throws Exception {
		SyncConfigDao dao = new SyncConfigDaoImpl();
		List<SyncConfig> res = dao.GetSyncConfigs( null, 0, 0 );
		
		List<String> data =new ArrayList<String>();
		for(int i = 0; i< res.size(); i++) {
			data.add(res.get(i).getGA_DEPARTMENT());
		}
		
		OrganizationDAO orgdao = new OrganizationDAOImpl();
		List<Organization> org=orgdao.GetOrgByIdNotIn(data, criteria, page, rows);;
		OrgListItem node = null;
		for(int i=0; i<org.size(); i++) {
			OrgManageService oms = new OrgManageService();
			node = oms.ConvertOrgToListItem(org.get(i));
			items.add(node);
		}
		
		int total = QuerySyncConfigOrgCount( data, criteria );
		return total;
	}
	
	public List<Organization> QuerySyncConfigOrg(String pid) throws Exception {
		SyncConfigDao dao = new SyncConfigDaoImpl();
		List<SyncConfig> res = dao.GetSyncConfigs( null, 0, 0 );
		
		List<String> data =new ArrayList<String>();
		for(int i = 0; i< res.size(); i++) {
			data.add(res.get(i).getGA_DEPARTMENT());
		}
		
		OrganizationDAO orgdao = new OrganizationDAOImpl();
		List<Organization> org=orgdao.GetOrgNodeByParentIdAndIdNotIn( pid, data );;
		
		return org;
	}
	
	private int QuerySyncConfigOrgCount(List<String> id, Organization criteria) throws Exception {
		OrganizationDAO dao = new OrganizationDAOImpl();
		int count = dao.GetSyncConfigOrgCount( id, criteria );
		return count;
	}
	
}

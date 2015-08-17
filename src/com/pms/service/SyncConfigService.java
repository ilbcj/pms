package com.pms.service;

import java.util.List;

import com.pms.dao.SyncConfigDao;
import com.pms.dao.impl.SyncConfigDaoImpl;
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
		System.out.println(syncConfig.getCLUE_DST_SYS());
		syncConfig = dao.SyncConfigAdd(syncConfig);
		return syncConfig;
	}
}

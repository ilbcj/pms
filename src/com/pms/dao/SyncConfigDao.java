package com.pms.dao;

import java.util.List;

import com.pms.model.SyncConfig;
import com.pms.model.SyncList;

public interface SyncConfigDao {
	public List<SyncConfig> GetSyncConfigs(SyncConfig criteria, int page, int rows) throws Exception;
	public int GetSyncConfigsCount(SyncConfig criteria) throws Exception;
	public SyncConfig SyncConfigAdd(SyncConfig syncConfig) throws Exception;
	public List<SyncConfig> GetAllSyncConfigs() throws Exception;
	
	public SyncList SyncListAdd(SyncList syncList) throws Exception;
	public void SyncListMod(SyncList syncList) throws Exception;
	public List<SyncList> GetAllSyncList(int status) throws Exception;
}

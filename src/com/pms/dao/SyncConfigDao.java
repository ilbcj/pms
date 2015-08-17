package com.pms.dao;

import java.util.List;

import com.pms.model.SyncConfig;

public interface SyncConfigDao {

	List<SyncConfig> GetSyncConfigs(SyncConfig criteria, int page, int rows) throws Exception;
	int GetSyncConfigsCount(SyncConfig criteria) throws Exception;
	SyncConfig SyncConfigAdd(SyncConfig syncConfig) throws Exception;

}

package com.pms.dao;

import java.util.List;

import com.pms.model.SystemConfig;

public interface SystemConfigDAO {
	List<SystemConfig> GetConfigByType(int type) throws Exception;
	SystemConfig UpdateConfig(SystemConfig systemConfig) throws Exception;
}

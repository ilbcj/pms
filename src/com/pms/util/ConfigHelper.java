package com.pms.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pms.dao.SystemConfigDAO;
import com.pms.dao.impl.SystemConfigDAOImpl;
import com.pms.model.SystemConfig;

public class ConfigHelper {
	
	private static Log logger = LogFactory.getLog(ConfigHelper.class);
	
	private static ConfigHelper inst = null;
	private static String region = null;
	
	private ConfigHelper()
	{
	}
	
	public static ConfigHelper getInstance() {
		if( inst == null ) {
			inst = new ConfigHelper();
		}
		return inst;
	}
	
	public static String getRegion() {
		if(region == null) {
			try {
				SystemConfigDAO dao = new SystemConfigDAOImpl();
				List<SystemConfig> confItems = dao.GetConfigByType(SystemConfig.SYSTEMCONFIGTYPESYSTEM);
				if(confItems != null) {
					for( SystemConfig item : confItems) {
						if( SystemConfig.SYSTEMCONFIG_ITEM_REGION.equalsIgnoreCase(item.getItem()) ) {
							region = item.getValue();
						}
					}
				}
			}
			catch (Exception e) {
				logger.info("Get region config from db failed, error info:" + e.getMessage() );
				region = null;
			}
		}
		return region;
	}
}

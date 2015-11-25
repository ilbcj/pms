package com.pms.util.timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pms.service.DataSyncService;


public class DatasyncNoticTimer implements Job{
	private static Log logger = LogFactory.getLog(DatasyncNoticTimer.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("[TDSN]通知服务定时器启动");
		DataSyncService dss = new DataSyncService();
		dss.NotificationPush();
		logger.info("[TDSN]通知服务定时器完成");
	}

}

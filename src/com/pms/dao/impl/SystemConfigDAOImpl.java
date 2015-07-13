package com.pms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pms.dao.SystemConfigDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.SystemConfig;

public class SystemConfigDAOImpl implements SystemConfigDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemConfig> GetConfigByType(int type) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SystemConfig> rs = null;
		String sqlString = "SELECT * FROM systemconfig where type=:type";
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SystemConfig.class);
			q.setInteger("type", type);
			rs = q.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return rs;
		
	}
	
	@Override
	public void UpdateConfig(SystemConfig systemConfig) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		try
		{
			session.update(systemConfig);
			tx.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return;	

	}

}

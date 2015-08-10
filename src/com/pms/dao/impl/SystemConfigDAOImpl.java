package com.pms.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

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
	public SystemConfig UpdateConfig(SystemConfig systemConfig) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			systemConfig = (SystemConfig) session.merge(systemConfig);
			tx.commit();
		}
		catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名数据资源。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
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
		return systemConfig;	

	}

}

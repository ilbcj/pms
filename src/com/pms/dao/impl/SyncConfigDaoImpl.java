package com.pms.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.SyncConfigDao;
import com.pms.model.HibernateUtil;
import com.pms.model.SyncConfig;

public class SyncConfigDaoImpl implements SyncConfigDao{
	@Override
	public SyncConfig SyncConfigAdd(SyncConfig syncConfig) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		try
		{
			syncConfig = (SyncConfig) session.merge(syncConfig);
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
		return syncConfig;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SyncConfig> GetSyncConfigs( SyncConfig criteria, int page, int rows) throws Exception {		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		List<SyncConfig> rs = null;
		String sqlString = "select * from syncconfig where 1 = 1 ";
		if( criteria != null ) {
			if(criteria.getId() != 0) {
				sqlString += " and id = :id ";
			}
			if(criteria.getCLUE_DST_SYS() != null && criteria.getCLUE_DST_SYS().length() > 0) {
				sqlString += " and CLUE_DST_SYS like :CLUE_DST_SYS ";
			}
		}
		
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(SyncConfig.class);
			if( criteria != null ) {
				if(criteria.getId() != 0) {
					q.setInteger( "id", criteria.getId());
				}
				if(criteria.getCLUE_DST_SYS() != null && criteria.getCLUE_DST_SYS().length() > 0) {
					q.setString( "CLUE_DST_SYS", "%" + criteria.getCLUE_DST_SYS() + "%" );
				}		

			}
			if( page > 0 && rows > 0) {
				q.setFirstResult((page-1) * rows);   
				q.setMaxResults(rows);
			}
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
	public int GetSyncConfigsCount(SyncConfig criteria) throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		int rs;
		String sqlString = "select count(*) from syncconfig where 1 = 1 ";
//		if( criteria != null ) {
//			if( criteria != null ) {
//				if(criteria.getName() != null && criteria.getName().length() > 0) {
//					sqlString += " and name like :name ";
//				}
//				if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
//					sqlString += " and RESOURCE_ID = :RESOURCE_ID ";
//				}
//			}
//		}
		
		try {
			Query q = session.createSQLQuery(sqlString);
//			if( criteria != null ) {
//				if( criteria != null ) {
//					if(criteria.getName() != null && criteria.getName().length() > 0) {
//						q.setString( "name", "%" + criteria.getName() + "%" );
//					}
//					if(criteria.getRESOURCE_ID() != null && criteria.getRESOURCE_ID().length() > 0) {
//						q.setString( "RESOURCE_ID", criteria.getRESOURCE_ID());
//					}
//				}
//			}
			rs = ((BigInteger)q.uniqueResult()).intValue();
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
	
}
